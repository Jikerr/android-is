package org.zhdev.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zhdev.config.R;
import org.zhdev.service.SocketService;
import org.zhdev.socket.Constants;
import org.zhdev.socket.SocketReceiver;
import org.zhdev.socket.entity.BaseSocketMessageRequest;
import org.zhdev.socket.entity.BaseSocketMessageResponse;
import org.zhdev.socket.entity.MessageBean;
import org.zhdev.entity.News;
import org.zhdev.item.ItemListener;
import org.zhdev.item.Adapter;
import org.zhdev.socket.SocketClient;
import org.zhdev.socket.entity.Users;
import org.zhdev.utils.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import static org.zhdev.service.SocketService.SOCKER_RECEIVER;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    Vector<News> news = new Vector<News>();
    Adapter myAdapter;

    PullToRefreshView mPullToRefreshView;
    private TextView userNameTextView;
    private TextView loginLogTextView;
    public Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /******** ListView 列表相关开始 ******/
        listView = (ListView) findViewById(R.id.listView1);
        //绑定单个点击事件监听器
        listView.setOnItemClickListener(new ItemListener(MainActivity.this));
        //绑定长按事件监听器
        listView.setOnItemLongClickListener(new ItemListener(MainActivity.this));
        myAdapter = new Adapter(news, MainActivity.this, mListener);//传入监听器 , 监听单条的按钮事件
        listView.setAdapter(myAdapter);
        /******** ListView 列表相关结束 ******/

        //启动网络相关操作的单独子线程
        new Thread(networkTask).start();

        SocketReceiver socketReceiver = new SocketReceiver();
        IntentFilter socketIntentFilter = new IntentFilter();
        socketIntentFilter.addAction(SOCKER_RECEIVER);
        registerReceiver(socketReceiver,socketIntentFilter);

        Intent socketIntent = new Intent();
        socketIntent.setClass(MainActivity.this, SocketService.class);
        startService(socketIntent);       // 启动  Socket 服务

        //下拉刷新数据
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //三秒后将下拉刷新的状态变为刷新完成
                        myAdapter.clearItems();
                        getOnlineList(1);
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    //handler为线程之间通信的桥梁
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:  //根据上面的提示，当Message为1，表示数据处理完了，可以通知主线程了
                    myAdapter.notifyDataSetChanged();        //这个方法一旦调用，UI界面就刷新了
                    break;
                default:
                    break;
            }
        }
    };


    public void alertConnected() {
        Toast.makeText(this, "连接成功!", Toast.LENGTH_SHORT).show();

    }

    //网络相关变量,套接字等
    private SocketClient socketClient;
    //private Handler socketHandler;//通知socket执行发送操作的handler

    //handler为线程之间通信的桥梁
    private Handler mainActivityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://0.连接失败

                    break;
                case 1:  //1.连接成功
                    alertConnected();
                    //2.登录
                    try {
                        BaseSocketMessageRequest loginRequest = new BaseSocketMessageRequest();
                        loginRequest.setActionType(Constants.Action.LOGIN);
                        loginRequest.setToClientId("0");
                        loginRequest.setToUser(new Users());
                        loginRequest.setMsgDate(DateUtils.getNowDate());
                        loginRequest.setMsgDateStamp(DateUtils.getNowDateStamp());
                        //loginRequest.setFromClientId(new Users());
                        //loginRequest.setFromUser(user);

                        //MessageBean messageBean = new MessageBean();
                        //messageBean.setMsgType("Login");
                        //messageBean.setToClientId("0");

                        Bundle extras = getIntent().getExtras();
                        String mEmail = extras.getString("mEmail");
                        String mPassword = extras.getString("mPassword");

                        userNameTextView = (TextView) findViewById(R.id.userName_text);
                        userNameTextView.setText("无用户名");
                        loginLogTextView = (TextView) findViewById(R.id.loginLog_text);
                        loginLogTextView.setText(mEmail);

                        JSONObject requestJson = new JSONObject();
                        requestJson.put("deviceType", "android");
                        requestJson.put("userName", mEmail);

                        loginRequest.setRequestParameters(requestJson);
                        socketClient.sendMessageToServer(loginRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:  //2.接受消息
                    messageHandler(msg);//处理消息
                    //myAdapter.addItemToFirst("服务器消息",messageBean.getMsgContent());
                    break;
                default:
                    break;
            }
        }
    };


    public void messageHandler(Message msg) {
        BaseSocketMessageResponse response = (BaseSocketMessageResponse) msg.getData().getSerializable("server-message");

        String handlerEvent = response.getHandlerEvent();
        JSONObject responseBody = response.getResponseBody();

        try {
            switch (handlerEvent) {
                case Constants.HandlerEvent.GET_ONLINE_LIST_RESULT:
                    JSONArray onlineList = responseBody.getJSONArray("data");
                    for (int i = 0; i < onlineList.length(); i++) {
                        JSONObject userJsonObj = onlineList.getJSONObject(i);
                        String deviceType = userJsonObj.getString("deviceType");
                        String userName = userJsonObj.getString("userName");
                        String clientId = userJsonObj.getString("clientId");
                        userName = userName + "-" + clientId;
                        myAdapter.addItemToFirst(userName, deviceType);
                    }

                    break;
                case Constants.HandlerEvent.LOGIN_RESPONSE:
                    int code = responseBody.getInt("code");//登录结果识别码
                    user = response.getToUser();//登录成功后把用户对象保存在当前Activity
                    if (code == 200) {//登录成功 获取在线列表
                        getOnlineList(1);
                    } else {
                        String loginErrorMsg = responseBody.getString("msg");
                        Toast.makeText(this, "登录失败," + loginErrorMsg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.HandlerEvent.MESSAGE_FORWARD_USER_OFFLINE://用户已经离线通知
                    Toast.makeText(this, "对方离线,消息接收失败", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.HandlerEvent.MESSAGE_FORWARD_SUCCESS://服务器成功转发消息通知
                    Toast.makeText(this, "消息(命令)发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.HandlerEvent.HREATBEAT_DATA:
                    break;
                default:
                    Toast.makeText(this, "处理Socket消息中失败!", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //网络相关操作的单独子线程
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            //连接socket
            socketClient = new SocketClient(mainActivityHandler);
            socketClient.connectServer("192.168.100.105", 8888);
            socketClient.startReceviceData();

        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class ViewHolder {
        public TextView tvTitle;
        public TextView tvContent;
        public Button operationButton;
    }

    private Adapter.MyClickListener mListener = new Adapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            //单击了关闭电源按钮,要发送对端命令消息
            try {
                String targetSendParam = news.get(position).title;
                Toast.makeText(MainActivity.this, "关闭 " + targetSendParam + " 电源命令正在送达.", Toast.LENGTH_SHORT).show();

                BaseSocketMessageRequest request = new BaseSocketMessageRequest();
                request.setActionType(Constants.Action.FORWARD_COMMAND_TOUSER);
                request.setFromClientId(user.getClientId());
                request.setFromUser(user);
                request.setMsgDateStamp(DateUtils.getNowDateStamp());
                request.setMsgDate(DateUtils.getNowDate());

                //从用户选定的项中获取用户名和客户端ID参数
                String[] targetSendParamArray = targetSendParam.split("-");
                String userName = targetSendParamArray[0];
                String toClientId = targetSendParamArray[1];

                request.setToClientId(toClientId);
                request.setToUser(null);

                JSONObject contentJsonObj = new JSONObject();
                contentJsonObj.put("command", "shutdown");//执行的命令
                contentJsonObj.put("remark", "shutdown host command , from android");//备注信息
                request.setRequestParameters(contentJsonObj);
                //发送到服务器
                socketClient.sendMessageToServer(request);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            /*Toast.makeText(
                    MainActivity.this,
                    "listview的内部的按钮被点击了！，位置是-->" + position + ",内容是-->"
                            + news.get(position).content, Toast.LENGTH_SHORT)
                    .show();*/
        }
    };
    //实现ListView的监听器的接口
    /*int visibleLastIndex = 0; //最后一个显示的索引

    public class ListViewListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //如果屏幕滑到最下面了，并且scrollState的状态为：滚动完毕之后ListView处于停止状态（手离开屏幕），
            if (visibleLastIndex == myAdapter.getCount() && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                new LoadDataThread().start();//如果满足上面的条件，此时，可以加载新数据了
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        }
    }*/

    /* //初始化数据
     public void initOnlineList() {
         MessageBean messageBean = new MessageBean();
         messageBean.setMsgType("getOnlineList");
         socketClient.sendMessageToServer(messageBean);
     }
     //额外开启一个线程，模拟加载数据
     class LoadDataThread extends Thread {
         @Override
         public void run() {
             try {
                 Thread.sleep(2000);//休眠两秒
                 initOnlineList();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             //新数据加载完成后，通过handle通知主线,，将这个新数据显示在UI界面上（因为涉及到线程安全问题）
             handler.sendEmptyMessage(1);
         }
     }*/
    public void getOnlineList(int index) {
        BaseSocketMessageRequest request = new BaseSocketMessageRequest();
        request.setActionType(Constants.Action.GET_ONLINE_LIST);
        request.setFromClientId(user.getClientId());
        request.setFromUser(user);
        request.setMsgDate(DateUtils.getNowDate());
        request.setMsgDateStamp(DateUtils.getNowDateStamp());
        request.setRequestParameters(new JSONObject());
        request.setToClientId("0");
        request.setToUser(new Users());
        socketClient.sendMessageToServer(request);
    }

}
