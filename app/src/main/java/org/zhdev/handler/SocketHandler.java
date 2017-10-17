package org.zhdev.handler;

import android.os.Handler;
import android.os.Message;

/**
 * Created by MACHENIKE on 2017/10/10.
 */

public class SocketHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what)
        {
            case 1://1发送

                break;
            default:
                break;
        }
        super.handleMessage(msg);
    }
}
