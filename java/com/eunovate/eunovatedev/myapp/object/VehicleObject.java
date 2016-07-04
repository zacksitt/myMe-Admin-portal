package com.eunovate.eunovatedev.myapp.object;

/**
 * Created by EunovateDev on 2/11/2016.
 */
public class VehicleObject {
    public String vehicle_no;
    public int vehicle_id;
    public int doingChk;
    public String vehicle_model;
    public int vehicle_usgae_id;

    public int getVehicle_usgae_id() {
        return vehicle_usgae_id;
    }

    public void setVehicle_usgae_id(int vehicle_usgae_id) {
        this.vehicle_usgae_id = vehicle_usgae_id;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public int getDoingChk() {
        return doingChk;
    }

    public void setDoingChk(int doingChk) {
        this.doingChk = doingChk;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }
}
