package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class exchange extends AppCompatActivity implements View.OnClickListener,Runnable {
    Float rd;
    Float re;
    Float rw;
    Handler handler;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        final SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                  RateManager rateManager=new RateManager(exchange.this);
                  List<RateItem> list=new ArrayList<RateItem>();
                  list=rateManager.listAll();
                  rd=Float.valueOf(list.get(2).getCurRate());
                  re=Float.valueOf(list.get(13).getCurRate());
                  rw=100/Float.valueOf(list.get(20).getCurRate());
                  SharedPreferences.Editor editor = sp.edit();
                    editor.putFloat("rateD",rd);
                    editor.putFloat("rateE",re);
                    editor.putFloat("rateW",rw);
                    editor.apply();
                }
                super.handleMessage(msg);

            }
        };
        rd=sp.getFloat("rateD",7.1224f);
        re=sp.getFloat("rateE",7.7883f);
        rw=sp.getFloat("rateW",168.2719f);
        String updateDate=sp.getString("update_date","");
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String todayStr=sdf.format(today);
//        if(!todayStr.equals(updateDate)){
            Thread t=new Thread(this);
            t.start();
//        }
    }

    @Override
    public void onClick(View view) {
        TextView out = findViewById(R.id.rate);
        TextView origin = findViewById(R.id.origin);
        String str = origin.getText().toString();
        if(view.getId()==R.id.config){
            Intent intent=new Intent(this,Config.class);
            startActivityForResult(intent,1);
        }else if(view.getId()==R.id.update){
            Thread t=new Thread(this);
            t.start();
        }else if(view.getId()==R.id.check){
            Intent list=new Intent(this,MyList.class);
            startActivity(list);
        }
        else if(str.length()>0){

            String f = null;
            Float res = Float.valueOf(str);
            switch (view.getId()) {
                case R.id.dollar:
                    res /= rd;
                    f = "$";
                    break;
                case R.id.euro:
                    f = "€";
                    res /= re;
                    break;
                case R.id.won:
                    res *= rw;
                    f = "₩";

            }
            out.setText(String.format("%.2f", res) + f);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==2){
            Bundle bun=data.getExtras();
            rd=bun.getFloat("rateD",7.1043f);
            re=bun.getFloat("rateE",7.8098f);
            rw=bun.getFloat("rateW",167.994f);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        TextView out = findViewById(R.id.rate);
        TextView origin = findViewById(R.id.origin);
        String str = origin.getText().toString();
        if(item.getItemId()==R.id.mc){
            Intent intent=new Intent(this,Config.class);
            startActivityForResult(intent,1);
        }
        else if(str.length()>0){
            String f = null;
            Float res = Float.valueOf(str);
            switch (item.getItemId()){
                case R.id.md:
                    res /= rd;
                    f = "$";
                    break;
                case R.id.me:
                    f = "€";
                    res /= re;
                    break;
                case R.id.mw:
                    res *= rw;
                    f = "₩";
            }
            out.setText(String.format("%.2f", res) + f);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
        String url = "http://www.usd-cny.com/icbc.htm";
        Document doc;
        List<RateItem> list= new ArrayList<RateItem>();
        try {
            doc = Jsoup.connect(url).get();
            Element table=doc.getElementsByTag("table").first();
            Elements trs=table.getElementsByTag("tr");
            for(int i=1;i<trs.size();i++){
                Element td=trs.get(i);
                Elements tds=td.getElementsByTag("td");
                String name=tds.get(0).text();
                String rate=tds.get(1).text();
                if(rate.equals("--")){
                    rate=tds.get(2).text();
                }
                RateItem item=new RateItem();
                item.setCurName(name);
                item.setCurRate(rate);
                list.add(item);
            }
            RateManager rateManager=new RateManager(this);
            rateManager.deleteAll();
            rateManager.addAll(list);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SharedPreferences sp=getSharedPreferences("myrate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String todayStr=sdf.format(today);
        editor.putString("update_date",todayStr);
        editor.apply();
        Message msg=handler.obtainMessage(1);
        handler.sendMessage(msg);
    }

}
