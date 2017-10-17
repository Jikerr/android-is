package org.zhdev.socket.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zh
 * @Description :
 * @Date Created in 10:47 2017/10/10
 * @Modified By :
 */
public class MessageBean implements Serializable{
    private static final long serialVersionUID = 8860622433909654915L;

    private String msgType;
    private Date msgDate;
    private String msgContent;
    private String toClientId;
    private String fromClientId;
    private Users user;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Date getMsgDate() {
        return msgDate;
    }

    private void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getToClientId() {
        return toClientId;
    }

    public void setToClientId(String toClientId) {
        this.toClientId = toClientId;
    }

    public String getFromClientId() {
        return fromClientId;
    }

    public void setFromClientId(String fromClientId) {
        this.fromClientId = fromClientId;
    }
}
