package com.clj.blesample.databasemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.clj.blesample.model.GasConsumptionPatternDTO;
import com.clj.blesample.model.MaintenaceServiceDTO;
import com.clj.blesample.model.StatisticsDTO;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.MathUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "scsk";
    public static final int DATABASE_VERSION = 1;

    //Maintenance Service Column Datas
    public static final String TABLE_NAME = "maintenaceservice";

    public static final String COLUMN_ID = "id";
    public static final String MISSUE_ID = "missue_id";
    public static final String FIXED_DATE = "fixed_date";
    public static final String PERSON_NAME = "mperson";
    public static final String ISSUE = "issue";
    public static final String DEVICE_ID = "deviceid";
    //End Of //Maintenance Service Column Datas

    public static final String ST_TABLE_NAME = "statisticsreport";
    public static final String ST_COOKING_ID = "cooking_id";
    public static final String ST_DATE = "cooking_date";
    public static final String ST_TIME = "cooking_time";
    public static final String ST_BURNER = "cooking_burner";
    public static final String ST_ANGLE = "cooking_angle";
    public static final String ST_DURATION = "cooking_duration";
    public static final String ST_COOKING_STATUS = "cooking_status";


    public static final String SIGNUP_TABLE = "signuptable";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_MOBILE = "user_mobile";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_ADDRESS = "user_address";
    public static final String USER_CREATION_DATE = "user_creation_date";

    public static final String GCP_TABLE = "gasconsumptionpattern";
    public static final String GCP_BURNER = "gcp_burner";
    public static final String GCP_USAGE_DATE = "gcp_usage_date";
    public static final String GCP_USAGE_VALUE = "gcp_usage_value";


    Context mCtx;

    public SqliteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mCtx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT add_cart_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + MISSUE_ID + " varchar(200) NOT NULL,\n" +
                "    " + FIXED_DATE + " tinyint(4) NOT NULL,\n" +
                "    " + PERSON_NAME + " varchar(200) NOT NULL,\n" +
                "    " + ISSUE + " varchar(200) NOT NULL,\n" +
                "    " + DEVICE_ID + " varchar(200) NOT NULL\n" +
                ");";


        String statisticsTable = "CREATE TABLE IF NOT EXISTS " + ST_TABLE_NAME + "(\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT add_cart_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + ST_COOKING_ID + " varchar(200) NOT NULL,\n" +
                "    " + ST_DATE + " tinyint(4) NOT NULL,\n" +
                "    " + ST_TIME + " varchar(200) NOT NULL,\n" +
                "    " + ST_BURNER + " varchar(200) NOT NULL,\n" +
                "    " + ST_ANGLE + " varchar(200) NOT NULL\n," +
                "    " + ST_DURATION + " varchar(200) NOT NULL\n," +
                "    " + ST_COOKING_STATUS + " varchar(200) NOT NULL\n," +
                "    " + DEVICE_ID + " varchar(200) NOT NULL\n" +
                ");";*/

        String gasConPatTable = "CREATE TABLE IF NOT EXISTS " + GCP_TABLE + "(\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT add_cart_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + GCP_BURNER + " varchar(200) NOT NULL,\n" +
                "    " + GCP_USAGE_VALUE + " INTEGER NOT NULL,\n" +
                "    " + GCP_USAGE_DATE + " text NOT NULL\n" +
                ");";

        String signUpTable = "CREATE TABLE IF NOT EXISTS " + SIGNUP_TABLE + "(\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT add_cart_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + USER_NAME + " varchar(200) NOT NULL,\n" +
                "    " + USER_EMAIL + " tinyint(4) NOT NULL,\n" +
                "    " + USER_MOBILE + " varchar(200) NOT NULL,\n" +
                "    " + USER_PASSWORD + " varchar(200) NOT NULL,\n" +
                "    " + USER_ADDRESS + " varchar(200) NOT NULL\n," +
                "    " + USER_CREATION_DATE + " varchar(200) NOT NULL\n" +
                ");";


        /*db.execSQL(sql);
        db.execSQL(statisticsTable);*/

        db.execSQL(gasConPatTable);

        db.execSQL(signUpTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addGasConsumptionPattern(Date date, int gasValue, String burner) {

        System.out.println("ReceivedValueInSqliteDB" + date + " " + gasValue + " " + burner);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //Date date1=simpleDateFormat.parse(date);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(GCP_BURNER, burner);
        contentValues.put(GCP_USAGE_VALUE, gasValue);
        contentValues.put(GCP_USAGE_DATE, simpleDateFormat.format(date));

        return sqLiteDatabase.insert(GCP_TABLE, null, contentValues) != -1;

    }

    public List<GasConsumptionPatternDTO> searchByDates(String burner, String startDate, String endDate) {


        System.out.println("StartDate " + startDate + " " + "EndDate" + endDate);

        /*String sDate = "01/11/2020";
        String eDate = "10/11/2020";*/

        List<GasConsumptionPatternDTO> gasConsumptionPatternDTOList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();


        /*Cursor cursor = sqLiteDatabase.rawQuery("select id,gcp_burner,gcp_usage_value,gcp_usage_date from " + GCP_TABLE + " where " + GCP_USAGE_DATE + ">=? and " + GCP_USAGE_DATE + "<=?",
                new String[]{sDate, eDate});*/

        Cursor cursor = sqLiteDatabase.rawQuery("select id,gcp_burner,gcp_usage_value,gcp_usage_date from " + GCP_TABLE + " where " + GCP_USAGE_DATE + ">=? and " + GCP_USAGE_DATE + "<=? and " + GCP_BURNER + "=?",
                new String[]{startDate, endDate, burner});

        if (cursor.moveToFirst()) {

            do {


                System.out.println("RrangeID " + cursor.getInt(0));
                System.out.println("RrangeBURNER " + cursor.getString(1));
                System.out.println("RrangeUSAGE " + cursor.getInt(2));
                System.out.println("DATE " + cursor.getString(3));

                GasConsumptionPatternDTO gasConsumptionPatternDTO = new GasConsumptionPatternDTO(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                gasConsumptionPatternDTOList.add(gasConsumptionPatternDTO);

            } while (cursor.moveToNext());


        }

        return gasConsumptionPatternDTOList;

    }

    public List<GasConsumptionPatternDTO> searchByBurner(String burner) {

        List<GasConsumptionPatternDTO> gasConsumptionPatternDTOList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy");
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select id,gcp_burner,gcp_usage_value,gcp_usage_date from gasconsumptionpattern where gcp_burner=?", new String[]{burner});

        if (cursor.moveToFirst()) {

            do {

                /*System.out.println("ID " + cursor.getInt(0));
                System.out.println("BURNER " + cursor.getString(1));
                System.out.println("USAGE " + cursor.getInt(2));
                System.out.println("DATE " + cursor.getString(3));*/

                GasConsumptionPatternDTO gasConsumptionPatternDTO = new GasConsumptionPatternDTO(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                gasConsumptionPatternDTOList.add(gasConsumptionPatternDTO);
            } while (cursor.moveToNext());


        }

        return gasConsumptionPatternDTOList;

    }


    public boolean addUser(String userName, String userEmail, String mobileNumber, String userPassword) {

        String userAddress="address";

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        /*try {*/

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_EMAIL, userEmail);
        contentValues.put(USER_MOBILE, mobileNumber);
        contentValues.put(USER_PASSWORD, userPassword);
        contentValues.put(USER_ADDRESS, userAddress);
        contentValues.put(USER_CREATION_DATE, MathUtil.dateAndTime());

        return sqLiteDatabase.insert(SIGNUP_TABLE, null, contentValues) != -1;

    }


    public boolean addMaintenanceServiceData(String m_issueID, String issueFixedDate, String personName, String issue, String deviceID) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        /*try {*/

        ContentValues contentValues = new ContentValues();

        contentValues.put(MISSUE_ID, m_issueID);
        contentValues.put(FIXED_DATE, issueFixedDate);
        contentValues.put(PERSON_NAME, personName);
        contentValues.put(ISSUE, issue);
        contentValues.put(DEVICE_ID, deviceID);

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) != -1;

    }

    public List<MaintenaceServiceDTO> getAllM_ServiceData(String mDeviceId) {

        List<MaintenaceServiceDTO> maintenaceServiceDTOList = new ArrayList<>();

        SQLiteDatabase selectAllData = getReadableDatabase();

        Cursor cursor = selectAllData.rawQuery("select missue_id,fixed_date,mperson,issue,deviceid from maintenaceservice where deviceid=?", new String[]{mDeviceId});


        if (cursor.moveToFirst()) {

            do {

                MaintenaceServiceDTO maintenaceServiceDTO = new MaintenaceServiceDTO(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

                maintenaceServiceDTOList.add(maintenaceServiceDTO);

            }
            while (cursor.moveToNext());

        }


        return maintenaceServiceDTOList;


    }


    public boolean addStatisticsBurnerValue(String cooking_ID, String date, String time, String burner, String angle, String duration, String cookingStatus, String deviceID) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ST_COOKING_ID, cooking_ID);
        contentValues.put(ST_DATE, date);
        contentValues.put(ST_TIME, time);
        contentValues.put(ST_BURNER, burner);
        contentValues.put(ST_ANGLE, angle);
        contentValues.put(ST_DURATION, duration);
        contentValues.put(ST_COOKING_STATUS, cookingStatus);
        contentValues.put(DEVICE_ID, deviceID);

        return sqLiteDatabase.insert(ST_TABLE_NAME, null, contentValues) != -1;

    }

    public List<StatisticsDTO> getBurnerStatisticsReport(String burnerNumber, String deviceID) {

        //Has unique cooking ID
        List<StatisticsDTO> statisticsDTOSList = new ArrayList<>();

        burnerNumber = "1";

        SQLiteDatabase selectAllData = getReadableDatabase();

        Cursor cursor = selectAllData.rawQuery("select DISTINCT cooking_id from statisticsreport where deviceid=?", new String[]{deviceID});

        if (cursor.moveToFirst()) {

            do {

                StatisticsDTO statisticsDTO = new StatisticsDTO(cursor.getString(0));
                statisticsDTOSList.add(statisticsDTO);

            }
            while (cursor.moveToNext());

        }

        List<StatisticsDTO> reportList = new ArrayList<>();

        for (int i = 0; i < statisticsDTOSList.size(); i++) {

            String cookingID = statisticsDTOSList.get(i).getCookingID();

            String startTime = getStartTimeOfCookingID(cookingID, burnerNumber, deviceID);

            String[] end_duration = getEndTimeOfCookingID(cookingID, burnerNumber, deviceID);

            StatisticsDTO statisticsDTO = new StatisticsDTO(end_duration[2], startTime, end_duration[0], end_duration[1]);

            reportList.add(statisticsDTO);


        }

        return reportList;


    }

    public String validateLoginUser(String userEmail, String password) {

        String username = "empty";

        SQLiteDatabase selectAllData = getReadableDatabase();

        Cursor userData = selectAllData.rawQuery("select id,user_name from signuptable where (user_email=? or user_mobile=?) and user_password=? ", new String[]{userEmail,userEmail, password});

        if (userData.moveToFirst()) {

            do {

                int id = userData.getInt(0);
                System.out.println("ReceivedUserId" + id);

                PreferencesUtil.setValueSInt(mCtx, PreferencesUtil.USER_ID, id);
                username = userData.getString(1);


            }
            while (userData.moveToNext());

        }

        return username;
    }

    public boolean resetPassword(String userEmail,String password){

        SQLiteDatabase updateSqLiteDatabase = getWritableDatabase();

        ContentValues updateContentValues = new ContentValues();

        updateContentValues.put(USER_PASSWORD, password);

        return updateSqLiteDatabase.update(TABLE_NAME, updateContentValues, USER_EMAIL + "=?", new String[]{userEmail}) > 0;

    }


    private String getStartTimeOfCookingID(String cookingID, String burnerNumber, String deviceID) {

        String startTime = "";

        SQLiteDatabase selectAllData = getReadableDatabase();

        Cursor StartTime = selectAllData.rawQuery("select cooking_time from statisticsreport where cooking_status=? and cooking_id=? and cooking_burner=? and deviceid=?", new String[]{"0", cookingID, burnerNumber, deviceID});


        if (StartTime.moveToFirst()) {

            do {

                startTime = StartTime.getString(0);

            }
            while (StartTime.moveToNext());

        }


        return startTime;


    }

    private String[] getEndTimeOfCookingID(String cookingID, String burnerNumber, String deviceID) {

        String endTime;
        String duration;
        String cookingDate;

        String[] end_duration = new String[3];

        SQLiteDatabase selectAllDatas = getReadableDatabase();

        Cursor endTimeCurser = selectAllDatas.rawQuery("select cooking_time,cooking_duration,cooking_date from statisticsreport where cooking_status=? and cooking_id=? and cooking_burner=? and deviceid=?", new String[]{"2", cookingID, burnerNumber, deviceID});

        if (endTimeCurser.moveToFirst()) {

            do {

                endTime = endTimeCurser.getString(0);
                duration = endTimeCurser.getString(1);
                cookingDate = endTimeCurser.getString(2);

                end_duration[0] = endTime;
                end_duration[1] = duration;
                end_duration[2] = cookingDate;

            }
            while (endTimeCurser.moveToNext());

        }

        return end_duration;

    }


}
