package org.zhdev.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static org.zhdev.service.SocketService.SOCKER_RECEIVER;


/**
 * Created by MACHENIKE on 2017/10/17.
 */

public class SocketReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if(action.equals(SOCKER_RECEIVER)) {
            String url = intent.getExtras().getString("action");
            System.out.println("=============== URL : "+url);
        }
    }
}