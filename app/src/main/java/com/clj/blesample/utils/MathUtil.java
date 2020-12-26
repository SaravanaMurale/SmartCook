package com.clj.blesample.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MathUtil {


    public static final String RIGHT_BURNER = "00";
    public static final String LEFT_BURNER = "01";
    public static final String CENTER_BURNER = "10";

    public static final int SIM = 1;
    public static final int HIGH = 2;
    public static final int OFF = 3;

    public static final int BURNER_FORMET = 1;
    public static final int EDIT_FORMET = 2;

    public static final int IMAGE_PICK_CODE = 1000;
    public static final int PERMISSION_CODE = 1001;

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

    public static boolean validateMobile(String mobile) {

        if (mobile.length() < 10) {
            return false;
        }
        return true;
    }


    public static boolean validateName(String name) {

        if (name.length() < 3) {
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

        if (email.length() < 5) {
            return false;
        }

        return true;
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


}
