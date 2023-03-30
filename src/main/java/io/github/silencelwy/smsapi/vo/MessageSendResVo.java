package io.github.silencelwy.smsapi.vo;

import java.io.Serializable;

public class MessageSendResVo implements Serializable {
    private String msgId;
    private String desc;
    private String failPhones;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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
