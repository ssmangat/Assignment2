package com.example.sukhbeer.assignment2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class MainActivity extends ActionBarActivity {
    private String url = null;
    private ProgressDialog pBar; //progress bar for downloading
    public static final int progress = 0;
    private ProgressDialog pBar2; //progress bar for unzipping
    public static final int progress2 = 1;
    private ProgressDialog pBar3; //progress bar for inserting data to database
    public static final int progress3 = 2;
    private Spinner spinner;
    private String spin_val;
    private String[] years = { "select a year","2011","2012","2013","2014" };
    public DBhelper dBhelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.deleteDatabase(Environment.getRootDirectory() + File.separator + "data/data/com.example.sukhbeer.assignment2/databases/departments.db");
        dBhelper = new DBhelper(this);
        db = dBhelper.getWritableDatabase();
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,years);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_val = years[position];

                switch (spin_val) {
                    case "2011":
                        url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY10-11.zip";
                        //filename = "tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY10-11/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY10-11.csv";
                        new DownloadFile().execute(url);//calls the download function
                        break;
                    case "2012":
                        url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY11-12.zip";
                        //filename = "tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY11-12/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY11-12.csv";
                        new DownloadFile().execute(url);//calls the download function
                        break;
                    case "2013":
                        url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY12-13.zip";
                        //filename = "tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY12-13/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY12-13.csv";
                        new DownloadFile().execute(url);//calls the download function
                        break;
                    case "2014":
                        url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY13-14.zip";
                        //filename = "tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY13-14/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY13-14.csv";
                        new DownloadFile().execute(url);//calls the download function
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        dBhelper = new DBhelper(this);

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress: // we set this to 0
                pBar = new ProgressDialog(this);
                pBar.setMessage("Downloading file. Please wait...");
                pBar.setIndeterminate(false);
                pBar.setMax(100);
                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pBar.setCancelable(true);
                pBar.show();
                return pBar;
            case progress2: // we set this to 0
                pBar2 = new ProgressDialog(this);
                pBar2.setMessage("unzipping file. Please wait...");
                pBar2.setIndeterminate(false);
                pBar2.setMax(100);
                pBar2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pBar2.setCancelable(true);
                pBar2.show();
                return pBar2;
            case progress3: // we set this to 0
                pBar3 = new ProgressDialog(this);
                pBar3.setMessage("Reading downloaded file. Please wait...");
                pBar3.setIndeterminate(false);
                pBar3.setMax(100);
                pBar3.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pBar3.setCancelable(true);
                pBar3.show();
                return pBar3;
            default:
                return null;
        }
    }

    public class DownloadFile extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showDialog(progress);
        }


        @Override
        protected String doInBackground(String... params) {
            int count;
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileSize = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(),10000);
                File data1 = new File(Environment.getExternalStorageDirectory() + "/App_data.zip");

                OutputStream out = new FileOutputStream(data1);

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1){
                    total+= count;
                    publishProgress(""+(int)((total*100)/fileSize));

                    out.write(data,0,count);
                }
                out.flush();

                out.close();

            } catch (java.io.IOException e) {
                Log.e("Error::", e.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(String... params) {
            // setting progress percentage
            pBar.setProgress(Integer.parseInt(params[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress);
             new unzip().execute();

        }
    }

    private class unzip extends AsyncTask<String,String,String>{
        InputStream is;

        ZipInputStream zis;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showDialog(progress2);
        }



        @Override
        protected String doInBackground(String... params) {
            try{
                is = new FileInputStream(Environment.getExternalStorageDirectory()+"/App_data.zip");
                zis = new ZipInputStream(new BufferedInputStream(is));
                ZipEntry ze;
                while ((ze = zis.getNextEntry())!= null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[12000];
                    int count1;
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "/unzipped.csv");
                    int fileSize = (int) ze.getSize();
                    OutputStream fileOutputStream = new FileOutputStream(file);
                    long total = 0;
                    while ((count1 = zis.read(buffer))!= -1){
                        baos.write(buffer,0,count1);
                        total += count1;
                        publishProgress("" + (int) ((total *100)/fileSize));
                        byte[] bytes = baos.toByteArray();
                        fileOutputStream.write(bytes);
                        baos.reset();
                    }
                    fileOutputStream.close();
                    zis.closeEntry();

                }
                zis.close();
            } catch (IOException e){
                Log.e("Decompress", "Unzip", e);
            }
            return null;
        }
        protected void onProgressUpdate(String... params) {
            // setting progress percentage
            pBar2.setProgress(Integer.parseInt(params[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress2);
            new readCSV().execute();
        }
    }

    private class readCSV extends AsyncTask<String,String,String>{
        File fr = null;
        FileReader fileReader = null;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showDialog(progress3);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ReadCsv","REading");
            try{
                fr = new File(Environment.getExternalStorageDirectory() + File.separator + "unzipped.csv");
                fileReader = new FileReader(fr);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d("ReadCsv", "file detected");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String data = "";
            long count = 0;
            try{
                Log.d("ReadCsv","started reading");
                String garbage = bufferedReader.readLine();
                while ((data = bufferedReader.readLine()) != null){
                    String[] stringArray = data.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    Departments departments = new Departments(stringArray[0],stringArray[1],stringArray[2],stringArray[3],stringArray[4],stringArray[5],stringArray[6],stringArray[7],stringArray[16]);
                    dBhelper.addToDatabase(departments);
                    count = count+1;
                    publishProgress("" + (int) ((count * 100) / 80000));

                }
                Log.d("ReadCsv", "Done Entering data");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(String... params) {
            // setting progress percentage
            pBar3.setProgress(Integer.parseInt(params[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress3);
            // unzip();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
