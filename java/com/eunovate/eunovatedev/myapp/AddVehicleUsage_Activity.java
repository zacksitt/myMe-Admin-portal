package com.eunovate.eunovatedev.myapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.object.VehicleObject;
import com.eunovate.eunovatedev.myapp.object.VehicleUsageObject;

import java.util.ArrayList;
import java.util.List;

public class AddVehicleUsage_Activity extends AppCompatActivity{
    private Toolbar myToolbar;
    private TextView stodometer_tv, end_odometer_tv;
    private EditText start_odometer, end_odometer;
    private VehicleUsageObject vusgObj;
    private AttendanceDBHelper db;
    private ArrayList<VehicleObject> vList;
    private ArrayList<VehicleUsageObject> locationList, allLocationList;
    private VehicleObject vObj;
    private ListView vListView;
    private RelativeLayout vLayout;
    private ScrollView usageSView;
    private LocationManager mLocationManager;
    private LocationListener locationListener;
    private Button location_accept, location_remove, endodometer_accept, viewLocationbtn;
    private int usage_id, vehicle_id, location_id = 0, doing_chk;
    private AutoCompleteTextView locationAct;
    private Button doneBtn;
    private String gps_location="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_usage_);
        myToolbar = (Toolbar) findViewById(R.id.display_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_back_btn);
        //catch event when user click back button from toolbar
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        end_odometer_tv = (TextView) findViewById(R.id.tv_end_odometer);
        stodometer_tv = (TextView) findViewById(R.id.tv_stodometer);
        viewLocationbtn = (Button) findViewById(R.id.locationbtn);
        start_odometer = (EditText) findViewById(R.id.start_odometer);
        //location=(EditText) findViewById(R.id.add_location);
        locationAct = (AutoCompleteTextView) findViewById(R.id.add_location);
        end_odometer = (EditText) findViewById(R.id.end_odometer);
        location_accept = (Button) findViewById(R.id.location_accept);
        location_remove = (Button) findViewById(R.id.location_remove);
        endodometer_accept = (Button) findViewById(R.id.endodometer_accept);
        doneBtn=(Button) findViewById(R.id.doneBtn);


        setup_VehicleList();
        showAddLocation();

        // Acquire a reference to the system Location Manager
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //update GPS location
                gps_location = String.valueOf(location.getLatitude())+"|"+String.valueOf(location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new  String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10);
                        return;
            }
        }else{
            mLocationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            Location loc = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
            if(loc!=null){
                gps_location = String.valueOf(loc.getLatitude())+"|"+String.valueOf(loc.getLongitude());
                Toast.makeText(getApplicationContext(),"GOT GPS LOCATION ! "+ gps_location, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mLocationManager.requestLocationUpdates("gps",5000,0,locationListener);
                    return;

        }
    }

    private void configureButton() {
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationManager.requestLocationUpdates("gps",5000,0,locationListener);
            }
        });

    }

//    @Override
//    public void onProviderDisabled(String string){}
//    @Override
//    public void onStatusChanged(String string,int myint,Bundle bundle){}
//    @Override
//    public void onLocationChanged(Location location){}
//    @Override
//    public void onProviderEnabled(String string){}
//    private void makeUseOfNewLocation(Location location){
//        Log.i("LOG TAG","LOCATION"+location.getLatitude());
//        Toast.makeText(getApplicationContext(),""+location.getLatitude(), Toast.LENGTH_SHORT).show();
//    }

    public void setup_VehicleList(){
//        locationAct.setVisibility(View.INVISIBLE);
//        end_odometer.setVisibility(View.INVISIBLE);
//        end_odometer_tv.setVisibility(View.INVISIBLE);
//        location_accept.setVisibility(View.INVISIBLE);
//        location_remove.setVisibility(View.INVISIBLE);
//        endodometer_accept.setVisibility(View.INVISIBLE);

        vListView=(ListView) findViewById(R.id.vListView);
        vLayout=(RelativeLayout) findViewById(R.id.vehicle_layout);
        usageSView=(ScrollView) findViewById(R.id.scrollView1);
        usageSView.setVisibility(View.INVISIBLE);

        db=new AttendanceDBHelper(this);
        vList=new ArrayList<>();
        vList=db.get_vlist();
        List<String> vehicles = new ArrayList<String>();
        for (int i=0; i<vList.size(); i++){
            vObj=new VehicleObject();
            vObj=vList.get(i);
            vehicles.add(vObj.getVehicle_no());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, vehicles);
        vListView.setAdapter(adapter);
        vListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
                vehicle_id = vList.get(arg2).getVehicle_id();
                doing_chk = vList.get(arg2).getDoingChk();
                usage_id = vList.get(arg2).getVehicle_usgae_id();

                if (doing_chk > 0) {
                    //Vehicle Usage is doing!
                    vusgObj = new VehicleUsageObject();

                    vusgObj = db.get_vusage_dtl(vehicle_id, usage_id);
                    start_odometer.setText(String.valueOf(vusgObj.getStart_odometer()).toString());

                    if (vusgObj.getLocation_count() > 0) {
                        viewLocationbtn.setVisibility(View.VISIBLE);
                    } else {
                        viewLocationbtn.setVisibility(View.INVISIBLE);
                    }
               }else {
                    viewLocationbtn.setVisibility(View.GONE);
                }

// else {
//                    start_odometer.clearFocus();
//                    end_odometer.setText(" ");
//                    locationAct.setText(" ");
//                    viewLocationbtn.setVisibility(View.INVISIBLE);
//                }

                vLayout.setVisibility(View.GONE);
                usageSView.setVisibility(View.VISIBLE);

            }
        });
    }

    public void clearLocation(View v){
        locationAct.setText("");
        locationAct.clearFocus();
    }

    public void done(View view){
       // vLayout.setVisibility(View.VISIBLE);
        //setup_VehicleList();
        //mLocationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        finish();
    }

    public void checkLocation(View v){

        vusgObj=new VehicleUsageObject();
        vusgObj.setLocation_id(location_id);
        vusgObj.setVehicle_usage_id(usage_id);
        vusgObj.setGps_location(gps_location);
        vusgObj.setLocation_desc(locationAct.getText().toString());

        if(db.insert_location(vusgObj)){
            viewLocationbtn.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Inserted location !", Toast.LENGTH_SHORT).show();
            location_id=0;
            showAddLocation();
        }else{
            Toast.makeText(getApplicationContext(),"not Inserted",Toast.LENGTH_SHORT).show();
        }
    }

    public void showAddLocation(){
        allLocationList=new ArrayList<VehicleUsageObject>();
        allLocationList=db.get_location(0);
        List<String> locations = new ArrayList<String>();
        for (int i=0; i<allLocationList.size(); i++){
            vusgObj=new VehicleUsageObject();
            vusgObj=allLocationList.get(i);
            locations.add(vusgObj.getLocation_desc());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,locations);
      //aa=new FancyAdapter();
     // locationAct.setAdapter(aa);

        locationAct.setAdapter(adapter);
        locationAct.setThreshold(1);
        locationAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                String selection = (String) arg0.getItemAtPosition(arg2);
                for (VehicleUsageObject obj : allLocationList) {
                    if(selection.equals(obj.getLocation_desc())){
                        location_id = obj.getLocation_id();
                    }
                }



            }
        });

        viewLocationbtn.setVisibility(View.VISIBLE);
        locationAct.setVisibility(View.VISIBLE);
        location_accept.setVisibility(View.VISIBLE);
        location_remove.setVisibility(View.VISIBLE);
    }

    public void showLocation(View v){

        Log.i("LOG TAG","USAGE ID : "+usage_id);
        locationList=db.get_location(usage_id);
        List<String> locations = new ArrayList<String>();
        for (int i=0; i<locationList.size(); i++){
            vusgObj=new VehicleUsageObject();
            vusgObj=locationList.get(i);
            locations.add(vusgObj.getLocation_desc());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, locations);



        LayoutInflater layoutInflater
                = (LayoutInflater)getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                AbsListView.LayoutParams.WRAP_CONTENT,
                AbsListView.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView)popupView.findViewById(R.id.locationList);
        listView.setAdapter(adapter);
        Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(stodometer_tv, Gravity.TOP, 0, 0);

    }
//
//    public void showEndOdometer(View v){
//        end_odometer_tv.setVisibility(View.VISIBLE);
//        end_odometer.setVisibility(View.VISIBLE);
//        endodometer_accept.setVisibility(View.VISIBLE);
//    }

    public void checkStOdo(View v){

        vusgObj=new VehicleUsageObject();

        vusgObj.setStart_odometer(Integer.parseInt(start_odometer.getText().toString()));
        vusgObj.setEnd_odometer(0);
        vusgObj.setVehicle_usage_id(usage_id);
        if(doing_chk>0){
            //Updage Usage
            if(db.update_vehicle_usage(vusgObj,0) > 0){
                Toast.makeText(getApplicationContext(), "Updated start odometer !", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Can't updated start odometer !", Toast.LENGTH_SHORT).show();
            }
        }else{
            vusgObj.setVehicle_id(vehicle_id);
            //Insert Usage
            usage_id=db.insert_vehicle_usage(vusgObj);
            if(usage_id>0){
                Toast.makeText(getApplicationContext(), "Inserted start odometer !", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getApplicationContext(),"not Inserted",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void checkEndOdometer(View v){

        vusgObj=new VehicleUsageObject();
        vusgObj.setEnd_odometer(Integer.parseInt(end_odometer.getText().toString()));
        vusgObj.setVehicle_usage_id(usage_id);
             //Update end odometer to vehicle usage table
            if(db.update_vehicle_usage(vusgObj,1)>0){
                Toast.makeText(getApplicationContext(), "Inserted end odometer !", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getApplicationContext(),"not Inserted",Toast.LENGTH_SHORT).show();
            }
    }

    class FancyAdapter extends ArrayAdapter<VehicleUsageObject> {

        FancyAdapter(){
            super(AddVehicleUsage_Activity.this,android.R.layout.activity_list_item,allLocationList);
        }

        public View getView(int position, View convertView,ViewGroup parent){
            ViewHolder holder;

            if(convertView==null){
                LayoutInflater inflater=getLayoutInflater();
                convertView=inflater.inflate(R.layout.location_row,null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else
            {
                holder=(ViewHolder)convertView.getTag();
            }
            holder.populateFrom(allLocationList.get(position));
            return (convertView);
        }
    }

    class ViewHolder{

        public TextView location=null;
        public TextView locationid=null;

        ViewHolder(View row){
            locationid=(TextView)row.findViewById(R.id.location_id);
            location=(TextView)row.findViewById(R.id.location);
        }

        void populateFrom(VehicleUsageObject stu){
            location.setText(stu.getLocation_desc());
            locationid.setText(stu.getLocation_id());
        }
    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        Bundle extras=getIntent().getExtras();
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return  true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()){

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
