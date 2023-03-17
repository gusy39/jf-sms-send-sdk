package cn.infocloud.cc.client;

import cn.infocloud.cc.request.ApiRequest;
import cn.infocloud.cc.request.ApiResponse;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class SmsSendClient {
    public static ApiResponse post(ApiRequest request) {

        Map<String, Object> header = request.getHeaders();
        ApiResponse response = new ApiResponse();
        DataOutputStream out = null;
        InputStream is = null;

        try {
            String url = request.getUrl();
            URL console = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) console.openConnection(Proxy.NO_PROXY);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type", "application/json");
            Iterator var11 = header.entrySet().iterator();
            while (var11.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) var11.next();
                conn.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }

            conn.connect();
            out = new DataOutputStream(conn.getOutputStream());

            out.write(JSON.toJSONString(request.getBodyParams()).getBytes(HttpCharacterEncoding.DEFAULT_ENCODING));
            out.flush();
            int statusCode = conn.getResponseCode();
            response.setHeader(conn.getHeaderFields());
            response.setStatus(statusCode);
            if (statusCode != 200) {
                return response;
            }

            is = conn.getInputStream();
            if (is != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                response.setBody(outStream.toByteArray());
                return response;
            }
        } catch (MalformedURLException var35) {
            var35.printStackTrace();
            response.setBody(var35.getMessage().getBytes());
            return response;
        } catch (UnsupportedEncodingException var36) {
            var36.printStackTrace();
            response.setBody(var36.getMessage().getBytes());
            return response;
        } catch (IOException var37) {
            var37.printStackTrace();
            response.setBody(var37.getMessage().getBytes());
            return response;
        }  finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var34) {
                    var34.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var33) {
                    var33.printStackTrace();
                }
            }

        }

        return response;
    }
}
