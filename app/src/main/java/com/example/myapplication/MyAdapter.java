package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter {
    public MyAdapter(Context context, int resource, List<RateItem> list) {
        super(context, resource,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView=convertView;
        if(itemView==null){
            itemView= LayoutInflater.from(getContext()).inflate(R.layout.activity_my_list,parent,false);
        }
        RateItem item=(RateItem) getItem(position);
        TextView title=(TextView)itemView.findViewById(R.id.itemTitle);
        TextView detail=(TextView)itemView.findViewById(R.id.itemDetail);
        title.setText(item.getCurName());
        detail.setText(item.getCurRate());
        return itemView;
    }



}
