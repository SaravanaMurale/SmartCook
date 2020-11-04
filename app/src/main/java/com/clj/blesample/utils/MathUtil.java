package com.clj.blesample.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MathUtil {

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

    public static String date() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        String date = sdf.format(d);
        return date;
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
