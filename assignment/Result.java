package com.example.sukhbeer.assignment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class Result extends ActionBarActivity {
    DBhelper dBhelper;
    Departments department;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        dBhelper = new DBhelper(this);
        SQLiteDatabase db = dBhelper.getReadableDatabase();

        Intent i = getIntent();
        //String query = i.getStringExtra(Activity_2.query);
        //String Answer = i.getStringExtra(Activity_2.Answer);
        department = (Departments)i.getSerializableExtra("departments");
        Cursor Amount = db.query(true,"Departments", new String[]{"amount"},"department =  \"" + department.Department + "\" AND commodity_family = \"" + department.Commodity_family + "\" AND commodity_group = \"" + department.Commodity_group + "\" AND commodity_cat = \"" + department.Commodity_cat + "\" AND commodity_sub = \"" + department.Commodity_subCat + "\" AND fiscal_year = \"" + department.Year + "\" AND quarter = \"" + department.Quarter + "\" AND period = " + "\"" + department.Period + "\"" ,null, null, null, null, null);
        Amount.moveToFirst();
        String Answer = "The amount For " + department.Department + " For "  + department.Commodity_family + " from " + department.Commodity_group + " Sub-Categorized as "+ department.Commodity_cat + " and " + department.Commodity_subCat + " from fiscal-year " + department.Year + " And Quater "+ department.Quarter + " and period of "+ department.Period + " is :" ;
        int rowID = Amount.getColumnIndex("amount");
        for(Amount.moveToFirst();!Amount.isAfterLast();Amount.moveToNext()){
            String data = Amount.getString(rowID);
            Log.d(Answer, data);
            Log.d("Data","entered" + data);
        }
        Amount.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
