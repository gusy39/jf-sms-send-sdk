package io.github.silencelwy.smsapi.vo;

import java.io.Serializable;


/**
 * @author gusy
 */
public class SmsSendResponse<T> implements Serializable {
    public static Integer SUCCESS = 200;

    private Integer code;
    private String message;
    private T data;
    private String requestId;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public static SmsSendResponse error(Integer code, String remark) {
        SmsSendResponse response = new SmsSendResponse();
        response.setCode(code);
        response.setMessage(remark);
        return response;
    }


}
