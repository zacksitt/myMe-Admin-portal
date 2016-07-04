package com.eunovate.eunovatedev.myapp.object;

import java.util.Date;

/**
 * Created by EunovateDev on 2/10/2016.
 */
public class MaintenanceObject {
    public int vehicle;
    public int oil;
    public int coolant;
    public int air;
    public int engineoile;
    public int car_body;
    public int brake;
    public int light;
    public int fb_light;
    public int wheel;
    public int service;
    public String comment;
    public Date create_time;

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getWheel() {
        return wheel;
    }

    public void setWheel(int wheel) {
        this.wheel = wheel;
    }

    public int getFb_light() {
        return fb_light;
    }

    public void setFb_light(int fb_light) {
        this.fb_light = fb_light;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getBrake() {
        return brake;
    }

    public void setBrake(int brake) {
        this.brake = brake;
    }

    public int getCar_body() {
        return car_body;
    }

    public void setCar_body(int car_body) {
        this.car_body = car_body;
    }

    public int getEngineoile() {
        return engineoile;
    }

    public void setEngineoile(int engineoile) {
        this.engineoile = engineoile;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAir() {
        return air;
    }

    public void setAir(int air) {
        this.air = air;
    }

    public int getCoolant() {
        return coolant;
    }

    public void setCoolant(int coolant) {
        this.coolant = coolant;
    }

    public int getOil() {
        return oil;
    }

    public void setOil(int oil) {
        this.oil = oil;
    }

    public int getVehicle() {
        return vehicle;
    }

    public void setVehicle(int vehicle) {
        this.vehicle = vehicle;
    }
}
