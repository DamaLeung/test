package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Point extends AppCompatActivity implements View.OnClickListener{
    TextView out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
    }

    @Override
    public void onClick(View view) {
        out=findViewById(R.id.point);
        String str= out.getText().toString();
        if(str.length()>0){
            int res=Integer.valueOf(str);
            switch(view.getId()){
                case R.id.one:
                    res++;
                    break;
                case R.id.two:
                    res+=2;
                    break;
                case R.id.three:
                    res+=3;
                    break;

            }
            out.setText(String.format("%.2f",res));
        }
    }
}
