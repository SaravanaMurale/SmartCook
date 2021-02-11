package com.clj.blesample.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Patterns;

import com.clj.blesample.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MathUtil {


    public static final String RIGHT_BURNER = "00";
    public static final String LEFT_BURNER = "01";
    public static final String CENTER_BURNER = "10";

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1256;

    public static final int OFF = 0;
    public static final int HIGH = 1;
    public static final int SIM = 2;

    public static final int SET_TIMER_VISIBLE=1;
    public static final int SET_WHISTLE_VISIBLE=2;

    public static final int NOTI_PREFERENCE_ENABLE=1;
    public static final int NOTI_PREFERENCE_DISABLE=-1;


    //sets sim,high,off
    public static final int BURNER_FORMET = 1;
    //sets whistle and timer
    public static final int EDIT_FORMET = 2;

    public static final int IMAGE_PICK_CODE = 1000;
    public static final int PERMISSION_CODE = 1001;

    public static final String RBINA="Right Burner Is Not Activated";
    public static final String LBISA="Left Burner Is Not Activated";
    public static final String CBISA="Center Burner Is Not Activated";

    public static final String VNPL="Vessel Is Not Placed On Left Burner To Set Whistle";
    public static final String VNPC="Vessel Is Not Placed On Center Burner To Set Whistle";
    public static final String VNPR="Vessel Is Not Placed On Right Burner To Set Whistle";

    public static final String VNPLT="Vessel Is Not Placed On Left Burner To Set Timer";
    public static final String VNPCT="Vessel Is Not Placed On Center Burner To Set Timer";
    public static final String VNPRT="Vessel Is Not Placed On Right Burner To Set Timer";

    public static final String WASL="Whistle Is Already Set For Left Burner";
    public static final String WASR="Whistle Is Already Set For Right Burner";
    public static final String WASC="Whistle Is Already Set For Center Burner";

    public static final String CRITICALLY_LOW="Your Battery is Criticaly Low";

    public static final String WCR="Right Burner Whistle Is Completed";
    public static final String WCL="Left Burner Whistle Is Completed";
    public static final String WCC="Center Burner Whistle Is Completed";

    public static final String TCR="Right Burner Timer Is Completed";
    public static final String TCL="Left Burner Timer Is Completed";
    public static final String TCC="Center Burner Timer Is Completed";


    //public static final String

    //0- non read
    //1-read


    public static final String LEFT_VESSEL_0="Vessel is not placed on left burner";
    public static final String LEFT_VESSEL_1="Vessel is placed on left burner";
    public static final String LEFT_WHISTLE="Whistle is set on left burner";
    public static final String LEFT_TIMER="Timer is set on left burner";


    public static final String RIGHT_VESSEL_0="Vessel is not placed on right burner";
    public static final String RIGHT_VESSEL_1="Vessel is placed on right burner";
    public static final String RIGHT_WHISTLE="Whistle is set on right burner";
    public static final String RIGHT_TIMER="Timer is set on right burner";


    public static final String CENTER_VESSEL_0="Vessel is not placed on center burner";
    public static final String CENTER_VESSEL_1="Vessel is placed on center burner";
    public static final String CENTER_WHISTLE="Whistle is set on center burner";
    public static final String CENTER_TIMER="Timer is set on center burner";


    public static boolean validatePassword(String password) {
        if (password.length() < 6) {

            // ToastUtils.getInstance(SignUpActivity.this).showLongToast(R.string.short_password);
            return false;
        }
        return true;
    }

    public static boolean passwordMatch(String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            return false;
        }


        return true;

    }

    public static boolean validateLoginEmailOrPassword(String mobile) {

        if (mobile.length() < 5) {
            return false;
        }
        return true;
    }

    public static boolean validateMobileNumberLength(String mobile){

        if(mobile.length()<10){
            return false;
        }
        return true;

    }


    public static boolean validateMobileNumber(String mobile) {

        String regex = "(0/91)?[7-9][0-9]{9}";

       if(Patterns.PHONE.matcher(mobile).matches()){
           return true;
       }

       /* if (!mobile.matches(regex)) {
            return false;
        }*/
        return false;
    }



    public static boolean validateName(String name) {

        if (name.length() < 3) {
            return false;
        }

        return true;

    }

    public static boolean ValidateNameWithoutSplChar(String name){
        if(!name.matches("^[A-Za-z]+$") ){
            return false;
        }
        return true;
    }

    public static boolean validateSpaceInName(String name){

        if(name.contains(" ")){
            return false;
        }

        return true;
    }


    public static boolean validateAmount(String amount) {

        if (amount.length() < 0) {
            return false;
        }

        return true;

    }

    public static boolean validateAddress(String address) {
        if (address.length() < 5) {
            return false;
        }
        return true;
    }

    public static boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern) && email.length() > 0){
            return true;
        }

        return false ;
    }


    public static Typeface getOctinPrisonFont(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");

        return typeface;

    }

    public static Double stringToDouble(String stringVal) {

        Double d = Double.parseDouble(stringVal);

        return d;
    }

    public static String dateToStringConversion(Date dateInput) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = sdf.format(dateInput);
        return dateInString;
    }

    public static Date stringToDateConversion(String date) {
        Date actualDate = null;
        try {
            actualDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return actualDate;
    }

    public static String time() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date();
        String time = sdf.format(d);
        return time;
    }


    public static String dateAndTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d = new Date();
        String dateAndTime = sdf.format(d);

        return dateAndTime;
    }

    public static Dialog showProgressBar(Context context) {

        Dialog csprogress;
        csprogress = new Dialog(context, R.style.MyAlertDialogStyle);
        csprogress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.red(R.color.red)));
        csprogress.setCancelable(false);
        csprogress.setContentView(R.layout.layout_progressbar);
        csprogress.setCanceledOnTouchOutside(true);
        csprogress.show();
        return csprogress;
    }

    public static void dismisProgressBar(Context context,Dialog dialog) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }

public static double getScreenInches(Activity activity){
    DisplayMetrics dm = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
    double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
    double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
    double screenInches = Math.sqrt(x + y);

    System.out.println("ScreenInches"+screenInches);

    return screenInches;
}

    private long findDifferenceBetweenDates(String fromDate, String toDate) {

        long difference_In_Days = 0;

        try {

            Date fromDateFormet = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);
            Date toDateFormet = new SimpleDateFormat("dd/MM/yyyy").parse(toDate);


            System.out.println("StringToDateConversion" + fromDateFormet); // Sat Jan 02 00:00:00 GMT 2010
            System.out.println("StringToDateConversion" + toDateFormet);

            long difference_In_Time = toDateFormet.getTime() - fromDateFormet.getTime();

            difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            System.out.println("NumberOfDays" + difference_In_Days);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return difference_In_Days;

    }


}
