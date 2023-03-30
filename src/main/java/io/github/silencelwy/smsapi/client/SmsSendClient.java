package io.github.silencelwy.smsapi.client;

import io.github.silencelwy.smsapi.request.ApiRequest;
import io.github.silencelwy.smsapi.request.ApiResponse;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.*;
import java.util.Map;

public class SmsSendClient {
    public static ApiResponse post(ApiRequest apiRequest) {
        ApiResponse apiResponse = new ApiResponse();
        Map<String, Object> header = apiRequest.getHeaders();
        String url = apiRequest.getUrl();
        Map<String, Object> bodyParams = apiRequest.getBodyParams();
        Request request = Request.Post(url)
                .version(HttpVersion.HTTP_1_1)
                //设置连接超时
                .connectTimeout(15000)
                //设置文本读取超时
                .socketTimeout(15000);
        header.forEach((k,v)->request.addHeader(k,String.valueOf(v)));
        InputStream content = null;
        ByteArrayOutputStream outStream = null;
        try {
            HttpResponse httpResponse = request.bodyString(JSON.toJSONString(bodyParams), ContentType.APPLICATION_JSON)
                        .execute()
                        .returnResponse();
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

        } catch (IOException ioException) {
            apiResponse.setStatus(404);
            apiResponse.setBody(ioException.getMessage().getBytes());
        }finally {
            try {
                if (outStream!=null){
                    outStream.close();
                }
                if (content !=null){
                    content.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return apiResponse;
    }
}
