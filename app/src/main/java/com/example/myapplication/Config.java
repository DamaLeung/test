package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.SharedPreferencesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class Config extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
    }

    @Override
    public void onClick(View view) {
        TextView rateD=findViewById(R.id.rateDollar);
        String sd=rateD.getText().toString();
        TextView rateE=findViewById(R.id.rateEuro);
        String se=rateE.getText().toString();
        TextView rateW=findViewById(R.id.rateWon);
        String sw=rateW.getText().toString();
        float d;
        float e;
        float w;
        Intent intent =getIntent();
        Bundle bun=new Bundle();
        SharedPreferences sp=getSharedPreferences("myrate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(sd.length()>0){
            d=Float.valueOf(sd);
            bun.putFloat("rateD",d);
            editor.putFloat("rateD",d);
        }
        if(se.length()>0){
            e=Float.valueOf(se);
            bun.putFloat("rateE",e);
            editor.putFloat("rateE",e);

        }
        if(sw.length()>0){
            w=Float.valueOf(sw);
            bun.putFloat("rateW",w);
            editor.putFloat("rateW",w);

        }
        intent.putExtras(bun);
        editor.apply();
        setResult(2,intent);
        finish();

    }
}
