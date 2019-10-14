package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ListClick extends AppCompatActivity  {
    TextView out;
    Float rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_click);
        Bundle bundle = this.getIntent().getExtras();
        String type=bundle.getString("type");
        rate=bundle.getFloat("rate");
        TextView title=findViewById(R.id.tar);
        title.setText(type);
        out=findViewById(R.id.listInput);
        out.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                TextView show=findViewById(R.id.show);
                if(editable.length()>0){
                    float val=Float.parseFloat(editable.toString());
                    show.setText(val+"RMB==>"+String.format("%.2f",100/rate*val));
                }else{
                    show.setText("consequence");
                }
            }
        });
    }


}
