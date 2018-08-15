
package com.wls.wnlibrary.utils.net.cache.stategy;

import com.wls.wnlibrary.utils.net.cache.RxCache;
import com.wls.wnlibrary.utils.net.cache.model.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * <p>描述：只读缓存</p>
 *<-------此类加载用的是反射 所以类名是灰色的 没有直接引用  不要误删----------------><br>
 */
public final class OnlyCacheStrategy extends BaseStrategy{
    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, long time, Observable<T> source, Type type) {
        return loadCache(rxCache,type,key,time,false);
    }
}
