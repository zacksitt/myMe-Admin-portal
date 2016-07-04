package com.eunovate.eunovatedev.myapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.object.Attendance_Record_Object;
import com.eunovate.eunovatedev.myapp.object.BehaviourObj;
import com.eunovate.eunovatedev.myapp.object.StudentObject;

import java.util.ArrayList;

public class AttendanceActivity extends ListActivity{

    private ArrayList<BehaviourObj> bvr_list;
    private ArrayList<StudentObject> stu_list;
    private AttendanceDBHelper atdHelper;
    public BehaviourObj bvrObj;
    public ArrayList<BehaviourObj> bvrUpdateList;
    private boolean firstLoad=false;
    private int param_class_id,param_schedule_id,stu_id,stu_count=0,atd_id;
    private TextView student;
    private RelativeLayout atdLayout,optionLayout,absentLayout;
    private StudentObject stuObj;
    private Button cmt_btn;
    private ImageButton quitBtn;
    private EditText cmt_edittxt;
    private  ArrayList<RowModel> list;
    private ArrayList<Attendance_Record_Object> atdArr;
    private Attendance_Record_Object atd_rcd_obj;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        atdArr=new ArrayList<>();


        student=(TextView) findViewById(R.id.student);
        atdLayout=(RelativeLayout) findViewById(R.id.atdLayout);
        optionLayout=(RelativeLayout) findViewById(R.id.optionLayout);
        absentLayout=(RelativeLayout) findViewById(R.id.absentLayout);
        cmt_btn=(Button) findViewById(R.id.comment_btn);
        cmt_edittxt=(EditText) findViewById(R.id.comment);
        quitBtn=(ImageButton) findViewById(R.id.closeicon);

        atdLayout.setVisibility(View.INVISIBLE);
        absentLayout.setVisibility(View.INVISIBLE);

        list=new ArrayList<RowModel>();
        bvrUpdateList=new ArrayList<BehaviourObj>();
        atdHelper=new AttendanceDBHelper(this);
        Bundle extras=getIntent().getExtras();
        param_class_id=extras.getInt("class_id");
        param_schedule_id=extras.getInt("schedule_id");

        //Get student list from db
        stu_list=atdHelper.get_stu_list(param_class_id,5);
        //Check student exits
        if(stu_list.size()>0){
            bvr_list=new ArrayList<BehaviourObj>();
            bvr_list=atdHelper.get_bvr_list();
            stuObj=new StudentObject();
            stuObj=stu_list.get(stu_count);
            student.setText(stuObj.getName());
            stu_id=stuObj.getStudent_id();
        }else{
            student.setText("No Student !");
            Button preBtn=(Button) findViewById(R.id.present);
            Button absBtn=(Button) findViewById(R.id.absent);

            preBtn.setVisibility(View.INVISIBLE);
            absBtn.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void close(View v){
        finish();
    }
    // When User choose present at attendance
    public void present(View v){
        atd_rcd_obj=new Attendance_Record_Object();
        quitBtn.setVisibility(View.INVISIBLE);
        atd_rcd_obj.setStudent_id(stu_id);
        atd_rcd_obj.setSchedule_id(param_schedule_id);
        atd_rcd_obj.setComment("");
        atdArr.add(atd_rcd_obj);

        //atd_id=atdHelper.insertAttendance(stu_id,param_schedule_id," ");
        if(atd_id>0){
            Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
            optionLayout.setVisibility(View.INVISIBLE);
            atdLayout.setVisibility(View.VISIBLE);
            absentLayout.setVisibility(View.INVISIBLE);

            ArrayList<BehaviourObj> bvr_list=new ArrayList<BehaviourObj>();
            ArrayList<RowModel> list=new ArrayList<RowModel>();
            bvr_list=atdHelper.get_bvr_list();
            for (BehaviourObj s : bvr_list) {
                list.add(new RowModel(s.getBehaviour()));
            }
            setListAdapter(new RatingAdapter(list));
        }else{
            Toast.makeText(getApplicationContext(),"not Inserted",Toast.LENGTH_SHORT).show();
        }
    }

    public void submit(View view){
        String cmt_desc=cmt_edittxt.getText().toString();
        quitBtn.setVisibility(View.INVISIBLE);
        atd_rcd_obj.setStudent_id(stu_id);
        atd_rcd_obj.setSchedule_id(param_schedule_id);
        atd_rcd_obj.setComment(cmt_desc);
        //atdArr.add(atd_rcd_obj);
//        atd_id=atdHelper.insertAttendance(stu_id,param_schedule_id,cmt_desc);
        if(atd_id>0){
            Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
            }else{
            Toast.makeText(getApplicationContext(),"not Inserted",Toast.LENGTH_SHORT).show();
        }

        stu_count++;
        if(stu_count < stu_list.size()) {
            stuObj = stu_list.get(stu_count);
            student.setText(stuObj.getName());
            stu_id = stuObj.getStudent_id();
            optionLayout.setVisibility(View.VISIBLE);
            atdLayout.setVisibility(View.INVISIBLE);
            absentLayout.setVisibility(View.INVISIBLE);
        }else{
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("schedule_id", param_schedule_id);
            Intent actChange = new Intent(AttendanceActivity.this, AttendanceInfo.class);
            actChange.putExtras(dataBundle);
            startActivity(actChange);
        }
    }
    //When User choose absent button at atendance.
    public void absent(View v){
        atd_rcd_obj=new Attendance_Record_Object();
        quitBtn.setVisibility(View.INVISIBLE);
        final ArrayList<String> cmtList = atdHelper.get_cmt_list();
        final ListView cmt_list_view=(ListView) findViewById(R.id.commantList);
        cmt_list_view.setVisibility(View.VISIBLE);
        optionLayout.setVisibility(View.INVISIBLE);
        atdLayout.setVisibility(View.INVISIBLE);
        absentLayout.setVisibility(View.VISIBLE);
        cmt_edittxt.setVisibility(View.INVISIBLE);
        cmt_btn.setVisibility(View.INVISIBLE);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,cmtList);
        cmt_list_view.setAdapter(arrayAdapter);
        cmt_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
                //TODO Auto-generated method stub
                cmt_list_view.setVisibility(View.INVISIBLE);
                cmt_edittxt.setVisibility(View.VISIBLE);
                cmt_btn.setVisibility(View.VISIBLE);
                cmt_edittxt.setText(cmtList.get(arg2));
               // submit();
            }
        });
    }

    //When User tab on next button
    public void next(View v){
        //Insert all behaviour of students to db
        atd_rcd_obj.setBvr_list(bvr_list);
        atdArr.add(atd_rcd_obj);
        Log.i("LogTag","Attendance Array"+atdArr);

//        for (BehaviourObj obj : bvr_list) {
//            if (atdHelper.insertAttendanceRcd(obj,atd_id)) {
//                Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(getApplicationContext(), "not Inserted", Toast.LENGTH_SHORT).show();
//            }
//        }

        stu_count++;
        if(stu_count < stu_list.size()) {

            stuObj = stu_list.get(stu_count);
            student.setText(stuObj.getName());
            stu_id = stuObj.getStudent_id();
            optionLayout.setVisibility(View.VISIBLE);
            atdLayout.setVisibility(View.INVISIBLE);
            absentLayout.setVisibility(View.INVISIBLE);

        }else{
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("schedule_id", param_schedule_id);
            Intent actChange = new Intent(AttendanceActivity.this, AttendanceInfo.class);
            actChange.putExtras(dataBundle);
            startActivity(actChange);
        }
    }

    private RowModel getModel(int position) {
        return(((RatingAdapter)getListAdapter()).getItem(position));
    }

    //Rating Adapter to show rating by list
    class RatingAdapter extends ArrayAdapter<RowModel> {
        RatingAdapter(ArrayList<RowModel> list) {
            super(AttendanceActivity.this, R.layout.behaviour_row, list);
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            View row=convertView;
            ViewWrapper wrapper;
            RatingBar rate;

            if (row==null) {
                LayoutInflater inflater=getLayoutInflater();

                row=inflater.inflate(R.layout.behaviour_row, parent, false);
                wrapper=new ViewWrapper(row);
                row.setTag(wrapper);
                rate=wrapper.getRatingBar();

                RatingBar.OnRatingBarChangeListener l=
                        new RatingBar.OnRatingBarChangeListener() {
                            public void onRatingChanged(RatingBar ratingBar,
                                                        float rating,
                                                        boolean fromTouch)  {
                                Integer myPosition=(Integer)ratingBar.getTag();

                                RowModel model=getModel(myPosition);
                                model.rating=Math.round(rating);
                                    if(fromTouch) {
                                        bvrObj = new BehaviourObj();
                                        bvrObj=bvr_list.get(myPosition);
                                        bvrObj.setBehaviour(model.toString());
                                        bvrObj.setRating(Math.round(model.rating));
                                        bvr_list.set(myPosition, bvrObj);

                                    }
                            }
                        };

                rate.setOnRatingBarChangeListener(l);
            }
            else {
                wrapper=(ViewWrapper)row.getTag();
                rate=wrapper.getRatingBar();
            }

            RowModel model=getModel(position);
            wrapper.getLabel().setText(model.toString());
            rate.setTag(new Integer(position));
            rate.setRating(model.rating);
            return(row);
        }
    }

    class RowModel {
        String label;
        int rating=2;


        RowModel(String label) {
            this.label=label;
        }

        public String toString() {
           return label;
        }
    }

    class ViewWrapper {
        View base;
        RatingBar rate=null;
        TextView label=null;

        ViewWrapper(View base) {
            this.base=base;
        }

        RatingBar getRatingBar() {
            if (rate==null) {
//                rate=(RatingBar)base.findViewById(R.id.ratingBar1);
            }

            return(rate);
        }

        TextView getLabel() {
            if (label==null) {
//                label=(TextView)base.findViewById(R.id.label);
            }
            return(label);
        }
    }
}


