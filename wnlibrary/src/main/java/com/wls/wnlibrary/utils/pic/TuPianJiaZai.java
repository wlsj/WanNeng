package com.wls.wnlibrary.utils.pic;

/*
 *
 * wlsj 2018/12/06
 *
 * */


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class TuPianJiaZai {

    public static void JiaZaiTuPian(Context mContext, String path, int zhanWei, int shiBai, boolean shiFouHuanCun,ImageView mImageView) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(zhanWei)
                .error(shiBai)
                .skipMemoryCache(shiFouHuanCun)
                ;

        Glide.with(mContext)
                .load(path)
                .apply(requestOptions)
                .into(mImageView);

    }


}
