package cn.com.infocloud.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author gusy
 */
public class ApiRequest implements Serializable {

    private Map<String, Object> headers;

    private Map<String, Object> bodyParams;

    private String url;

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getBodyParams() {
        return bodyParams;
    }

    public void setBodyParams(Map<String, Object> bodyParams) {
        this.bodyParams = bodyParams;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
