package cn.infocloud.cc.request;

import java.util.List;
import java.util.Map;

public class ApiResponse {
    private Map<String, List<String>> header;
    private byte[] body;
    private int status = 0;
    private String charset = "UTF-8";

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
