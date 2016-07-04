package com.eunovate.eunovatedev.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.eunovate.eunovatedev.myapp.dao_class.AttendanceDBHelper;
import com.eunovate.eunovatedev.myapp.object.MaintenanceObject;
import com.eunovate.eunovatedev.myapp.object.VehicleObject;

public class AddVehicleMaintenance_Activity extends AppCompatActivity implements OnItemSelectedListener {
    Toolbar myToolbar;
    CheckBox oil,coolant,air,engineoil,car_body,brake,light,fb_light,wheel,service;
    EditText comment;
    AttendanceDBHelper db;
    MaintenanceObject obj;
    Spinner spinner;
    AttendanceDBHelper atd_db;
    VehicleObject vObj;
    ArrayList<VehicleObject> vList;
    int vehicle_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_maintenance_);
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
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        vList=new ArrayList<>();
        atd_db=new AttendanceDBHelper(this);
        vList=atd_db.get_vlist();
        Log.i("Log_Tag","Vehicle List"+vList);
        List<String> vehicles = new ArrayList<String>();
        for (int i=0; i<vList.size(); i++){
            vObj=new VehicleObject();
            vObj=vList.get(i);
            vehicles.add(vObj.getVehicle_no());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vehicles);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        db=new AttendanceDBHelper(this);
        oil=(CheckBox) findViewById(R.id.chk_oil);
        coolant=(CheckBox) findViewById(R.id.chk_coolant);
        air=(CheckBox) findViewById(R.id.chk_air);
        engineoil=(CheckBox) findViewById(R.id.chk_engine);
        car_body=(CheckBox) findViewById(R.id.chk_carbdy);
        brake=(CheckBox) findViewById(R.id.chk_brake);
        light=(CheckBox) findViewById(R.id.chk_light);
        fb_light=(CheckBox) findViewById(R.id.chk_fblight);
        wheel=(CheckBox) findViewById(R.id.chk_wheel);
        service=(CheckBox) findViewById(R.id.chk_service);

        comment=(EditText) findViewById(R.id.comment);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        vehicle_id=vList.get(position).getVehicle_id();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void save(View view){
        obj=new MaintenanceObject();
        if(oil.isChecked())
            obj.setOil(1);
        else
            obj.setOil(0);

        if(coolant.isChecked())
            obj.setCoolant(1);
        else
            obj.setCoolant(0);

        if(air.isChecked())
            obj.setAir(1);
        else
            obj.setAir(0);

        if(engineoil.isChecked())
            obj.setEngineoile(1);
        else
            obj.setEngineoile(0);

        if (car_body.isChecked())
            obj.setCar_body(1);
        else
            obj.setCar_body(0);

        if(brake.isChecked())
            obj.setBrake(1);
        else
            obj.setBrake(0);

        if(light.isChecked())
            obj.setLight(1);
        else
            obj.setLight(0);

        if(fb_light.isChecked())
            obj.setFb_light(1);
        else
            obj.setFb_light(0);

        if(wheel.isChecked())
            obj.setWheel(1);
        else
            obj.setWheel(0);

        if(service.isChecked())
            obj.setService(1);
        else
            obj.setService(0);

        obj.setComment(comment.getText().toString());
        obj.setVehicle(vehicle_id);
        Log.i("Log_Tag","MTN OBJ"+obj);
        if(db.insert_maintenance(obj)){
            Toast.makeText(getApplicationContext(), "Inserted vehicle maintenance !", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"not Inserted",Toast.LENGTH_SHORT).show();
        }
    }
}
