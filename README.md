# WanNeng 
慢慢更新，API会一直有变动，直到稳定版，其中诸多参考借鉴，望使用的同时提出建议意见。
##依赖
只针对于AS的玩家，别的idea的玩家可以下载源码
首先在工程的gradle的allprojects当中添加maven之后添加
url'https://jitpack.io'
```
allprojects {
    repositories {
        google()
        jcenter()
        maven{
            url'https://jitpack.io'
        }
    }
}
```
然后在Module的gradle的dependencies中添加
implementation 'com.github.wlsj:WanNeng:1.2.3'
```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    implementation 'com.github.wlsj:WanNeng:1.2.3'
}
```
这时候可能有V7包的冲突，可以忽略

## 网络请求
需要在application初始化HttpQingQiu.init(this);
```
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpQingQiu.init(this);
    }
}
```
然后在类或者接口中发起请求
要先设置baseURL，就是主域名
HttpQingQiu.getInstance().setBaseUrl("http://fanyi.youdao.com");

### get请求
```
  HttpQingQiu.get("/openapi.do?keyfrom=MyFristBlog&key=1985316716&type=data&doctype=json&version=1.1&q=买了否冷").execute(new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {
                //失败后的处理
            }

            @Override
            public void onSuccess(String s) {
                //回调成功的类
            }
        });
```
### post请求，参数以K_V的形式添加
```
  HttpQingQiu.post("/openapi.do")
                .params("keyfrom", "MyFristBlog")
                .params("key", "1985316716")
                .params("type", "data")
                .params("doctype", "json")
                .params("version", "1.1")
                .params("q", "买了否冷")
                .execute(new CallBack<String>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.e("TAG", "onSuccess: " + s);
                    }
                });
                ```
## 解析

                Entity entity = JsonJieXi.GsonToBean(s, Entity.class);、//s为获取到的字符串，Entity是实体类