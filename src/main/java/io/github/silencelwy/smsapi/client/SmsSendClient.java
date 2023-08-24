package io.github.silencelwy.smsapi.client;

import io.github.silencelwy.smsapi.request.ApiRequest;
import io.github.silencelwy.smsapi.request.ApiResponse;
import com.alibaba.fastjson.JSON;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import java.io.*;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.Map;

public class SmsSendClient {

    private static final RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
            //如果获得这个异常，则执行重试
            .handle(SocketTimeoutException.class, IOException.class, Exception.class)
            //延迟3秒
            .withDelay(Duration.ofSeconds(3))
            //最多尝试5次
            .withMaxRetries(5);

    public static ApiResponse post(ApiRequest apiRequest) {
        ApiResponse apiResponse = new ApiResponse();
        Map<String, Object> header = apiRequest.getHeaders();
        String url = apiRequest.getUrl();
        Map<String, Object> bodyParams = apiRequest.getBodyParams();
        Request request = Request.Post(url)
                .addHeader("Content-Type","application/json;charset=utf-8")
                .version(HttpVersion.HTTP_1_1)
                //设置连接超时
                .connectTimeout(15000)
                //设置文本读取超时
                .socketTimeout(15000);
        header.forEach((k, v) -> request.addHeader(k, String.valueOf(v)));
        InputStream content = null;
        ByteArrayOutputStream outStream = null;
        try {
            HttpResponse httpResponse = Failsafe.with(retryPolicy).get(() ->
                    request.bodyString(JSON.toJSONString(bodyParams), ContentType.APPLICATION_JSON)
                            .execute().returnResponse());

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            apiResponse.setStatus(statusCode);
            content = httpResponse.getEntity().getContent();
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = content.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            apiResponse.setBody(outStream.toByteArray());

        } catch (Exception exception){
            exception.printStackTrace();
            apiResponse.setStatus(404);
            apiResponse.setBody(exception.getMessage().getBytes());
        }finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (content != null) {
                    content.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return apiResponse;
    }
}
