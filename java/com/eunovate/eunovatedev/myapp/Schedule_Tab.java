package com.eunovate.eunovatedev.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceInfoDBHelper;
import com.eunovate.eunovatedev.myapp.dao_class.DataBaseHelper;
import com.eunovate.eunovatedev.myapp.dao_class.Schedule_DBHelper;
import com.eunovate.eunovatedev.myapp.object.ScheduleObject;
import com.eunovate.eunovatedev.myapp.object.notiObject;

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
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class Schedule_Tab extends Fragment {

    private ListView scheduleList;
    private ProgressDialog progressDialog;
    private Schedule_DBHelper schedule_dbHelper;
    private DataBaseHelper dbHelper;
    View v;
    private Button weekBtn,monthBtn,dayBtn;
    SimpleDateFormat date_fmt;
    ArrayList<ScheduleObject> arrayOfWebData=new ArrayList<ScheduleObject>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.activity_schedule__tab, container, false);

        boolean fromApi=false;
        dbHelper=new DataBaseHelper(getActivity());
        schedule_dbHelper=new Schedule_DBHelper(getActivity());

        weekBtn = (Button) v.findViewById(R.id.div_week); // you have to use rootview object..
        dayBtn = (Button) v.findViewById(R.id.div_today);
        monthBtn = (Button) v.findViewById(R.id.div_month);

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

            arrayOfWebData = schedule_dbHelper.get_schedule_list("all");
           // arrayOfWebData = schedule_dbHelper.get_schedule_list("today");
            set_list_data(arrayOfWebData);


        Log.e("LOG_TAG", "Schedule List : " + arrayOfWebData);
        return v;
    }

    public void set_list_data(ArrayList<ScheduleObject> data){
        scheduleList=(ListView)v.findViewById(R.id.div_scheList);
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
        set_list_data(arrayOfWebData);
    }

    public void get_by_month(View v){
        arrayOfWebData = schedule_dbHelper.get_schedule_list("month");
        set_list_data(arrayOfWebData);
    }

    FancyAdapter aa=null;
    class FancyAdapter extends ArrayAdapter<ScheduleObject> {

        FancyAdapter(){
            super(getActivity(),android.R.layout.activity_list_item,arrayOfWebData);
        }

        public View getView(int position, View convertView,ViewGroup parent){
            ViewHolder holder;

            if(convertView==null){
                LayoutInflater inflater=getActivity().getLayoutInflater();
                convertView=inflater.inflate(R.layout.schedule_row_div,null);
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

        SimpleDateFormat date_fmt = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat time_fmt = new SimpleDateFormat("HH:mm:ss");
        public TextView classname=null;
        public TextView location=null;
        public TextView date=null;
        public TextView vehicle=null;
//        public TextView stime=null;
//        public TextView etime=null;
        public TextView schedule_id=null;

        ViewHolder(View row){
//            stime=(TextView)row.findViewById(R.id.stime);
//            etime=(TextView)row.findViewById(R.id.etime);
            classname=(TextView)row.findViewById(R.id.cname);
            location=(TextView)row.findViewById(R.id.location);
            vehicle=(TextView)row.findViewById(R.id.bus);
            date=(TextView)row.findViewById(R.id.date);
            schedule_id=(TextView)row.findViewById(R.id.schedule_id);
        }

        void populateFrom(ScheduleObject stu){
//            stime.setText(time_fmt.format(stu.getStart_time()).toString());
//            etime.setText(time_fmt.format(stu.getEnd_time()).toString());
            classname.setText(stu.getClassname());
            location.setText(stu.getLocation());
            vehicle.setText(stu.getVehicle());
            date.setText(date_fmt.format(stu.getDate()).toString());
            schedule_id.setText(String.valueOf(stu.getSchedule_id()));
        }
    }
}
