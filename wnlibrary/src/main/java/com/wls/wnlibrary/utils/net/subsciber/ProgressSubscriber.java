

package com.wls.wnlibrary.utils.net.subsciber;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.wls.wnlibrary.utils.net.exception.ApiException;


/**
 * <p>描述： 实现带有进度的订阅</p>
 * <p>
 * 1.支持自定义加载进度框<br>
 * 2.支持对话框取消时可以自动终止本次请求，取消订阅。<br>
 * 修改内容：支持自定义对话框<br>

 */
public abstract class ProgressSubscriber<T> extends BaseSubscriber<T> implements ProgressCancelListener {
    private IProgressDialog progressDialog;
    private Dialog mDialog;
    private boolean isShowProgress = true;


    /**
     * 默认不显示弹出框，不可以取消
     *
     * @param context  上下文
     */
    public ProgressSubscriber(Context context) {
        super(context);
        init(false);
    }

    /**
     * 自定义加载进度框
     *
     * @param context 上下文
     * @param progressDialog 自定义对话框
     */
    public ProgressSubscriber(Context context, IProgressDialog progressDialog) {
        super(context);
        this.progressDialog = progressDialog;
        init(false);
    }

    /**
     * 自定义加载进度框,可以设置是否显示弹出框，是否可以取消
     *
     * @param context 上下文 
     * @param progressDialog 对话框
     * @param isShowProgress  是否显示对话框
     * @param isCancel  对话框是否可以取消
     */
    public ProgressSubscriber(Context context, IProgressDialog progressDialog, boolean isShowProgress, boolean isCancel) {
        super(context);
        this.progressDialog = progressDialog;
        this.isShowProgress = isShowProgress;
        init(isCancel);
    }

    /**
     * 初始化
     *
     * @param isCancel 对话框是否可以取消
     */
    private void init(boolean isCancel) {
        if (progressDialog == null) return;
        mDialog = progressDialog.getDialog();
        if (mDialog == null) return;
        mDialog.setCancelable(isCancel);
        if (isCancel) {
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    ProgressSubscriber.this.onCancelProgress();
                }
            });
        }
    }

    /**
     * 展示进度框
     */
    private void showProgress() {
        if (!isShowProgress) {
            return;
        }
        if (mDialog != null) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }

    /**
     * 取消进度框
     */
    private void dismissProgress() {
        if (!isShowProgress) {
            return;
        }
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    @Override
    public void onStart() {
        showProgress();
    }

    @Override
    public void onComplete() {
        dismissProgress();
    }

    @Override
    public void onError(ApiException e) {
        dismissProgress();
        //int errCode = e.getCode();
        /*if (errCode == ApiException.ERROR.TIMEOUT_ERROR) {
            ToastUtil.showToast(contextWeakReference.get(), "网络中断，请检查您的网络状态");
        } else if (errCode == ApiException.ERROR.NETWORD_ERROR) {
            ToastUtil.showToast(contextWeakReference.get(), "请检查您的网络状态");
        } else {
            ToastUtil.showToast(contextWeakReference.get(), "error:" + e.getMessage());
        }*/
    }

    @Override
    public void onCancelProgress() {
        if (!isDisposed()) {
            dispose();
        }
    }
}
