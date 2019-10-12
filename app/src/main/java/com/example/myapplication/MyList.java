package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;



public class MyList extends ListActivity implements Runnable{
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list);
        Thread t=new Thread(this);
        t.start();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    List<String>show= (List<String>) msg.obj;
                    ListAdapter adapter=new ArrayAdapter<String>(MyList.this,
                            android.R.layout.simple_list_item_1,show);
                    setListAdapter(adapter);
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
        List<String> list=new ArrayList<String>();
        try {
            doc = Jsoup.connect(url).get();
            Element table=doc.getElementsByTag("table").first();
            Elements trs=table.getElementsByTag("tr");
            for(int i=1;i<trs.size();i++){
                Element td=trs.get(i);
                Elements tds=td.getElementsByTag("td");
                String name=tds.get(0).text();
                String rate=tds.get(1).text();

                list.add(name+">>>"+rate);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Message msg=handler.obtainMessage(1);
        msg.obj=list;
        handler.sendMessage(msg);
    }
}
