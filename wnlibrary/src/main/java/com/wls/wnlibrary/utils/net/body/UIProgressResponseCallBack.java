
package com.wls.wnlibrary.utils.net.body;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * <p>描述：可以直接更新UI的回调</p>
 */
public abstract class UIProgressResponseCallBack implements ProgressResponseCallBack {
    private static final int RESPONSE_UPDATE = 0x02;

    //处理UI层的Handler子类
    private static class UIHandler extends Handler {
        //弱引用
        private final WeakReference<UIProgressResponseCallBack> mUIProgressResponseListenerWeakReference;

        public UIHandler(Looper looper, UIProgressResponseCallBack uiProgressResponseListener) {
            super(looper);
            mUIProgressResponseListenerWeakReference = new WeakReference<UIProgressResponseCallBack>(uiProgressResponseListener);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPONSE_UPDATE:
                    UIProgressResponseCallBack uiProgressResponseListener = mUIProgressResponseListenerWeakReference.get();
                    if (uiProgressResponseListener != null) {
                        //获得进度实体类
                        ProgressModel progressModel = (ProgressModel) msg.obj;
                        //回调抽象方法
                        uiProgressResponseListener.onUIResponseProgress(progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    //主线程Handler
    private final Handler mHandler = new UIHandler(Looper.getMainLooper(), this);

    @Override
    public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
        //通过Handler发送进度消息
        Message message = Message.obtain();
        message.obj = new ProgressModel(bytesWritten, contentLength, done);
        message.what = RESPONSE_UPDATE;
        mHandler.sendMessage(message);
    }

    /**
     * UI层回调抽象方法
     *
     * @param bytesRead     当前读取响应体字节长度
     * @param contentLength 总字节长度
     * @param done          是否读取完成
     */
    public abstract void onUIResponseProgress(long bytesRead, long contentLength, boolean done);

    public class ProgressModel implements Serializable {
        //当前读取字节长度
        private long currentBytes;
        //总字节长度
        private long contentLength;
        //是否读取完成
        private boolean done;

        public ProgressModel(long currentBytes, long contentLength, boolean done) {
            this.currentBytes = currentBytes;
            this.contentLength = contentLength;
            this.done = done;
        }

        public long getCurrentBytes() {
            return currentBytes;
        }

        public ProgressModel setCurrentBytes(long currentBytes) {
            this.currentBytes = currentBytes;
            return this;
        }

        public long getContentLength() {
            return contentLength;
        }

        public ProgressModel setContentLength(long contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public boolean isDone() {
            return done;
        }

        public ProgressModel setDone(boolean done) {
            this.done = done;
            return this;
        }

        @Override
        public String toString() {
            return "ProgressModel{" +
                    "currentBytes=" + currentBytes +
                    ", contentLength=" + contentLength +
                    ", done=" + done +
                    '}';
        }
    }

}
