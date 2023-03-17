package cn.infocloud.cc.vo;

import java.io.Serializable;

public class ArriveInfoResVo implements Serializable {
    private String msgId;
    private String arrive;
    private String tel;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
