package cn.com.infocloud.vo;

import java.io.Serializable;

public class MessageSendResVo implements Serializable {
    private String msgId;
    private Integer status;
    private String desc;
    private String failPhones;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFailPhones() {
        return failPhones;
    }

    public void setFailPhones(String failPhones) {
        this.failPhones = failPhones;
    }
}
