package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RateManager {
    private  DBHelper dbHelper;
    private  String TBName;
    public  RateManager(Context context){
        dbHelper=new DBHelper(context);
        TBName=DBHelper.TB_Name;
    }
    public void add(RateItem item){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("curname",item.getCurName());
        values.put("currate",item.getCurRate());
        db.insert(TBName,null,values);
        db.close();
    }
    public void addAll(List<RateItem> list){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        for(RateItem item:list){
            ContentValues values=new ContentValues();
            values.put("curname",item.getCurName());
            values.put("currate",item.getCurRate());
            db.insert(TBName,null,values);
        }
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(TBName,null,null);
        db.close();
    }
    public void delete(RateItem item){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(TBName,"ID=",new String[]{String.valueOf(item.getId())});
    }
    public void update(RateItem item){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("curname",item.getCurName());
        values.put("currate",item.getCurRate());
        db.update(TBName,values,"ID=?",new String[]{String.valueOf(item.getId())});
        db.close();
    }
    public List<RateItem> listAll(){
        List<RateItem> rateList =null;
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TBName,null,null,null,null,null,null);
        if(cursor!=null){
            rateList=new ArrayList<RateItem>();
            while(cursor.moveToNext()){
                RateItem item=new RateItem();
                item.setId(cursor.getColumnIndex("ID"));
                item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
                item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
                rateList.add(item);
            }
        }
        cursor.close();
        return  rateList;
    }
    public RateItem findById(int id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TBName,null,"ID=?",new String[]{String.valueOf(id)},null,null,null);
        RateItem item=null;
        if(cursor!=null&&cursor.moveToFirst()){
            item=new RateItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
            item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
            cursor.close();
        }
        db.close();
        return item;
    }
}
