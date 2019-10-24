# WanNeng 
慢慢更新，API会一直有变动，直到稳定版，其中诸多参考借鉴，望使用的同时提出建议意见。暂时不支持非restful API 接口
## 依赖
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

                Entity entity = JsonJieXi.GsonToBean(s, Entity.class);
                //s为获取到的字符串，Entity是实体类

## 底部导航栏
               
### 布局内添加

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/myRoot"
    android:layout_weight="1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <FrameLayout
        android:id="@+id/fl_container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="0.9">
    </FrameLayout>

    <com.wls.wnlibrary.utils.ui.DiBuDaoHang
        android:id="@+id/dibudaohang"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="0.1"
        />
</LinearLayout>

```
### 类中使用

```
private DiBuDaoHang diBuDaoHang;

diBuDaoHang = findViewById(R.id.dibu_daohang);

diBuDaoHang.setNeiRong(R.id.neirong)
        .setBiaoTiKaiShiHeDianJiHouDeYanSe("#FFFF4431","#452565")
        .addYiGe(BlankFragment.class,"首页",R.mipmap.ic_launcher_round,R.mipmap.ic_launcher)
        .addYiGe(BlankFragment2.class,"贷款",R.mipmap.ic_launcher_round,R.mipmap.ic_launcher)
        .addYiGe(BlankFragment3.class,"还款",R.mipmap.ic_launcher_round,R.mipmap.ic_launcher)
        .addYiGe(BlankFragment4.class,"我的",R.mipmap.ic_launcher_round,R.mipmap.ic_launcher)
        .build();
```

## 图片加载
```
private ImageView imageView;

imageView = findViewById(R.id.iv_mian2);

String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544085757843&di=5588db72a553cc7eaf4a7019e2a7d498&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20180621%2F3496efefa4e647719e68b63b39d94275.jpeg";
//参数为：上下文、占位图、加载错误图、是否缓存、自己的view
TuPianJiaZai.JiaZaiTuPian(this,url,R.mipmap.ic_launcher,R.mipmap.ic_launcher,false,imageView);

```
## 图片处理（占坑）

```
Bitmap bitmap = TuPianChuLiJianBan.转为BitMap(imageView);
Bitmap bimap2 = TuPianChuLiJianBan.图片模糊处理(bitmap, 2.2f, this);
imageView.setImageBitmap(bimap2);

```
