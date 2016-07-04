package com.eunovate.eunovatedev.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.object.Attendance_Record_Object;
import com.eunovate.eunovatedev.myapp.object.BehaviourObj;
import com.eunovate.eunovatedev.myapp.object.StudentObject;
import com.eunovate.eunovatedev.myapp.object.VehicleUsageObject;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecord_Activity extends AppCompatActivity{

    private Toolbar myToolbar;
    private ListView atdStu_ListView,bvr_lsitview,cmt_listview;
    private AttendanceDBHelper atd_db;
    private ArrayList<StudentObject> stuList;
    private ArrayList<BehaviourObj> bvr_list;
    private ArrayList<Attendance_Record_Object> atd_rcd_list;
    private int param_class_id,param_schedule_id,param_course_id;
    FancyAdapter aa;
    private Attendance_Record_Object atd_rcd_obj;
    private int current_stu_location;
    private Spinner ratingSpinner;
    private PopupWindow popupWindow;
    private EditText comment;
    private Bundle dataBundle;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_record);
        myToolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_back_btn);
        //catch event when user click back button from toolbar
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //onBackPressed();
            }
        });
        atdStu_ListView=(ListView) findViewById(R.id.atd_stu_list);
        atd_db=new AttendanceDBHelper(this);
        Bundle extras=getIntent().getExtras();
        atd_rcd_list=new ArrayList<>();
        param_class_id=extras.getInt("class_id");
        param_schedule_id=extras.getInt("schedule_id");
        param_course_id=extras.getInt("course_id");
        stuList=atd_db.get_stu_list(param_class_id,param_course_id);
        aa=new FancyAdapter();
        atdStu_ListView.setAdapter(aa);
    }

    private Boolean check_stu_atd(){
        for (StudentObject sObj : stuList){
            if(sObj.getAtd_finished()==0){
                Toast.makeText(getApplicationContext(), "Plase, Record all student !", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void atd_finished(View v){

       if(check_stu_atd()){
           for (Attendance_Record_Object atdObj : atd_rcd_list){
              int atd_id= atd_db.insertAttendance(param_schedule_id,atdObj);
               if(atd_id>0 && atdObj.getBvr_list().size()>0){
                   atd_db.insertAttendanceRcd(atdObj.getBvr_list(),atd_id);
               }
           }
           atd_db.set_schedule_recorded(param_schedule_id);
           Toast.makeText(getApplicationContext(), "Attendance record finished !", Toast.LENGTH_SHORT).show();
           //finish();
           Intent intent = new Intent(AttendanceRecord_Activity.this, HomeActivity.class);
           dataBundle = new Bundle();
           dataBundle.putInt("atd_finished_param", 1);
           intent.putExtras(dataBundle);
           startActivity(intent);
       }

    }

    public void atd_cancel(View v){
        popupWindow.dismiss();
//        atd_rcd_obj.setBvr_list(bvr_list);
//        atd_rcd_list.add(atd_rcd_obj);
    }

    public void atd_ok(View view){
        atd_rcd_obj.setBvr_list(bvr_list);
        if(bvr_list.size()>0){
            atd_rcd_obj.setComment(" ");
            atd_rcd_obj.setPresent_flag(1);
        }else{
            atd_rcd_obj.setComment(comment.getText().toString());
            atd_rcd_obj.setPresent_flag(0);
        }

        atd_rcd_list.add(atd_rcd_obj);

        stuList.get(current_stu_location).setAtd_finished(1);
        //close pop up
        popupWindow.dismiss();
        //create new list view with updated data
        aa=new FancyAdapter();
        atdStu_ListView.setAdapter(aa);
    }

    class BvrList_Adapter extends ArrayAdapter<BehaviourObj> {

        BvrList_Adapter(){
            super(AttendanceRecord_Activity.this, android.R.layout.activity_list_item, bvr_list);
        }

        public View getView(final int position, View convertView,ViewGroup parent){
            Bvr_ViewHolder h;

            if(convertView==null){
                LayoutInflater inflater=getLayoutInflater();
                convertView=inflater.inflate(R.layout.behaviour_row,null);
                h = new Bvr_ViewHolder(convertView);
                convertView.setTag(h);
                h.radio_one.setTag(new Integer(position));
                h.radio_two.setTag(new Integer(position));
                h.radio_three.setTag(new Integer(position));
                h.radio_four.setTag(new Integer(position));
                h.radio_five.setTag(new Integer(position));

            }else
            {
                h=(Bvr_ViewHolder)convertView.getTag();
            }

            h.radio_one.setOnClickListener(new View.OnClickListener()
            {
                public void onClick (View v)
                {
                    BehaviourObj obj=new BehaviourObj();
                    obj=bvr_list.get(position);
                    bvr_list.get(position).setRating(1);
                }
            });
            h.radio_two.setOnClickListener(new View.OnClickListener()
            {
                public void onClick (View v)
                {
                    BehaviourObj obj=new BehaviourObj();
                    obj=bvr_list.get(position);
                    bvr_list.get(position).setRating(2);
                }
            });
            h.radio_three.setOnClickListener(new View.OnClickListener()
            {
                public void onClick (View v)
                {
                    BehaviourObj obj=new BehaviourObj();
                    obj=bvr_list.get(position);
                    bvr_list.get(position).setRating(3);
                }
            });
            h.radio_four.setOnClickListener(new View.OnClickListener()
            {
                public void onClick (View v)
                {
                    BehaviourObj obj=new BehaviourObj();
                    obj=bvr_list.get(position);
                    bvr_list.get(position).setRating(4);
                }
            });
            h.radio_five.setOnClickListener(new View.OnClickListener()
            {
                public void onClick (View v)
                {
                    BehaviourObj obj=new BehaviourObj();
                    obj=bvr_list.get(position);
                    bvr_list.get(position).setRating(5);
                }
            });
            h.populateFrom(bvr_list.get(position));
            return (convertView);
        }
    }

    class Bvr_ViewHolder{

        public TextView behaviour=null;
        public RadioButton radio_one=null;
        public RadioButton radio_two=null;
        public RadioButton radio_three=null;
        public RadioButton radio_four=null;
        public RadioButton radio_five=null;
        Bvr_ViewHolder(View row){
            behaviour=(TextView)row.findViewById(R.id.behaviour);
            radio_one=(RadioButton)row.findViewById(R.id.radio_one);
            radio_three=(RadioButton)row.findViewById(R.id.radio_three);
            radio_two=(RadioButton)row.findViewById(R.id.radio_two);
            radio_four=(RadioButton)row.findViewById(R.id.radio_four);
            radio_five=(RadioButton)row.findViewById(R.id.radio_five);
        }

        void populateFrom(BehaviourObj obj){
            behaviour.setText(obj.getBehaviour());
        }
    }

    class FancyAdapter extends ArrayAdapter<StudentObject> {

        FancyAdapter(){
            super(AttendanceRecord_Activity.this, android.R.layout.activity_list_item, stuList);
        }

        public View getView(final int position, View convertView,ViewGroup parent){
            final ViewHolder holder;
            if(convertView==null){

                LayoutInflater inflater=getLayoutInflater();
                convertView=inflater.inflate(R.layout.atd_stu_row,null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);



            }else
            {


                holder = (ViewHolder) convertView.getTag();
//                LayoutInflater inflater=getLayoutInflater();
//                convertView=inflater.inflate(R.layout.atd_stu_row,null);
//                holder = new ViewHolder(convertView);
//                convertView.setTag(holder);


            }

            if(stuList.get(position).getAtd_finished() > 0){
                //Attendance Record Finished
                holder.finished_txt.setVisibility(View.VISIBLE);
                holder.present.setVisibility(View.INVISIBLE);
                holder.absent.setVisibility(View.INVISIBLE);

            }else{
                holder.finished_txt.setVisibility(View.INVISIBLE);
                holder.present.setVisibility(View.VISIBLE);
                holder.absent.setVisibility(View.VISIBLE);
                holder.present.setTag(new Integer(position));
                holder.absent.setTag(new Integer(position));
            }

            // Catch on click event from present btn inside the student list view
            holder.present.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    //Button atvBtn=(Button) v;
                    //current_stu_location=(Integer)v.getTag();
                    current_stu_location=position;
                    //current_stu_id=Integer.parseInt(holder.stu_id.getText().toString());
                    //Attendance_Record_Object studentObject=arrayOfStuData.get(pos);

                    atd_rcd_obj=new Attendance_Record_Object();
                    StudentObject stuObj=stuList.get(current_stu_location);
                    atd_rcd_obj.setStudent_id(stuObj.getStudent_id());

                    if(stuObj.getIs_active()==2){
                        atd_rcd_obj.setStu_chk(3);
                    }else{
                        atd_rcd_obj.setStu_chk(stuObj.getStu_new());
                    }

                   //Load Pop View
                    LayoutInflater layoutInflater
                            = (LayoutInflater)getBaseContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.atd_popup, null);

                    //Set Popup window
                    popupWindow = new PopupWindow(
                            popupView,
                            AbsListView.LayoutParams.WRAP_CONTENT,
                            AbsListView.LayoutParams.WRAP_CONTENT);

                    popupWindow .setTouchable(true);
                    popupWindow .setFocusable(true);


                    popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                    popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
                    popupWindow.setAnimationStyle(R.style.PopupAnimation);
                    popupWindow.showAtLocation(atdStu_ListView, Gravity.TOP, 0, 0);

                    TextView title_txt=(TextView)popupView.findViewById(R.id.popup_title);
                    title_txt.setText("Rating !");
                    bvr_list=new ArrayList<BehaviourObj>();
                    bvr_list=atd_db.get_bvr_list();
                    bvr_lsitview=(ListView)popupView.findViewById(R.id.bvr_list_view);
                    cmt_listview=(ListView)popupView.findViewById(R.id.cmt_list_view);
                    EditText comment=(EditText)popupView.findViewById(R.id.comment);
                    cmt_listview.setVisibility(View.GONE);
                    bvr_lsitview.setVisibility(View.VISIBLE);
                    comment.setVisibility(View.GONE);

                    BvrList_Adapter bvr_view=new BvrList_Adapter();
                    bvr_lsitview.setAdapter(bvr_view);

                }
            });

            holder.absent.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    current_stu_location=position;
                    atd_rcd_obj=new Attendance_Record_Object();
                    StudentObject stuObj=stuList.get(current_stu_location);
                    atd_rcd_obj.setStudent_id(stuObj.getStudent_id());
                    if(stuObj.getIs_active()==2){
                        atd_rcd_obj.setStu_chk(3);
                    }else{
                        atd_rcd_obj.setStu_chk(stuObj.getStu_new());
                    }

                    LayoutInflater layoutInflater
                            = (LayoutInflater)getBaseContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View popupView = layoutInflater.inflate(R.layout.atd_popup, null);


                    popupWindow = new PopupWindow(
                            popupView,
                            AbsListView.LayoutParams.WRAP_CONTENT,
                            AbsListView.LayoutParams.WRAP_CONTENT);

                    popupWindow .setTouchable(true);
                    popupWindow .setFocusable(true);


                    popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                    popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
                    popupWindow.setAnimationStyle(R.style.PopupAnimation);
                    popupWindow.showAtLocation(atdStu_ListView, Gravity.TOP, 0, 0);

                    TextView title_txt=(TextView)popupView.findViewById(R.id.popup_title);
                    title_txt.setText("Comment !");

                    bvr_list=new ArrayList<BehaviourObj>();
                    final ArrayList<String> cmt_list=atd_db.get_cmt_list();
                    Button svBtn=(Button)popupView.findViewById(R.id.saveBtn);
                    bvr_lsitview=(ListView)popupView.findViewById(R.id.bvr_list_view);
                    cmt_listview=(ListView)popupView.findViewById(R.id.cmt_list_view);
                    comment=(EditText)popupView.findViewById(R.id.comment);
                    svBtn.setVisibility(View.GONE);
                    cmt_listview.setVisibility(View.VISIBLE);
                    bvr_lsitview.setVisibility(View.GONE);
                    comment.setVisibility(View.GONE);
                    ArrayAdapter arrayAdapter=new ArrayAdapter(AttendanceRecord_Activity.this,android.R.layout.simple_list_item_1,cmt_list);
                    cmt_listview.setAdapter(arrayAdapter);
                    cmt_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
                            //TODO Auto-generated method stub
                            Button svBtn=(Button)popupView.findViewById(R.id.saveBtn);
                            svBtn.setVisibility(View.VISIBLE);
                            cmt_listview.setVisibility(View.INVISIBLE);
                            comment.setVisibility(View.VISIBLE);
                            // submit();
                            comment.setText(cmt_list.get(pos));

                        }
                    });
                    // bvr_lsitview.setAdapter(bvr_view);

                }
            });

            holder.populateFrom(stuList.get(position));
            return (convertView);
        }
    }

    class ViewHolder{
        public Button present=null;
        public Button absent=null;
        public TextView name=null;
        public TextView locaiton=null;
        public TextView finished_txt;
        public TextView stu_id=null;

        ViewHolder(View row){
            name=(TextView)row.findViewById(R.id.name);
            locaiton=(TextView)row.findViewById(R.id.location);
            present=(Button)row.findViewById(R.id.atd_ok_btn);
            absent=(Button)row.findViewById(R.id.atd_absent_btn);
            finished_txt=(TextView)row.findViewById(R.id.finished_txt);
            stu_id=(TextView)row.findViewById(R.id.stu_id);
        }

        void populateFrom(StudentObject stu){
            name.setText(stu.getName());
            locaiton.setText(stu.getLocation());
            stu_id.setText(String.valueOf(stu.getStudent_id()));
        }
    }

    public  boolean onCreateOptionsMenu(Menu menu){

        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.class_display_menu, menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.addStudent:

                dataBundle = new Bundle();
                dataBundle.putInt("course_id", param_course_id);
                dataBundle.putInt("class_id", param_class_id);
                dataBundle.putInt("schedule_id",param_schedule_id);

                intent = new Intent(getApplicationContext(), DisplayStudent.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
