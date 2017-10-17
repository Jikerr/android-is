package org.zhdev.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.zhdev.activity.MainActivity;
import org.zhdev.config.R;
import org.zhdev.entity.News;

import java.util.Vector;

/**
 * Created by MACHENIKE on 2017/10/9.
 */

public class Adapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private Vector<News> news = new Vector<News>();
    private MyClickListener mListener;

    public Adapter(Vector<News> news, Context context,MyClickListener listener) {
        this.mContext = context;
        this.news = news;
        this.mInflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return news.size();
    }

    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearItems() {
        news.removeAllElements();
        notifyDataSetChanged();
    }

    public void addItemToFirst(String title, String content) {
        News n = new News();
        n.title = title;
        n.content = content;
        news.add(0, n);//增加到顶部
        notifyDataSetChanged();
    }

    public void addItemToEnd(String title, String content) {
        News n = new News();
        n.title = title;
        n.content = content;
        news.add(n);//增加到底部
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainActivity.ViewHolder viewHolder = new MainActivity.ViewHolder();
        //通过下面的条件判断语句，来循环利用。如果convertView = null ，表示屏幕上没有可以被重复利用的对象。
        if (convertView == null) {
            //创建View
            convertView = mInflater.inflate(R.layout.activity_main_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.textView1_title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.textView2_content);
            viewHolder.operationButton = (Button) convertView.findViewById(R.id.button);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MainActivity.ViewHolder) convertView.getTag();
        }
        //从Vector中取出数据填充到ListView列表项中
        News n = news.get(position);
        viewHolder.tvTitle.setText(n.title);
        viewHolder.tvContent.setText(n.content);
        viewHolder.operationButton.setOnClickListener(mListener);
        viewHolder.operationButton.setTag(position);

        //viewHolder.operationButton.setOnClickListener(mListener);
        return convertView;
    }

    public static abstract class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }

}
