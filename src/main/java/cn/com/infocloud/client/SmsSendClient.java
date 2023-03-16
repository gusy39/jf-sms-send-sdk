package cn.com.infocloud.client;

import cn.com.infocloud.vo.ApiRequest;
import cn.com.infocloud.vo.ApiResponse;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsSendClient {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SmsSendClient.class);
    public static ApiResponse post(ApiRequest request) {

        Map<String, Object> header = request.getHeaders();
        ApiResponse response = new ApiResponse();
        DataOutputStream out = null;
        InputStream is = null;
        try {
            String url = request.getUrl();
            URL console = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)console.openConnection(Proxy.NO_PROXY);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type", "application/json");
            Iterator var11 = header.entrySet().iterator();
            while(var11.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var11.next();
                conn.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
            }

            conn.connect();
            out = new DataOutputStream(conn.getOutputStream());

            out.write(JSON.toJSONString(request.getBodyParams()).getBytes(HttpCharacterEncoding.DEFAULT_ENCODING));
            out.flush();
            int statusCode = conn.getResponseCode();

            if (statusCode != 200) {
                throw new Exception("网络请求异常");
            }


            is = conn.getInputStream();
            if (is != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                byte[] bytes = outStream.toByteArray();
                if (request != null) {
                    try {
                        String s = new String(bytes, HttpCharacterEncoding.DEFAULT_ENCODING);
                        return JSON.parseObject(s,ApiResponse.class);
                    } catch (UnsupportedEncodingException var2) {
                        LOGGER.error(var2.getMessage());
                        return ApiResponse.error(4001,var2.getMessage());
                    }
                }
            }
        } catch (MalformedURLException var35) {
            LOGGER.error(var35.getMessage());
            return ApiResponse.error(4001,var35.getMessage());
        } catch (UnsupportedEncodingException var36) {
            LOGGER.error(var36.getMessage());
            return response;
        } catch (IOException var37) {
            LOGGER.error(var37.getMessage());
            return ApiResponse.error(4001,var37.getMessage());
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var34) {
                    LOGGER.error(var34.getMessage());
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException var33) {
                    LOGGER.error(var33.getMessage());
                }
            }

        }

        return response;
    }
}
