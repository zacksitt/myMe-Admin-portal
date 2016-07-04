package com.eunovate.eunovatedev.myapp.dao_class;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Created by EunovateDev on 1/15/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper
{
    public  static final String DATABASE_NAME="attendance_v04.db";

    public static SimpleDateFormat date_fmt=new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat datetime_fmt=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static SimpleDateFormat time_fmt=new SimpleDateFormat("HH:mm a");

    private static final int DATABASE_VERSION = 1;
    // Student Table Field
    public static final String STU_TABLE_NAME="student";
    public static final String STU_COLUMN_ID="student_id";
    public static final String STU_COLUMN_NAME="name";
    public static final String STU_COLUMN_ADDRESS="address";
    public static final String STU_COLUMN_CONTACT="contact";
    public static final String STU_COLUMN_LOCATION="location";
    public static final String STU_COLUMN_GENDER="gender";
    public static final String STU_COLUMN_DATEOFBIRTH="date_of_birth";
    public static final String STU_COLUMN_FATHERNAME="father_name";
    public static final String STU_COLUMN_FATHERNRCNO="father_nrc_no";
    public static final String STU_COLUMN_MOTHERNAME="mother_name";
    public static final String STU_COLUMN_MOTHERNRCNO="mother_nrc_no";
    public static final String STU_COLUMN_REMARK="remark";
    public static final String STU_COLUMN_NRCNO="nrc_no";
    public static final String STU_COLUMN_CDATE="created_date";
    public static final String STU_COLUMN_RADATE="reactivated_date";
    public static final String STU_COLUMN_NEW="new_stu";
    public static final String STU_COLUMN_ISACTIVE="is_active";

    // Schedule Table Field
    public static final String SCHEDULE_TABLE_NAME="schedule";
    public static final String SCHEDULE_COLUMN_ID="schedule_id";
    public static final String SCHEDULE_COLUMN_CLASS="class_id";
    public static final String SCHEDULE_COLUMN_COURSE="course";
    public static final String SCHEDULE_COLUMN_LESSON="lesson";
    public static final String SCHECULE_COLUMN_LEVEL="level";
    public static final String SCHEDULE_COLUMN_VEHICLE="vehicle";
    public static final String SCHEDULE_COLUMN_DATE="date";
    public static final String SCHEDULE_COLUMN_STIME="start_time";
    public static final String SCHEDULE_COLUMN_ETIME="end_time";
    public static final String SCHEDULE_COLUMN_TEACHER="teacher";
    public static final String SCHEDULE_COLUMN_DRIVER="driver";
    public static final String SCHEDULE_COLUMN_USER="user";
    public static final String SCHEDULE_COLUMN_RECORDED="recorded";
    public static final String SCHEDULE_COLUMN_TID="teacher_id";
    public static final String SCHEDULE_COLUMN_DID="driver_id";
    public static final String SCHEDULE_COLUMN_SENT="sent_to_server";
    public static final String SCHEDULE_COLUMN_ACTIVE="active_flag";

    //Class Table Field
    public static final String CLASS_TABLE_NAME="class_tbl";
    public static final String CLASS_COLUMN_ID="class_id";
    public static final String CLASS_COLUMN_NAME="class_name";
    public static final String CLASS_COLUMN_VEHICLE="vehicle";
    public static final String CLASS_COLUMN_USER="user";
    public static final String CLASS_COLUMN_TID="teacher_id";
    public static final String CLASS_COLUMN_LOCATION="location";
    public static final String CLASS_COLUMN_SDATE="start_date";
    public static final String CLASS_COLUMN_EDATE="end_date";
    public static final String CLASS_COLUMN_DIVISION="division";
    public static final String CLASS_COLUMN_CITY="city";
    public static final String CLASS_COLUMN_ADDRESS="contact_address";
    public static final String CLASS_COLUMN_PHONE="contact_phone";

    //Attendance Table Field
    public static final String ATD_TABLE_NAME="attendance";
    public static final String ATD_COLUMN_ID="attendance_id";
    public static final String ATD_COLUMN_COMMENT="comment";
    public static final String ATD_COLUMN_PRESENT="present_flag";
    //For student deactivate and new student
    public static final String ATD_COLUMN_STU_CHK="stu_chk";
    //Course Table Field
//    public static final String CUR_TABLE_NAME="course";
    public static final String CUR_COLUMN_ID="course_id";
//    public static final String CUR_COLUMN_DESC="description";
//
//    //Lesson Table Field
//    public static final String LESSON_TABLE_NAME="lesson";
//    public static final String LESSON_COLUMN_ID="lesson_id";
//    public static final String LESSON_COLUMN_DESC="description";

//    //Course_Class Table Field
//    public static final String CUR_CLS_TABLE_NAME="course_class";
//    public static final String CUR_CLS_COLUMN_ACT="active_flag";

    public static final String STU_CLASS_TABLE_NAME="student_class";
    public static final String STU_CLASS_COLUMN_FLAT="active_flag";

    //Behavior Table Field
    public static final String BVR_TABLE_NAME="behaviour";
    public static final String BVR_COLUMN_ID="behaviour_id";
    public static final String BVR_COLUMN_DESC="description";

    //Behavior Record Table Field
    public static final String BVR_RCD_TABLE_NAME="behaviour_record";
    public static final String BVR_RCD_COLUMN_RATING="rating";

    //Notification Table Field
    public static final String NOTI_TABLE_NAME="notification";
    public static final String NOTI_COLUMN_ID="noti_id";
    public static final String NOTI_COLUMN_UID="user_id";
    public static final String NOTI_COLUMN_DESC="description";
    public static final String NOTI_COLUMN_SEEN="seen";
    public static final String NOTI_COLUMN_DATETIME="date_time";

    //Comment Table Field
    public static final String COMMENT_TABLE_NAME="comment";
    public static final String COMMENT_COLUMN_ID="comment_id";
    public static final String COMMENT_DESC="description";

    //Comment Location Field
    public static final String LOCATION_TABLE_NAME="location";
    public static final String LOCATION_COLUMN_ID="location_id";
    public static final String LOCATION_COLUMN_DESC="description";
    public static final String LOCATION_COLUMN_NEWFLG="lg_new_flag";
    //Vehicle Table Field
    public static final String VEHICLE_TABLE_NAME="vehicle";
    public static final String VEHICLE_COLUMN_ID="vehicle_id";
    public static final String VEHICLE_COLUMN_BRAND="brand";
    public static final String VEHICLE_COLUMN_MODEL="model";
    public static final String VEHICLE_COLUMN_CHASSIC="chassic";
    public static final String VEHICLE_COLUMN_ENGINE="engine";
    public static final String VEHICLE_COLUMN_LEDATE="licence_expired_date";
    public static final String VEHICLE_COLUMN_BDATE="bought_date";
    public static final String VEHICLE_COLUMN_COLOR="color";
    public static final String VEHICLE_COLUMN_NO="no";

    //Vehicle Maintenance Field
    public static final String VELMTN_TABLE_NAME = "vehicle_maintenance";
    public static final String VELMTN_COLUMN_RCD_ID = "record_id";
    public static final String VELMTN_COLUMN_SERVICE = "servicing";
    public static final String VELMTN_COLUMN_OIL = "oil";
    public static final String VELMTN_COLUMN_COOLANT = "coolant";
    public static final String VELMTN_COLUMN_AIR = "aircon";
    public static final String VELMTN_COLUMN_ENGOIL = "engine_oil";
    public static final String VELMTN_COLUMN_CARBDY = "car_body";
    public static final String VELMTN_COLUMN_BRAKE = "brake";
    public static final String VELMTN_COLUMN_LIGHT = "signal_light";
    public static final String VELMTN_COLUMN_FBLIGHT = "front_back_light";
    public static final String VELMTN_COLUMN_WHEEL = "wheel";
    public static final String VELMTN_COLUMN_COMMENT = "comment";
    public static final String VELMTN_COLUMN_CTIME = "created_time";
    public static final String VELMTN_COLUMN_SENT = "sent_to_server";
    public static final String VELMTN_COLUMN_USER = "user_id";

    //Vehicle Usage Field;
    public static final String VELUGE_TABLE_NAME = "vehicle_usage";
    public static final String VELUGE_COLUMN_ID = "vehicle_usage_id";
    public static final String VELUGE_COLUMN_STODOMTR = "start_odometer";
    public static final String VELUGE_COLUMN_STTIME = "start_time";
    public static final String VELUGE_COLUMN_ENDOMTR = "end_odometer";
    public static final String VELUGE_COLUMN_ENDTIME = "end_time";
    public static final String VELUGE_COLUMN_SENT = "sent_to_server";
    public static final String VELUGE_COLUMN_UID = "user_id";

    //Vehicle Usage Line Field
    public static final String VELUGE_LINE_TABLE_NAME="vehicle_usage_line";
    public static final String VELUGE_LINE_COLUMN_LOCATION="location_id";
    public static final String VELUGE_LINE_COLUMN_ADTIME="added_time";
    public static final String VELUGE_LINE_COLUMN_GPS="gps_location";

    public static final String VELUGE_LINE_COLUMN_ACTFLG="active_flag";

//    //Last Sync Field
//    public static final String SYNC_COUNT_TABLE_NAME="last_sync";
//    public static final String SYNC_COUNT_COLUMN_CATID="sync_category_id";
//    public static final String SYNC_COUNT_COLUMN_CAT="category";
//    public static final String SYNC_COUNT_COLUMN_COUNT="last_sync_count";

    SQLiteDatabase db;
    public static final String CREATE_SCHEDULE_TABLE="CREATE TABLE "
            +SCHEDULE_TABLE_NAME + "(" + SCHEDULE_COLUMN_ID + " INTEGER PRIMARY KEY, "
            +SCHEDULE_COLUMN_CLASS + " TEXT, " + SCHEDULE_COLUMN_COURSE + " TEXT, " + SCHEDULE_COLUMN_LESSON + " TEXT, " + CUR_COLUMN_ID + " INTEGER, "
            +SCHEDULE_COLUMN_VEHICLE + " TEXT, " + SCHEDULE_COLUMN_DATE + " DATE, " + SCHEDULE_COLUMN_STIME + " DATETIME, "
            +SCHEDULE_COLUMN_ETIME + " DATETIME, " + SCHECULE_COLUMN_LEVEL + " TEXT,"
            +SCHEDULE_COLUMN_TEACHER + " TEXT, " + SCHEDULE_COLUMN_DRIVER + " TEXT, " + SCHEDULE_COLUMN_USER + " TEXT, " + SCHEDULE_COLUMN_TID + " INTEGER, " + SCHEDULE_COLUMN_DID + " INTEGER, "
            +SCHEDULE_COLUMN_RECORDED + " INTEGER DEFAULT 0, " +SCHEDULE_COLUMN_SENT+" INTEGER DEFAULT 0, "+SCHEDULE_COLUMN_ACTIVE +" INTEGER)";

    public  static final String CREATE_STUDENT_TABLE="CREATE TABLE "
            +STU_TABLE_NAME + "(" + STU_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +STU_COLUMN_NAME + " TEXT, " + STU_COLUMN_ADDRESS + " TEXT, " + STU_COLUMN_CONTACT + " TEXT, " + STU_COLUMN_CDATE + " DATE, " + STU_COLUMN_RADATE + " DATE,"
            +STU_COLUMN_DATEOFBIRTH + " DATE, " + STU_COLUMN_FATHERNAME + " TEXT, " + STU_COLUMN_FATHERNRCNO + " TEXT, "
            +STU_COLUMN_GENDER + " INTEGER, " + STU_COLUMN_MOTHERNRCNO + " TEXT, " + STU_COLUMN_MOTHERNAME + " TEXT, " + STU_COLUMN_NEW + " INTEGER DEFAULT 0,"
            +STU_COLUMN_LOCATION + " TEXT, " + STU_COLUMN_NRCNO + " TEXT, " + STU_COLUMN_REMARK +" TEXT, " + STU_COLUMN_ISACTIVE + " INTEGER)";

    public  static final  String CREATE_CLASS_TABLE="CREATE TABLE "
            + CLASS_TABLE_NAME + "(" + CLASS_COLUMN_ID + " INTEGER PRIMARY KEY, " + CLASS_COLUMN_TID + " INTEGER, "
            + CLASS_COLUMN_LOCATION + " TEXT, " + CLASS_COLUMN_NAME + " TEXT, " + CLASS_COLUMN_USER + " INTEGER, "
            + CLASS_COLUMN_VEHICLE + " TEXT, " + CLASS_COLUMN_SDATE + " DATE, " + CLASS_COLUMN_EDATE + " DATE, " + CLASS_COLUMN_DIVISION  + " TEXT," + CLASS_COLUMN_CITY + " TEXT,"
            + CLASS_COLUMN_ADDRESS + " TEXT, " + CLASS_COLUMN_PHONE + " TEXT)";

    public  static final  String CREATE_STU_CLSSS_TABLE="CREATE TABLE "
        + STU_CLASS_TABLE_NAME + "(" + STU_COLUMN_ID + " INTEGER, " + STU_COLUMN_NEW + " INTEGER, " + CUR_COLUMN_ID + " INTEGER, "
        + CLASS_COLUMN_ID + " INTEGER, " + STU_CLASS_COLUMN_FLAT + "  INTEGER, PRIMARY KEY ( " + STU_COLUMN_ID + "," + CLASS_COLUMN_ID + "," + CUR_COLUMN_ID + "))";

    public static final String CREATE_BVR_TABLE="CREATE TABLE "
            + BVR_TABLE_NAME + " ( " + BVR_COLUMN_ID + " INTEGER PRIMARY KEY, " + BVR_COLUMN_DESC + " TEXT)";

    public static final String CREATE_BVR_RCD_TABLE="CREATE TABLE "
            + BVR_RCD_TABLE_NAME + " ( " + BVR_COLUMN_ID + " INTEGER, " + ATD_COLUMN_ID + " INTEGER, " + BVR_RCD_COLUMN_RATING + " TEXT, " +
            " PRIMARY KEY( " + BVR_COLUMN_ID + " , "+ ATD_COLUMN_ID + "))";

//    public static final String CREATE_COURSE_TABLE="CREATE TABLE "
//            + CUR_TABLE_NAME + " ( " + CUR_COLUMN_ID + " INTEGER PRIMARY KEY, " + CUR_COLUMN_DESC + " TEXT)";

    public static final String CREATE_ATD_TABLE="CREATE TABLE "
            + ATD_TABLE_NAME + " ( " + ATD_COLUMN_ID + " INTEGER PRIMARY KEY, " + STU_COLUMN_ID + " INTEGER, " + SCHEDULE_COLUMN_ID + " INTEGER, "
            + ATD_COLUMN_STU_CHK + " INTEGER DEFAULT 0, " + ATD_COLUMN_PRESENT + " INTEGER, " + ATD_COLUMN_COMMENT + " TEXT)";

//    public static final String CREATE_CUR_CLS_TABLE="CREATE TABLE "
//            + CUR_CLS_TABLE_NAME + " ( " + CLASS_COLUMN_ID + " INTEGER, " + CUR_COLUMN_ID + " INTEGER, " + CUR_CLS_COLUMN_ACT + " TEXT, " +
//            " PRIMARY KEY ( " + CLASS_COLUMN_ID + " , " + CUR_COLUMN_ID + "))";

    public static final String CREATE_NOTI_TABLE="CREATE TABLE "
            + NOTI_TABLE_NAME + " ( " + NOTI_COLUMN_ID + " INTEGER PRIMARY KEY, " + NOTI_COLUMN_DESC + " TEXT, " + NOTI_COLUMN_UID + " INTEGER, "
            + NOTI_COLUMN_DATETIME + " DATETIME, " + NOTI_COLUMN_SEEN + " BOOLEAN)";

    public static final String CREATE_COMMENT_TABLE="CREATE TABLE "
            + COMMENT_TABLE_NAME + " ( " + COMMENT_COLUMN_ID + " INTEGER PRIMARY KEY, " + COMMENT_DESC + " TEXT)";

//    public static final String CREATE_LESSON_TABLE="CREATE TABLE "
//            + LESSON_TABLE_NAME + " ( " + LESSON_COLUMN_ID + " INTEGER PRIMARY KEY, " + CUR_COLUMN_ID + " INTEGER, "
//            + LESSON_COLUMN_DESC + " TEXT)";

    public static final String CREATE_VEHICLE_TABLE="CREATE TABLE "
            + VEHICLE_TABLE_NAME + " ( " + VEHICLE_COLUMN_ID + " INTEGER PRIMARY KEY, " + VEHICLE_COLUMN_BRAND + " TEXT, " + VEHICLE_COLUMN_CHASSIC + " TEXT, "
            + VEHICLE_COLUMN_COLOR + " TEXT, " + VEHICLE_COLUMN_ENGINE + " TEXT, " + VEHICLE_COLUMN_LEDATE+ " DATE, " + VEHICLE_COLUMN_MODEL + " TEXT, "
            + VEHICLE_COLUMN_BDATE + " DATE, " + VEHICLE_COLUMN_NO + " TEXT)";

    public static final String CREATE_VHLMTN_TABLE="CREATE TABLE "
            + VELMTN_TABLE_NAME + " ( "+ VELMTN_COLUMN_RCD_ID + " INTEGER PRIMARY KEY, " + VEHICLE_COLUMN_ID + " INTEGER, " +VELMTN_COLUMN_SERVICE+ " INTEGER, " + VELMTN_COLUMN_OIL + " INTEGER, "
            + VELMTN_COLUMN_COOLANT + " INTEGER, " + VELMTN_COLUMN_AIR + " INTEGER, " + VELMTN_COLUMN_ENGOIL + " INTEGER, " + VELMTN_COLUMN_CARBDY + " INTEGER, "
            + VELMTN_COLUMN_BRAKE + " INTEGER, " + VELMTN_COLUMN_LIGHT + " INTEGER, " + VELMTN_COLUMN_FBLIGHT + " INTEGER, " + VELMTN_COLUMN_WHEEL + " INTEGER, "
            + VELMTN_COLUMN_COMMENT + " TEXT, " + VELMTN_COLUMN_CTIME + " DATETIME, "+ VELMTN_COLUMN_SENT + " INTEGER, "+ VELMTN_COLUMN_USER + " TEXT)";

    public static final String CREATE_VHLUGE_TABLE="CREATE TABLE "
            + VELUGE_TABLE_NAME + " ( " + VELUGE_COLUMN_ID + " INTEGER PRIMARY KEY, " + VEHICLE_COLUMN_ID + " INTEGER, " + VELUGE_COLUMN_STODOMTR + " INTEGER, "
            + VELUGE_COLUMN_SENT+ " INTEGER, "+ VELUGE_COLUMN_STTIME + " DATETIME, " + VELUGE_COLUMN_ENDOMTR+ " INTEGER, " + VELUGE_COLUMN_ENDTIME + " DATETIME," + VELUGE_COLUMN_UID + " INTEGER)";

    public static final String CREATE_VHLUGE_LINE_TABLE="CREATE TABLE "
            + VELUGE_LINE_TABLE_NAME + " ( " + VELUGE_COLUMN_ID +" INTEGER, " + LOCATION_COLUMN_ID + " INTEGER," + VELUGE_LINE_COLUMN_ADTIME + " DATETIME,"
            + VELUGE_LINE_COLUMN_GPS + " TEXT, " + VELUGE_LINE_COLUMN_ACTFLG + " INTEGER)";

    public static final String CREATE_LOCATION_TABLE="CREATE TABLE "
            + LOCATION_TABLE_NAME + " ( " + LOCATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + LOCATION_COLUMN_DESC + " TEXT, " + LOCATION_COLUMN_NEWFLG + " INTEGER)";

//    public static final String CREATE_LAST_SYNC_TABLE="CREATE TABLE "
//            + SYNC_COUNT_TABLE_NAME + " ( " + SYNC_COUNT_COLUMN_CATID + " INTEGER, " + SYNC_COUNT_COLUMN_CAT + " TEXT," + SYNC_COUNT_COLUMN_COUNT + " INTEGER)";

    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DataBaseHelper(context);
        return instance;
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //TODO Auto-generated method stub
        db.execSQL(CREATE_SCHEDULE_TABLE);
        Log.i("LOG TAG", "CREATED SCHEDULE TABLE");
        db.execSQL(CREATE_STUDENT_TABLE);
        Log.i("LOG TAG", "CREATED STUDENT TABLE");
        db.execSQL(CREATE_CLASS_TABLE);
        Log.i("LOG TAG", "CREATED CLASS TABLE");
        db.execSQL(CREATE_STU_CLSSS_TABLE);
        Log.i("LOG TAG", "CREATED STUDENT_CLASS TABLE");
        db.execSQL(CREATE_BVR_TABLE);
        Log.i("LOG TAG", "CREATED BEHAVIOUR TABLE");
        db.execSQL(CREATE_BVR_RCD_TABLE);
        Log.i("LOG TAG", "CREATED BEHAVIOUR RECOURD TABLE");
//        db.execSQL(CREATE_COURSE_TABLE);
//        Log.i("LOG TAG", "CREATED COURSE TABLE");
        db.execSQL(CREATE_ATD_TABLE);
        Log.i("LOG TAG", "CREATED ATTENDANCE TABLE");
//        db.execSQL(CREATE_CUR_CLS_TABLE);
//        Log.i("LOG TAG", "CREATED COURSE_CLASS TABLE");
        db.execSQL(CREATE_NOTI_TABLE);
        Log.i("LOG TAG", "CREATED NOTI TABLE");
        db.execSQL(CREATE_COMMENT_TABLE);
        Log.i("LOG TAG", "CREATED COMMENT TABLE");
//        db.execSQL(CREATE_LESSON_TABLE);
//        Log.i("LOG TAG", "CREATED LESSON TABLE");
        db.execSQL(CREATE_VEHICLE_TABLE);
        Log.i("LOG TAG", "CREATED VEHICLE TABLE");
        db.execSQL(CREATE_VHLMTN_TABLE);
        Log.i("LOG TAG", "CREATED VEHICLE MAINTENANCE TABLE");
        db.execSQL(CREATE_VHLUGE_TABLE);
        Log.i("LOG TAG", "CREATED VEHICLE USAGE TABLE");
        db.execSQL(CREATE_VHLUGE_LINE_TABLE);
        Log.i("LOG TAG", "CREATED VEHICLE USAGE LINE TABLE");
        db.execSQL(CREATE_LOCATION_TABLE);
        Log.i("LOG TAG", "CREATED LOCATION TABLE");
//        db.execSQL(CREATE_LAST_SYNC_TABLE);
//        Log.i("LOG TAG", "CREATED LAST SYNC TABLE");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS schedule");
        db.execSQL("DROP TABLE IF EXISTS student");
        db.execSQL("DROP TABLE IF EXISTS class_tbl");
        onCreate(db);
    }
}
