
package com.wls.wnlibrary.utils.net.cache.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * <p>描述：通用转换器接口</p>
 * 1.实现该接口可以实现一大波的磁盘存储操作。<br>
 * 2.可以实现Serializable、Gson,Parcelable、fastjson、xml、kryo等等<br>
 * 目前只实现了：GsonDiskConverter和SerializableDiskConverter转换器，有其它自定义需求自己去实现吧！<br>
 * <p>
 * 《看到能很方便的实现一大波功能，是不是很刺激啊(*>﹏<*)》<br>
 * <p>
 */
public interface IDiskConverter {

    /**
     * 读取
     *
     * @param source 输入流
     * @param type  读取数据后要转换的数据类型
     *               这里没有用泛型T或者Tyepe来做，是因为本框架决定的一些问题，泛型会丢失
     * @return
     */
    <T> T load(InputStream source, Type type);

    /**
     * 写入
     *
     * @param sink
     * @param data 保存的数据
     * @return
     */
    boolean writer(OutputStream sink, Object data);

}
