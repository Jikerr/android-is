package org.zhdev.socket;

/**
 * @Author: zh
 * @Description :
 * @Date Created in 15:38 2017/10/13
 * @Modified By :
 */
public class Constants {
    public final static class Action{
        public final static String LOGIN = "Login";//请求登录操作
        public final static String GET_ONLINE_LIST = "GetOnlineList";//请求用户在线列表
        public final static String FORWARD_COMMAND_TOUSER = "ForwardCommandToUser";//请求转发用户命令到指定用户
    }

    public final static class HandlerEvent{
        public final static String GET_ONLINE_LIST_RESULT ="GetOnlineListResult";//获取在线列表结果
        public final static String LOGIN_RESPONSE = "LoginResponse";//登录响应
        public final static String MESSAGE_FORWARD_SUCCESS = "MessageForwardSuccess";//消息转发成功标记
        public final static String MESSAGE_FORWARD_USER_OFFLINE = "UserOffline";//用户下线通知
        public final static String FROM_USER_COMMAND = "FromUserCommand";//来自用户的命令
        public final static String FROM_USER_TEXT_MESSAGE = "FromUserTextMessage";//来自用户的文本消息
        public final static String HREATBEAT_DATA = "HreatbeatData";//心跳数据
    }
}
