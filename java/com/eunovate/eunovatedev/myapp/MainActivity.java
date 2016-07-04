package com.eunovate.eunovatedev.myapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.object.ScheduleObject;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

public class MainActivity extends Activity {

    private EditText usernameField,passwordField;
    private TextView loginStatus;
    private ProgressDialog progressDialog;
    public  static final String KEY_TOKEN = "token";
    private static final int PREFERENCE_MODE_PRIVATE=0;

    public static SimpleDateFormat date_fmt=new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat datetime_fmt=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static SimpleDateFormat time_fmt=new SimpleDateFormat("HH:mm a");

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    public int uid=0;
    public static ConnectivityManager cm;
    int PRIVATE_MODE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref=getSharedPreferences("loginPrefs", PREFERENCE_MODE_PRIVATE);
        String token=pref.getString("token", null);
        uid=pref.getInt("role", 0);
        int user_id=pref.getInt("user_id", 0);
        // Check User Ex.Teacher OR Driver

        cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(token!=null){
            if(uid==1) {
                Log.i("Log Tag", "Admin Role !");
                //User Mode Admin:1,Driver:2
                Intent actChange = new Intent(MainActivity.this, HomeActivity.class);
                finish();
                startActivity(actChange);

            }else if(uid==4){
                Log.i("Log Tag", "Teacher Role !");
                //User Mode Admin:1,Driver:2
                Intent actChange = new Intent(MainActivity.this, HomeActivity.class);
                finish();
                startActivity(actChange);
            }
            else if(uid==5){
                Log.i("Log Tag", "Driver Role !");
                Intent actChange = new Intent(MainActivity.this, DriverHome_Activity.class);
                finish();
                startActivity(actChange);

            }else{
                setContentView(R.layout.activity_main);
                usernameField=(EditText)findViewById(R.id.editText1);
                passwordField=(EditText)findViewById(R.id.editText2);
                loginStatus=(TextView)findViewById(R.id.textView4);
            }
            }else{
                setContentView(R.layout.activity_main);
                //Get textview id from view
                usernameField=(EditText)findViewById(R.id.editText1);
                passwordField=(EditText)findViewById(R.id.editText2);
                loginStatus=(TextView)findViewById(R.id.textView4);
               // loginStatus.setText("Invalid username or password. Please, try again");
        }
    }

    public static boolean isOnline() {

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
           //return true;
        }

    }
    @Override
    //Chreate App Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // When user tab on login button
    public  void login(View view){

        if(isOnline()) {
            if (usernameField.getText().toString().equals("") || passwordField.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "Type username and password!", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading..");
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                new Login_user().execute(username, password);
            }
        }else{
                Toast.makeText(MainActivity.this, "No internet connection !", Toast.LENGTH_SHORT).show();
          }
       // progressDialog.dismiss();
    }

    // Background job connecting to server
    private class Login_user extends AsyncTask<String,Void,String> {

        //flag 0 means get and 1 means post.(By default it is get.);
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... arg0){

            try {

                String username=(String)arg0[0];
                String password=(String)arg0[1];

                String link= HomeActivity.BASE_URL+"/login";
                // Parameter
                String data= URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
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
        //Do when background job finished.
        protected void onPostExecute(String result){
            Log.i("LOGTAG","Log Result " +result);
            try{
            JSONObject jStr=new JSONObject(result);
            String res=jStr.getString("result");
            if(res.equals("success")){

                String token=jStr.getString("token");
                int rid = Integer.parseInt(jStr.getString("urole"));
                int user_id=Integer.parseInt(jStr.getString("user_id"));

                    pref=getSharedPreferences("loginPrefs",PREFERENCE_MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("token", token);
                    editor.putInt("role", rid);
                    editor.putInt("user_id",user_id);
                   editor.commit();
                    //Change activity(Go to another page!)
                    if(rid==4) {
                        //Log.i("Log Tag","Teacher Role !");
                        //User Mode Admin:1,Driver:2
                        progressDialog.dismiss();
                        Intent actChange = new Intent(MainActivity.this, HomeActivity.class);
                        finish();
                        startActivity(actChange);
                    }else if(rid==5){
                        //Log.i("Log Tag","Driver Role !");
                        //Intent actChange = new Intent(MainActivity.this, AddVehicleUsage_Activity.class);
                        progressDialog.dismiss();
                        Intent actChange = new Intent(MainActivity.this, DriverHome_Activity.class);
                        finish();
                        startActivity(actChange);
                    }else{
                        progressDialog.dismiss();
                        loginStatus.setText(jStr.getString("msg"));
                    }
                }else{
                    progressDialog.dismiss();
                    loginStatus.setText("Invalid Username or Password");
                }
                //finish();
            }catch (JSONException e){
                e.printStackTrace();
            }


        }
    }
}
