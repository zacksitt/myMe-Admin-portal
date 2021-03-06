package com.eunovate.eunovatedev.myapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**myo
 * Created by EunovateDev on 1/2/2016.
 */
public class SigninActivity extends AsyncTask<String,Void,String>{

    //flag 0 means get and 1 means post.(By default it is get.);
    public SigninActivity(Context context){

    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0){

            try {
                String username=(String)arg0[0];
                String password=(String)arg0[1];

                String link="http://10.0.2.2/testAPI/api/index.php/Login";
               // String link="http://www.eunovate.com/api/login";
                String data=URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                URL url=new URL(link);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer sb=new StringBuffer();
                String line=null;

                //Read Server Response
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                    break;
                }
                return sb.toString();
            }
            catch (Exception e){
                return new String("Exception: "+e.getMessage());
            }

    }

    @Override
    protected void onPostExecute(String result){
        try{
            JSONObject jStr=new JSONObject(result);


        }catch (JSONException e){
            e.printStackTrace();
        }


    }
}
