package com.eunovate.eunovatedev.myapp.object;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by EunovateDev on 2/10/2016.
 */
public class VehicleUsageObject {
    public int start_odometer;
    public Date start_time;
    public Date end_tiem;
    public int end_odometer;
    public int vehicle_id;
    public int location_id;
    public int location_count;
    public String location_desc;
    public String gps_location;
    public int vehicle_usage_id;

    public String getGps_location() {
        return gps_location;
    }

    public void setGps_location(String gps_location) {
        this.gps_location = gps_location;
    }

    public String getLocation_desc() {
        return location_desc;
    }

    public void setLocation_desc(String location_desc) {
        this.location_desc = location_desc;
    }

    public int getVehicle_usage_id() {
        return vehicle_usage_id;
    }

    public void setVehicle_usage_id(int vehicle_usage_id) {
        this.vehicle_usage_id = vehicle_usage_id;
    }


    public int getLocation_count() {
        return location_count;
    }

    public void setLocation_count(int location_count) {
        this.location_count = location_count;
    }


    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public int getEnd_odometer() {
        return end_odometer;
    }

    public void setEnd_odometer(int end_odometer) {
        this.end_odometer = end_odometer;
    }

    public Date getEnd_tiem() {
        return end_tiem;
    }

    public void setEnd_tiem(Date end_tiem) {
        this.end_tiem = end_tiem;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public int getStart_odometer() {
        return start_odometer;
    }

    public void setStart_odometer(int start_odometer) {
        this.start_odometer = start_odometer;
    }
}
