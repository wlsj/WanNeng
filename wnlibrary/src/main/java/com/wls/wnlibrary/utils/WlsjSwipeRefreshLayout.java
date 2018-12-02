package com.wls.wnlibrary.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wls.wnlibrary.R;

public class WlsjSwipeRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {
    //加载更多监听
    private OnLoadingListener listener;
    //尾布局
    private View footerView;
    //是否在加载的过程中
    private boolean isLoading = false;
    private ListView lv;
    //滑动到最底端时的上拉操作
    private int mTouchSlop;
    //按下时的Y坐标
    private int mYDown;
    //抬起时的Y坐标,和mYDown一同用于判断滑动到最底端时的上拉或下滑
    private int mLastY;

    public WlsjSwipeRefreshLayout(Context context) {
        super(context, null);
        initFooterView(context);
    }

    public WlsjSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initFooterView(context);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //判断是否可以加载更多
        if (canLoad()) {
            //加载更多
            loadData();
        }
    }

    //定义加载更多的接口
    public interface OnLoadingListener {
        public void onLoading();
    }

    public void setOnLoadingListener(OnLoadingListener listener) {
        this.listener = listener;
    }

    //添加尾布局
    private void initFooterView(Context context) {
        footerView = LayoutInflater.from(context).inflate(R.layout.shanglajiazai_weibuju, null, false);
    }


    //是否是加载中
    public void setLoadingState(boolean state) {
        isLoading = state;
        //是加载中则添加尾布局,否则移除尾布局
        if (state) {
            lv.addFooterView(footerView);
        } else {
            lv.removeFooterView(footerView);
            mLastY = 0;
            mYDown = 0;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //如果ListView不为空时,则获取ListView对象并为其设置滚动监听
        if (lv == null) {
            int childCount = getChildCount();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    if (view instanceof ListView) {
                        lv = (ListView) view;
                        lv.setOnScrollListener(this);
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //按下,或许Y坐标
                mYDown = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动获取Y坐标
                mLastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //抬起,如果满足加载更多,则进行加载更多
                if (canLoad()) {
                    loadData();
                }

        }
        return super.dispatchTouchEvent(ev);
    }

    //是否可以加载更多,是否到达底部,并ListView不在加载中,执行上拉操作
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    //判断是否滑到最底部
    private boolean isBottom() {
        if (lv != null && lv.getAdapter() != null) {
            return lv.getLastVisiblePosition() == (lv.getAdapter().getCount() - 1);
        }
        return false;
    }

    //判断是否是上拉操作
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    //如果是最底部,且为上拉操作,不在加载中,则加载更多
    private void loadData() {
        if (listener != null) {
            setLoadingState(true);
            listener.onLoading();
        }
    }
}

