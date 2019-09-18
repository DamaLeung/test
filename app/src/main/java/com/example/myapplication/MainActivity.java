package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView out;
    EditText input;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        out=findViewById(R.id.output);
        input=findViewById(R.id.input);
        Button song=findViewById(R.id.song);
//        song.setOnClickListener(this);
        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str=input.getText().toString();
                out.setText(str);
            }
        });
    }

//    @Override
//    public void onClick(View view) {
//        String str=input.getText().toString();
//        out.setText(str);
//    }
}
