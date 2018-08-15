
package com.wls.wnlibrary.utils.net.func;

import com.wls.wnlibrary.utils.net.exception.ApiException;
import com.wls.wnlibrary.utils.net.exception.ServerException;
import com.wls.wnlibrary.utils.net.model.ApiResult;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * <p>描述：ApiResult<T>转换T</p>
 */
public class HandleFuc<T> implements Function<ApiResult<T>, T> {
    @Override
    public T apply(@NonNull ApiResult<T> tApiResult) throws Exception {
        if (ApiException.isOk(tApiResult)) {
            return tApiResult.getData();// == null ? Optional.ofNullable(tApiResult.getData()).orElse(null) : tApiResult.getData();
        } else {
            throw new ServerException(tApiResult.getCode(), tApiResult.getMsg());
        }
    }
}
