package io.github.silencelwy.smsapi.request;

import java.io.Serializable;
import java.util.Map;

public class ApiRequest implements Serializable {

    private Map<String, String> headers;

    private Map<String, String> bodyParams;

    private String url;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getBodyParams() {
        return bodyParams;
    }

    public void setBodyParams(Map<String, String> bodyParams) {
        this.bodyParams = bodyParams;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
