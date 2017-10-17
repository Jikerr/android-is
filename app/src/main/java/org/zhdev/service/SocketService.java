package org.zhdev.service;

/**
 * Created by MACHENIKE on 2017/10/17.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import org.zhdev.socket.SocketReceiver;


public class SocketService extends Service {

    private SocketReceiver socketReceiver;

    public static final String SOCKER_ACTION = "org.zhdev.socket.action";
    public static final String SOCKER_RECEIVER = "org.zhdev.socket.receiver.action";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service", "socket service created");

        socketReceiver = new SocketReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SOCKER_ACTION);
        registerReceiver(socketReceiver, filter);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("service", "socket service start");
    }


    @Override
    public void onDestroy() {
        Log.i("service", "socket service destroy!");
    }

}