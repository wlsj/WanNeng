
package com.wls.wnlibrary.utils.net.cache.stategy;


import com.wls.wnlibrary.utils.net.cache.RxCache;
import com.wls.wnlibrary.utils.net.cache.model.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * <p>描述：实现缓存策略的接口，可以自定义缓存实现方式，只要实现该接口就可以了</p>
 */
public interface IStrategy {

    /**
     * 执行缓存
     *
     * @param rxCache   缓存管理对象
     * @param cacheKey  缓存key
     * @param cacheTime 缓存时间
     * @param source    网络请求对象
     * @param type     要转换的目标对象
     * @return 返回带缓存的Observable流对象
     */
    <T> Observable<CacheResult<T>> execute(RxCache rxCache, String cacheKey, long cacheTime, Observable<T> source, Type type);

}
