package com.clj.blesample.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.Settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FontUtil {

    public static Typeface getOctinPrisonFont(Context context){

        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"fonts/octin prison rg.ttf");

        return typeface;

    }

    public static String deviceID(Context context){

        String ANDROID_MOBILE_ID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return ANDROID_MOBILE_ID;

    }

    public static String timeVal(Context context){

        Date time=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(time);


        return currentDateTimeString;

    }

    public static String dateVal(Context context){

        Date date=new Date();
        SimpleDateFormat sDate=new SimpleDateFormat("dd-MM-yyyy");
        String formattedDAte = sDate.format(date);
        //System.out.println("DateAlone "+formattedDAte);

        return formattedDAte;
    }






}
