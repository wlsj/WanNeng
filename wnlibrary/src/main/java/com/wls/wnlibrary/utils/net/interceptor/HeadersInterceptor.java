
package com.wls.wnlibrary.utils.net.interceptor;
import com.wls.wnlibrary.utils.net.model.HttpHeaders;
import com.wls.wnlibrary.utils.net.utils.HttpLog;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>描述：配置公共头部</p>
 */
public class HeadersInterceptor implements Interceptor {

    private HttpHeaders headers;

    public HeadersInterceptor(HttpHeaders headers) {
        this.headers = headers;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (headers.headersMap.isEmpty()) return chain.proceed(builder.build());
        try {
            for (Map.Entry<String, String> entry : headers.headersMap.entrySet()) {
                //去除重复的header
                //builder.removeHeader(entry.getKey());
                //builder.addHeader(entry.getKey(), entry.getValue()).build();
                builder.header(entry.getKey(), entry.getValue()).build();
            }
        } catch (Exception e) {
            HttpLog.e(e);
        }
        return chain.proceed(builder.build());

    }
}
