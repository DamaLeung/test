package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MyList extends ListActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    Handler handler;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        RateManager rateManager=new RateManager(this);
        List<RateItem> list=new ArrayList<RateItem>();
        list=rateManager.listAll();
        myAdapter = new MyAdapter(this, R.layout.activity_my_list,list);
        setListAdapter(myAdapter);
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        HashMap<String,String> map=(HashMap<String,String>)getListView().getItemAtPosition(i);
//        String title=map.get("ItemTitle");
//        String detail=map.get("ItemDetail");
        TextView title=(TextView)view.findViewById(R.id.itemTitle);
        TextView detail=(TextView)view.findViewById(R.id.itemDetail);
        String titleStr=String.valueOf(title.getText());
        String detailStr=String.valueOf(detail.getText());
        Log.i("123", "onItemClick: ");
        Intent intent=new Intent(this, ListClick.class);
        Bundle bundle=new Bundle();
        bundle.putString("type",titleStr);
        bundle.putFloat("rate",Float.valueOf(detailStr));
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final int position=i;
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myAdapter.remove(getListView().getItemAtPosition(position));
                    }
                })
                .setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}
