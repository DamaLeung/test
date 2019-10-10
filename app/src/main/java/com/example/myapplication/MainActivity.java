package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    TextView out;
    EditText input;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temper);
        input=findViewById(R.id.e1);
        out=findViewById(R.id.e2);
        Button sub=findViewById(R.id.sub);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str=input.getText().toString();
                if(str.length()>0){
                    float f=Float.valueOf(str);
                    float res=(f-32)/1.8f;
                    DecimalFormat decimalFormat=new DecimalFormat(".00");
                    out.setText(decimalFormat.format(res));
                }
            }
        });
    }


}
