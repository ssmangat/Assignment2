package com.example.sukhbeer.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sukhbeer on 2015-12-01.
 */
public class DBhelper extends SQLiteOpenHelper {
    final static String DB_NAME = "departments.db";
    final static int DB_VERSION = 1;
    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS Departments;");
        db.execSQL("CREATE TABLE Departments(_id integer PRIMARY KEY AUTOINCREMENT ,department TEXT,commodity_family TEXT,commodity_group TEXT, commodity_cat TEXT , commodity_sub TEXT, fiscal_year TEXT, quarter TEXT, period TEXT, amount TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addToDatabase(Departments departments)
    {
        ContentValues values = new ContentValues();
        values.put("department", departments.Department);
        values.put("commodity_family", departments.Commodity_family);
        values.put("commodity_group", departments.Commodity_group);
        values.put("commodity_cat", departments.Commodity_cat);
        values.put("commodity_sub", departments.Commodity_subCat);
        values.put("fiscal_year", departments.Year);
        values.put("quarter", departments.Quarter);
        values.put("period", departments.Period);
        values.put("period", departments.Period);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("Departments", null, values);
        db.close();

    }
}
