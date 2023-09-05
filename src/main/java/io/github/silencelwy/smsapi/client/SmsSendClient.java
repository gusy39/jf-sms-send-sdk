package io.github.silencelwy.smsapi.client;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import io.github.silencelwy.smsapi.request.ApiRequest;
import io.github.silencelwy.smsapi.request.ApiResponse;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SmsSendClient {

    private static final RetryPolicy retryPolicy = new RetryPolicy().withDelay(3,TimeUnit.SECONDS).withMaxRetries(5).abortOn(SocketTimeoutException.class, IOException.class, Exception.class);

    public static ApiResponse post(ApiRequest apiRequest) {
        ApiResponse apiResponse = new ApiResponse();
        final Map<String, String> header = apiRequest.getHeaders();
        final String url = apiRequest.getUrl();
        final Map<String, String> bodyParams = apiRequest.getBodyParams();

        HttpResponse httpResponse = Failsafe.with(retryPolicy).get(
                new Callable<HttpResponse>() {
                    @Override
                    public HttpResponse call() {
                        return HttpRequest.post(url)
                                .addHeaders(header)
                                .body(JSONUtil.toJsonStr(bodyParams))
                                .timeout(15000)
                                .setConnectionTimeout(15000)
                                .setReadTimeout(60000)
                                .keepAlive(true)
                                .header(Header.CONTENT_TYPE, "application/json;charset=utf-8").execute();
                    }
                }
        );
        apiResponse.setStatus(httpResponse.getStatus());
        apiResponse.setBody(httpResponse.bodyBytes());

        return apiResponse;
    }
}