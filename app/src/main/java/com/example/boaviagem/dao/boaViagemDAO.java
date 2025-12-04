package com.example.boaviagem.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.boaviagem.DatabaseHelper;

public class boaViagemDAO {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public boaViagemDAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDb(){
        if (db == null){
            db = helper.getWritableDatabase();
        }
        return db;
    }
    public void close(){
        helper.close();
    }

}
