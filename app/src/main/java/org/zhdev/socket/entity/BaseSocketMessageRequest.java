package org.zhdev.socket.entity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zh
 * @Description :
 * @Date Created in 15:51 2017/10/13
 * @Modified By :
 */
public class BaseSocketMessageRequest implements Serializable{

    private static final long serialVersionUID = -3491319648103195155L;
    private String actionType;//请求action
    private String requestParameters;//请求参数
    private String toClientId;//发送到哪个客户端ID
    private String fromClientId;//来自哪个客户端ID
    private Users toUser;//发送到哪个用户
    private Users fromUser;//来自哪个用户
    private String msgDate;//消息时间
    private long msgDateStamp;//消息时间戳


    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public JSONObject getRequestParameters() {
        JSONObject jsonObject = null;
        try {
            if(null!=this.requestParameters && !"".equals(this.requestParameters)){
                jsonObject = new JSONObject(this.requestParameters);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void setRequestParameters(JSONObject requestParameters) {
        this.requestParameters = requestParameters.toString();
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

    public Users getToUser() {
        return toUser;
    }

    public void setToUser(Users toUser) {
        this.toUser = toUser;
    }

    public Users getFromUser() {
        return fromUser;
    }

    public void setFromUser(Users fromUser) {
        this.fromUser = fromUser;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public long getMsgDateStamp() {
        return msgDateStamp;
    }

    public void setMsgDateStamp(long msgDateStamp) {
        this.msgDateStamp = msgDateStamp;
    }
}
