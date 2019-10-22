package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;

public class Frag extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        ViewPager viewPager=findViewById(R.id.viewpager);
        MyPageAdapter pageAdapter=new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        XTabLayout xTabLayout=findViewById(R.id.sliding_tabs);
        xTabLayout.setupWithViewPager(viewPager);
    }
}
