package com.eunovate.eunovatedev.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.eunovate.eunovatedev.myapp.dao_class.DataBaseHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Schedule_DBHelper;
import com.eunovate.eunovatedev.myapp.object.ScheduleObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab1 extends Fragment {

    private ListView scheduleList;
    private ProgressDialog progressDialog;
    private Schedule_DBHelper schedule_dbHelper;
    private DataBaseHelper dbHelper;
    View v;
    private Button weekBtn,monthBtn,dayBtn;
    SimpleDateFormat date_fmt;
    int position;
    ArrayList<ScheduleObject> arrayOfWebData=new ArrayList<ScheduleObject>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.tab_1, container, false);

        boolean fromApi=false;
        dbHelper=new DataBaseHelper(getActivity());
        schedule_dbHelper=new Schedule_DBHelper(getActivity());

        weekBtn = (Button) v.findViewById(R.id.week); // you have to use rootview object..
        dayBtn = (Button) v.findViewById(R.id.today);
        monthBtn = (Button) v.findViewById(R.id.month);

        dayBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dayBtn.setBackgroundColor(getResources().getColor(R.color.lightblue));
                weekBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                monthBtn.setBackgroundColor(getResources().getColor(R.color.lightblue));

                arrayOfWebData = schedule_dbHelper.get_schedule_list("week");
                set_list_data(arrayOfWebData);
            }
        });

        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                weekBtn.setBackgroundColor(getResources().getColor(R.color.lightblue));
                dayBtn.setBackgroundColor(getResources().getColor(R.color.lightblue));
                monthBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

                arrayOfWebData = schedule_dbHelper.get_schedule_list("month");
                set_list_data(arrayOfWebData);
            }
        });
        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                weekBtn.setBackgroundColor(getResources().getColor(R.color.lightblue));
                dayBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
                monthBtn.setBackgroundColor(getResources().getColor(R.color.lightblue));

                arrayOfWebData = schedule_dbHelper.get_schedule_list("all");
                set_list_data(arrayOfWebData);
            }
        });


        if(fromApi) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading..");
            new getData().execute();
        }else {
            arrayOfWebData = schedule_dbHelper.get_schedule_list("all");
            //arrayOfWebData = schedule_dbHelper.get_schedule_list("today");
            Log.i("LOG TAG","DAILY SCHEDULE" + arrayOfWebData);
            set_list_data(arrayOfWebData);

        }

        Log.e("LOG_TAG","Schedule List : "+arrayOfWebData);
        return v;
    }

    public void set_list_data(ArrayList<ScheduleObject> data){
        scheduleList=(ListView)v.findViewById(R.id.scheList);
        aa=new FancyAdapter();
        scheduleList.setAdapter(aa);
        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
                String selected=((TextView)arg1.findViewById(R.id.schedule_id)).getText().toString();

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("param", Integer.parseInt(selected));
                Intent intent = new Intent(getActivity().getApplicationContext(), ScheduleDisplay.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    public void get_by_week(View v){
        arrayOfWebData = schedule_dbHelper.get_schedule_list("week");
        Log.i("LOG TAG","WEEKLY SCHEDULE"+arrayOfWebData);
        set_list_data(arrayOfWebData);
    }

    public void get_by_month(View v){
        arrayOfWebData = schedule_dbHelper.get_schedule_list("month");
        Log.i("LOG TAG","MONTHLY SCHEDULE"+arrayOfWebData);
        set_list_data(arrayOfWebData);
    }

    FancyAdapter aa=null;
    private class getData extends AsyncTask<String,String,String> {

        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... arg0){
            try {
                String link="http://10.0.2.2/ams/service/index.php/Schedule_output_ctrl/getschedulelst";
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
            date_fmt = new SimpleDateFormat("MM/dd/yyyy");
            try{
                JSONArray jArray=new JSONArray(result);
                for (int i=0;i<jArray.length();i++){
                    JSONObject json_data=jArray.getJSONObject(i);
                    ScheduleObject scheduleRow=new ScheduleObject();
                    scheduleRow.setSchedule_id(json_data.getInt("schedule_id"));
                    scheduleRow.setClassname(json_data.getString("classname"));
                    scheduleRow.setCourse(json_data.getString("course"));
                    scheduleRow.setLesson(json_data.getString("lesson"));
                    String date=json_data.getString("date");
                    try {
                        scheduleRow.setDate(date_fmt.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    arrayOfWebData.add(scheduleRow);
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data" + e.toString());
            }

            scheduleList=(ListView)v.findViewById(R.id.scheList);
            aa=new FancyAdapter();
            scheduleList.setAdapter(aa);
            progressDialog.dismiss();
            scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
                    //TODO Auto-generated method stub
                    int id_To_Search = arg2 + 1;

                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("id", id_To_Search);

                    Intent intent = new Intent(getActivity().getApplicationContext(), ScheduleDisplay.class);

                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });
        }
    }

    class FancyAdapter extends ArrayAdapter<ScheduleObject> {

        FancyAdapter(){
            super(getActivity(),android.R.layout.activity_list_item,arrayOfWebData);
        }

        public View getView(int position, View convertView,ViewGroup parent){
            ViewHolder holder;

            if(convertView==null){
                LayoutInflater inflater=getActivity().getLayoutInflater();
                convertView=inflater.inflate(R.layout.schedule_row,null);
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

        SimpleDateFormat date_fmt = new SimpleDateFormat("dd/MM/yyyy");
        public TextView classname=null;
        public TextView lesson=null;
        public TextView date=null;
        public TextView course=null;
        public TextView schedule_id=null;

        ViewHolder(View row){
            classname=(TextView)row.findViewById(R.id.cname);
            lesson=(TextView)row.findViewById(R.id.lesson);
            course=(TextView)row.findViewById(R.id.course);
            date=(TextView)row.findViewById(R.id.date);
            schedule_id=(TextView)row.findViewById(R.id.schedule_id);
        }

        void populateFrom(ScheduleObject stu){
            classname.setText(stu.getClassname());
            course.setText(stu.getCourse());
            lesson.setText(stu.getLesson());
            date.setText(date_fmt.format(stu.getDate()).toString());
            schedule_id.setText(String.valueOf(stu.getSchedule_id()));
        }
    }
}