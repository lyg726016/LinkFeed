package com.lyg.lyg.comment_friends_05;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class page999_Comment_ListView_Adapter extends ArrayAdapter<page999_Comment_for_Listview> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<page999_Comment_for_Listview> page999_comment_for_listviewArrayList = null;
    private Context mContext = null;


    public page999_Comment_ListView_Adapter(Context c, int textViewResourceId,
                              ArrayList<page999_Comment_for_Listview> arrays) {
        super(c, textViewResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        View v = convertview;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.page999_comment_for_listview, null);
            viewHolder.name = (TextView)v.findViewById(R.id.page999_comment_name);
            viewHolder.comment_content = (TextView)v.findViewById(R.id.page999_comment_content);
            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.name.setText(getItem(position).getName());
        viewHolder.comment_content.setText(getItem(position).getComment());

        return v;
    }

    class ViewHolder{
        public TextView name = null;
        public TextView comment_content = null;

    }
}