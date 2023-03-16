package cn.com.infocloud.vo;

import java.io.Serializable;


/**
 * @author gusy
 */
public class ApiResponse implements Serializable {
    public static Integer SUCCESS = 200;

    private Integer code;
    private String message;
    private MessageSendResVo data;
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

    public MessageSendResVo getData() {
        return data;
    }

    public void setData(MessageSendResVo data) {
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public static ApiResponse error(Integer code, String remark) {
        ApiResponse response = new ApiResponse();
        response.setCode(code);
        response.setMessage(remark);
        return response;
    }


}
