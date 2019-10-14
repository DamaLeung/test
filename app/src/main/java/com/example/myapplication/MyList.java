package com.example.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MyList extends ListActivity implements Runnable, AdapterView.OnItemClickListener {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Thread t=new Thread(this);
        t.start();
        getListView().setOnItemClickListener(this);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    ArrayList<HashMap<String,String>>show= (ArrayList<HashMap<String,String>>) msg.obj;
//                    SimpleAdapter listItemAdpter=new SimpleAdapter(MyList.this,
//                            show,
//                            R.layout.activity_my_list,
//                            new String[]{"ItemTitle","ItemDetail"},
//                            new int[]{R.id.itemTitle,R.id.itemDetail});
//                    MyList.this.setListAdapter(listItemAdpter);
                    MyAdapter myAdapter=new MyAdapter(MyList.this,R.layout.activity_my_list,show);
                    setListAdapter(myAdapter);
                }
                super.handleMessage(msg);

            }
        };
    }

    @Override
    public void run() {
        //获取网络数据
        String url = "http://www.usd-cny.com/icbc.htm";
        Document doc;
        List<HashMap<String,String>> list= new ArrayList<HashMap<String,String>>();
        try {
            doc = Jsoup.connect(url).get();
            Element table=doc.getElementsByTag("table").first();
            Elements trs=table.getElementsByTag("tr");
            for(int i=1;i<trs.size();i++){
                Element td=trs.get(i);
                Elements tds=td.getElementsByTag("td");
                String name=tds.get(0).text();
                String rate=tds.get(1).text();
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("ItemTitle",name);
                map.put("ItemDetail",rate);
                list.add(map);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Message msg=handler.obtainMessage(1);
        msg.obj=list;
        handler.sendMessage(msg);
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
}
