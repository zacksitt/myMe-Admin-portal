package com.eunovate.eunovatedev.myapp.object;

import java.util.ArrayList;

/**
 * Created by EunovateDev on 1/26/2016.
 */
public class Attendance_Record_Object {
    private int attendance_id;
    private int student_id;
    private int schedule_id;
    private int present_flag;
    private int behaviour_id;
    private int rating;
    private String comment;
    private int stu_new;
    private int stu_chk;

    public int getStu_chk() {
        return stu_chk;
    }

    public void setStu_chk(int stu_chk) {
        this.stu_chk = stu_chk;
    }

    private ArrayList<BehaviourObj> bvr_list;

    public int getStu_new() {
        return stu_new;
    }

    public void setStu_new(int stu_new) {
        this.stu_new = stu_new;
    }

    public int getPresent_flag() {
        return present_flag;
    }

    public void setPresent_flag(int present_flag) {
        this.present_flag = present_flag;
    }

    public ArrayList<BehaviourObj> getBvr_list() {
        return bvr_list;
    }

    public void setBvr_list(ArrayList<BehaviourObj> bvr_list) {
        this.bvr_list = bvr_list;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getBehaviour_id() {
        return behaviour_id;
    }

    public void setBehaviour_id(int behaviour_id) {
        this.behaviour_id = behaviour_id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(int attendance_id) {
        this.attendance_id = attendance_id;
    }
}
