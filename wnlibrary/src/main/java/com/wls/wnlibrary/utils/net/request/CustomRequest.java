
package com.wls.wnlibrary.utils.net.request;


import com.google.gson.reflect.TypeToken;
import com.wls.wnlibrary.utils.net.cache.model.CacheResult;
import com.wls.wnlibrary.utils.net.callback.CallBack;
import com.wls.wnlibrary.utils.net.callback.CallBackProxy;
import com.wls.wnlibrary.utils.net.func.ApiResultFunc;
import com.wls.wnlibrary.utils.net.func.CacheResultFunc;
import com.wls.wnlibrary.utils.net.func.HandleFuc;
import com.wls.wnlibrary.utils.net.func.RetryExceptionFunc;
import com.wls.wnlibrary.utils.net.model.ApiResult;
import com.wls.wnlibrary.utils.net.subsciber.CallBackSubsciber;
import com.wls.wnlibrary.utils.net.transformer.HandleErrTransformer;
import com.wls.wnlibrary.utils.net.utils.RxUtil;
import com.wls.wnlibrary.utils.net.utils.Utils;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * <p>描述：自定义请求，例如你有自己的ApiService</p>
 */
@SuppressWarnings(value={"unchecked", "deprecation"})
public class CustomRequest extends BaseRequest<CustomRequest> {
    public CustomRequest() {
        super("");
    }

    @Override
    public CustomRequest build() {
        return super.build();
    }

    /**
     * 创建api服务  可以支持自定义的api，默认使用BaseApiService,上层不用关心
     *
     * @param service 自定义的apiservice class
     */
    public <T> T create(final Class<T> service) {
        checkvalidate();
        return retrofit.create(service);
    }

    private void checkvalidate() {
        Utils.checkNotNull(retrofit, "请先在调用build()才能使用");
    }

    /**
     * 调用call返回一个Observable<T>
     * 举例：如果你给的是一个Observable<ApiResult<AuthModel>> 那么返回的<T>是一个ApiResult<AuthModel>
     */
    public <T> Observable<T> call(Observable<T> observable) {
        checkvalidate();
        return observable.compose(RxUtil.io_main())
                .compose(new HandleErrTransformer())
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    public <T> void call(Observable<T> observable, CallBack<T> callBack) {
        call(observable, new CallBackSubsciber(context, callBack));
    }

    public <R> void call(Observable observable, Observer<R> subscriber) {
        observable.compose(RxUtil.io_main())
                .subscribe(subscriber);
    }


    /**
     * 调用call返回一个Observable,针对ApiResult的业务<T>
     * 举例：如果你给的是一个Observable<ApiResult<AuthModel>> 那么返回的<T>是AuthModel
     */
    public <T> Observable<T> apiCall(Observable<ApiResult<T>> observable) {
        checkvalidate();
        return observable
                .map(new HandleFuc<T>())
                .compose(RxUtil.<T>io_main())
                .compose(new HandleErrTransformer<T>())
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    public <T> Disposable apiCall(Observable<T> observable, CallBack<T> callBack) {
        return call(observable, new CallBackProxy<ApiResult<T>, T>(callBack) {
        });
    }

    public <T> Disposable call(Observable<T> observable, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        Observable<CacheResult<T>> cacheobservable = build().toObservable(observable, proxy);
        if (CacheResult.class != proxy.getCallBack().getRawType()) {
            return cacheobservable.compose(new ObservableTransformer<CacheResult<T>, T>() {
                @Override
                public ObservableSource<T> apply(@NonNull Observable<CacheResult<T>> upstream) {
                    return upstream.map(new CacheResultFunc<T>());
                }
            }).subscribeWith(new CallBackSubsciber<T>(context, proxy.getCallBack()));
        } else {
            return cacheobservable.subscribeWith(new CallBackSubsciber<CacheResult<T>>(context, proxy.getCallBack()));
        }
    }

    @SuppressWarnings(value={"unchecked", "deprecation"})
    private <T> Observable<CacheResult<T>> toObservable(Observable observable, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return observable.map(new ApiResultFunc(proxy != null ? proxy.getType() : new TypeToken<ResponseBody>() {
        }.getType()))
                .compose(isSyncRequest ? RxUtil._main() : RxUtil._io_main())
                .compose(rxCache.transformer(cacheMode, proxy.getCallBack().getType()))
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    @Override
    protected Observable<ResponseBody> generateRequest() {
        return null;
    }
}
