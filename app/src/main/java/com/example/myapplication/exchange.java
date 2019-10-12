package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class exchange extends AppCompatActivity implements View.OnClickListener,Runnable {
    Float rd;
    Float re;
    Float rw;
    Handler handler;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    Bundle b = msg.getData();
                    rd=b.getFloat("rd");
                    re=b.getFloat("re");
                    rw=b.getFloat("rw");

                }
                super.handleMessage(msg);

            }
        };
        String updateDate=sp.getString("update_date","");
        rd=sp.getFloat("rateD",7.1224f);
        re=sp.getFloat("rateE",7.7883f);
        rw=sp.getFloat("rateW",168.2719f);
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String todayStr=sdf.format(today);
        if(!todayStr.equals(updateDate)){
            Thread t=new Thread(this);
            t.start();
        }
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
        //获取网络数据
        String url = "http://www.usd-cny.com/icbc.htm";
        Document doc;
        Bundle b=new Bundle();
        Float nd=0f;
        Float ne=0f;
        Float nw=0f;
        try {
            doc = Jsoup.connect(url).get();
            Element table=doc.getElementsByTag("table").first();
            Elements trs=table.getElementsByTag("tr");
            Element d=trs.get(3);//美元
            nd=Float.valueOf(d.getElementsByTag("td").get(1).text())/100;
            b.putFloat("rd",nd);
            Element e=trs.get(14);//欧元
            ne=Float.valueOf(e.getElementsByTag("td").get(1).text())/100;
            b.putFloat("re",ne);
            Element w=trs.get(21);//韩元
            nw=100/Float.valueOf(w.getElementsByTag("td").get(2).text());
            b.putFloat("rw",nw);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SharedPreferences sp=getSharedPreferences("myrate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String todayStr=sdf.format(today);
        editor.putFloat("rateD",nd);
        editor.putFloat("rateE",ne);
        editor.putFloat("rateW",nw);
        editor.putString("update_date",todayStr);
        editor.apply();
        Message msg=handler.obtainMessage(1);
        msg.setData(b);
        handler.sendMessage(msg);
    }
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
