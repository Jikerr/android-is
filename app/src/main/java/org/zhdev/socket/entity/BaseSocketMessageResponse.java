package org.zhdev.socket.entity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @Author: zh
 * @Description :
 * @Date Created in 15:51 2017/10/13
 * @Modified By :
 */
public class BaseSocketMessageResponse implements Serializable {

    private static final long serialVersionUID = -3491319648103195155L;
    private String responseActionType;//响应哪个action


    private String handlerEvent;//处理事件
    private String responseBody;//响应主体

    private Integer code;//响应代码 200 成功 500 服务器错误 404 不接受的请求类型

    private String toClientId;//发送到哪个客户端ID
    private String fromClientId;//来自哪个客户端ID
    private Users toUser;//发送到哪个用户
    private Users fromUser;//来自哪个用户
    private String msgDate;//消息时间
    private long msgDateStamp;//消息时间戳

    public String getResponseActionType() {
        return responseActionType;
    }

    public void setResponseActionType(String responseActionType) {
        this.responseActionType = responseActionType;
    }

    public String getHandlerEvent() {
        return handlerEvent;
    }

    public void setHandlerEvent(String handlerEvent) {
        this.handlerEvent = handlerEvent;
    }

    public JSONObject getResponseBody() {
        JSONObject jsonObject = null;
        try {
            if (null != this.responseBody && !"".equals(this.responseBody)) {
                jsonObject = new JSONObject(this.responseBody);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void setResponseBody(JSONObject responseBody) {
        this.responseBody = responseBody.toString();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
