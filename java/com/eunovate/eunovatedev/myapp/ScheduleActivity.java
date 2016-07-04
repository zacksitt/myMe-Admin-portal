package com.eunovate.eunovatedev.myapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class ScheduleActivity extends AppCompatActivity {

    private ListView stuList;
    ArrayList<Student> arrayOfWebData=new ArrayList<Student>();
    class Student{
        public String student_id;
        public  String name;
        public String date_of_birth;
        public String gender;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        new getData().execute();
    }

    FancyAdapter aa=null;
    private class getData extends AsyncTask<String,String,String> {

        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... arg0){
            try {
                String link="http://10.0.2.2/api/index.php/Login_ctrl/get_data";
                // String link="http://www.eunovate.com/api/login";
                String data=" ";

                URL url=new URL(link);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer sb=new StringBuffer();
                //StringBuilder result=new StringBuilder();
                String line=null;

                //Read Server Response
                while ((line=reader.readLine())!=null){
                    sb.append(line+"\n");
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
                JSONArray jArray=new JSONArray(result);
                for (int i=0;i<jArray.length();i++){
                    JSONObject json_data=jArray.getJSONObject(i);
                    Student studentRow=new Student();
                    studentRow.student_id=json_data.getString("student_id");
                    studentRow.name=json_data.getString("name");
                    studentRow.date_of_birth=json_data.getString("date_of_birth");
                    studentRow.gender=json_data.getString("gender");
                    arrayOfWebData.add(studentRow);
                }
            } catch (JSONException e) {
                Log.e("log_tag","Error parsing data"+e.toString());
            }

            ListView myListView=(ListView)findViewById(R.id.myListView);
            aa=new FancyAdapter();
            myListView.setAdapter(aa);

        }
    }

    class FancyAdapter extends ArrayAdapter<Student>{

        FancyAdapter(){
            super(ScheduleActivity.this,android.R.layout.activity_list_item,arrayOfWebData);
        }

        public View getView(int position, View convertView,ViewGroup parent){
            ViewHolder holder;

            if(convertView==null){
                LayoutInflater inflater=getLayoutInflater();
                convertView=inflater.inflate(R.layout.row,null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else
            {
                holder=(ViewHolder)convertView.getTag();
            }
            holder.populateFrom(arrayOfWebData.get(position));
            return (convertView);
        }
    }

    class ViewHolder{

        public TextView name=null;
        public TextView date_of_birth=null;
        public TextView gender=null;

        ViewHolder(View row){
            name=(TextView)row.findViewById(R.id.name);
            date_of_birth=(TextView)row.findViewById(R.id.date_of_birth);
            gender=(TextView)row.findViewById(R.id.gentder);
        }

        void populateFrom(Student stu){
            name.setText(stu.name);
            date_of_birth.setText(stu.date_of_birth);
            gender.setText(stu.gender);
        }
    }
}
