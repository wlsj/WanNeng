

package com.wls.wnlibrary.utils.net.subsciber;

import android.content.Context;


import com.wls.wnlibrary.utils.net.exception.ApiException;
import com.wls.wnlibrary.utils.net.utils.HttpLog;

import java.lang.ref.WeakReference;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

import static com.wls.wnlibrary.utils.net.utils.Utils.isNetworkAvailable;

/**
 * <p>描述：订阅的基类</p>
 * 1.可以防止内存泄露。<br>
 * 2.在onStart()没有网络时直接onCompleted();<br>
 * 3.统一处理了异常<br>

 */
public abstract class BaseSubscriber<T> extends DisposableObserver<T> {
    public WeakReference<Context> contextWeakReference;
    
    public BaseSubscriber() {
    }

    @Override
    protected void onStart() {
        HttpLog.e("-->http is onStart");
        if (contextWeakReference != null && contextWeakReference.get() != null && !isNetworkAvailable(contextWeakReference.get())) {
            //Toast.makeText(context, "无网络，读取缓存数据", Toast.LENGTH_SHORT).show();
            onComplete();
        }
    }


    public BaseSubscriber(Context context) {
        if (context != null) {
            contextWeakReference = new WeakReference<>(context);
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        HttpLog.e("-->http is onNext");
    }

    @Override
    public final void onError(Throwable e) {
        HttpLog.e("-->http is onError");
        if (e instanceof ApiException) {
            HttpLog.e("--> e instanceof ApiException err:" + e);
            onError((ApiException) e);
        } else {
            HttpLog.e("--> e !instanceof ApiException err:" + e);
            onError(ApiException.handleException(e));
        }
    }

    @Override
    public void onComplete() {
        HttpLog.e("-->http is onComplete");
    }


    public abstract void onError(ApiException e);

}
