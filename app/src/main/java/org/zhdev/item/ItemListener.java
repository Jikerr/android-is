package org.zhdev.item;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.zhdev.config.R;

/**
 * Created by MACHENIKE on 2017/10/9.
 */

public class ItemListener implements OnItemClickListener,OnItemLongClickListener {

    private Context context;

    public ItemListener(Context context){
        this.context = context;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //处理点击事件
        System.out.print("++++++点击++++++");
        TextView tv = (TextView)view.findViewById(R.id.textView2_content);
        Toast.makeText(context,
                tv.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //处理长按事件
        TextView tv = (TextView)view.findViewById(R.id.textView2_content);
        Toast.makeText(context,
                tv.getText(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
