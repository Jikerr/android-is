package org.zhdev.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import org.json.JSONObject;
import org.zhdev.socket.entity.BaseSocketMessageRequest;
import org.zhdev.socket.entity.BaseSocketMessageResponse;
import org.zhdev.socket.entity.MessageBean;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * Created by MACHENIKE on 2017/10/9.
 */

public class SocketClient {

    ObjectOutputStream dos = null;
    ObjectInputStream dis = null;

    private boolean bConnected = false;
    private Socket clientSocket;
    //private Thread receviceListener;

    private Handler mainActivityHandler;
    //private OutputStream outputStream;
    //private PrintWriter printWriter;
    //private BufferedReader br;
    //private InputStream is;

    public SocketClient(Handler mainActivityHandler) {
        this.mainActivityHandler = mainActivityHandler;
    }

    //handler为线程之间通信的桥梁
    /*public Handler socketHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:  //1为发送
                    MessageBean messageBean = new MessageBean();
                    messageBean.setMsgContent("安卓发过来的消息!");
                    sendMessageToServer(messageBean);
                    break;
                default :
                    break;
            }
        }
    };*/

    public void tryAgainConnect(String host, int port) {
        System.out.println("try again Connect...");
        connectServer(host, port);
    }

    public boolean connectServer(String host, int port) {
        //1.创建客户端 ,连接到指定host和端口
        try {
            clientSocket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
            clientSocket.connect(inetSocketAddress, 3000);//三秒连接超时
            if (clientSocket.isConnected()) {
                dos = new ObjectOutputStream(clientSocket.getOutputStream());
                dis = new ObjectInputStream(clientSocket.getInputStream());
                System.out.println("~~~~~~~~connected~~~~~~~~!");
                bConnected = true;
                mainActivityHandler.sendEmptyMessage(1);
            }

        } catch (Exception e) {
            //if (e instanceof SocketTimeoutException && e instanceof SocketException) {//只在SocketTimeoutException时候重新连接
                e.printStackTrace();
                tryAgainConnect(host, port);
            //}
            //if (e instanceof IOException) {//这里当切换WIFI的时候 报出ConnectException异常
                //e.printStackTrace();
            //}
        }
        return bConnected;
    }

    public void startReceviceData() {
        //4,启动接收线程
        ReceviceThread receviceThread = new ReceviceThread();
        System.out.println("recevice thread is started...");
        //receviceListener = new Thread(receviceThread);
        //receviceListener.start();
        new Thread(receviceThread).start();
    }

    public void sendMessageToServer(BaseSocketMessageRequest request) {
        SendThread sendThread = new SendThread(request);
        new Thread(sendThread).start();
    }

    public void shutdownSocket() {
        //关闭资源
        try {
            bConnected = false;
            if (dos != null) {
                dos.close();
            }
            if (dis != null) {
                dis.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            //printWriter.close();
            //outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SendThread implements Runnable {
        private BaseSocketMessageRequest request;

        public SendThread(BaseSocketMessageRequest request) {
            this.request = request;
        }

        @Override
        public void run() {
            try {
                System.out.println("request server actionType : " + request.getActionType());
                System.out.println("request server parameters : " + request.getRequestParameters());
                dos.writeObject(request);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    class ReceviceThread implements Runnable {
        @Override
        public void run() {
            String info = null;
            try {
                while (bConnected) {
                    BaseSocketMessageResponse response = (BaseSocketMessageResponse) dis.readObject();
                    //if(str.indexOf("heartbeatData")==-1){
                    String handlerEvent = response.getHandlerEvent();
                    JSONObject responseBody = response.getResponseBody();

                    if (null != handlerEvent && "".equals(handlerEvent)) {
                        System.out.println("from server handler event : " + handlerEvent);
                    }
                    if (null != response.getResponseBody()) {
                        System.out.println("from server response message : " + responseBody.toString());
                    }
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();// 存放数据
                    bundle.putSerializable("server-message", response);//使用Parcelable接口存放实体
                    message.what = 2;//设置what为2 , 1为发送
                    message.setData(bundle);//放置data

                    mainActivityHandler.sendMessage(message);//发送到队列,通知ui线程
                }
                System.out.println("接收线程已经停止...");
                /*while((info=br.readLine())!=null){
                    System.out.println("我是客户端，服务器说："+info);
                }*/
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                shutdownSocket();
            }
        }
    }

   /* public static void main(String[] args) {
        SocketClient client = new SocketClient();
        try {
            client.connectServer("localhost", 8888);
            client.startReceviceData();

            while (true) {
                System.out.println("请输入要发送到服务器的字符 : ");
                Scanner sc = new Scanner(System.in);
                String inputString = sc.next();
                if (null != inputString && !"".equals(inputString)) {
                    if (inputString.equals("exit")) {
                        break;
                    }
                    MessageBean msg = new MessageBean();
                    msg.setMsgContent("我是客户端 : "+new Date().getTime());
                    client.sendMessageToServer(msg);
                }
            }
            client.shutdownSocket();
            System.out.println("程序结束...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
