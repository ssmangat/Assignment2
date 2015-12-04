package com.example.sukhbeer.assignment2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class MainActivity extends ActionBarActivity {
    private String url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY10-11.zip";
    private ProgressDialog pBar; //progress bar for downloading
    public static final int progress = 0;
    private ProgressDialog pBar2; //progress bar for unzipping
    public static final int progress2 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DownloadFile().execute(url);
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
            //new readCSV().execute();
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
