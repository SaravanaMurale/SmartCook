package com.clj.blesample.utils;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class AnimationUtil {

    public static void  startBlinking(Context context, ImageView bluetoothIcon){

        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        bluetoothIcon.startAnimation(animation);

    }

    public static void stopBlinking(Context context,ImageView bluetoothIcon) {
        bluetoothIcon.clearAnimation();
    }

}
