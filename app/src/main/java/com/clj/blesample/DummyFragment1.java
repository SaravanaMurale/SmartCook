package com.clj.blesample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.menuoperationactivity.EditActivity;
import com.clj.blesample.menuoperationactivity.MenuActivity;


public class DummyFragment1 extends Fragment {


    ImageView leftBurner, leftBurnerVessel, leftBurnerTimer, leftBurnerWhistle, leftBurnerVesselEmpty;
    Button leftBurnerSetting, leftBurnerEdit;

    ImageView centerBurner, centerBurnerVessel, centerBurnerTimer, centerBurnerWhistle, centerBurnerVesselEmpty;
    Button centerBurnerSetting, centerBurnerEdit;

    ImageView rightBurner, rightBurnerVessel, rightBurnerTimer, rightBurnerWhistle, rightBurnerVesselEmpty;
    Button rightBurnerSetting, rightBurnerEdit;

    LinearLayout leftBurnerWidgetBlock, centerBurnerWidgetBlock, rightBurnerWidgetBlock;

    TextView leftBurnerTime, centerBurnerTime, rightBurnerTime;

    byte[] burnerValue = new byte[12];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dummy, container, false);

        //initView(view);

        //setValueToBurner();


        return view;
    }

    private void setValueToBurner() {

        //Left
        burnerValue[0] = 1; //active
        burnerValue[1] = 0; //vessel
        burnerValue[2] = 1; //timer
        burnerValue[3] = 1; //whistle

        //Center
        burnerValue[4] = 0;
        burnerValue[5] = 0;
        burnerValue[6] = 0;
        burnerValue[7] = 0;

        //Right
        burnerValue[8] = 1;
        burnerValue[9] = 0;
        burnerValue[10] = 0;
        burnerValue[11] = 0;

        if (burnerValue[0] == 1 && burnerValue[1] == 1) {
            //leftBurner.setBackground(getResources().getDrawable(R.drawable.burner_with_vessel));
            leftBurner.setImageDrawable(getResources().getDrawable(R.drawable.burner_with_vessel));
        } else if (burnerValue[0] == 1 && burnerValue[1] == 0) {
            leftBurner.setImageDrawable(getResources().getDrawable(R.drawable.burner_without_vessel));
        } else if (burnerValue[0] == 0 && burnerValue[1] == 0) {
            leftBurner.setImageDrawable(getResources().getDrawable(R.drawable.burner));
        }

        if (burnerValue[2] == 1) {

            leftBurnerTime.setText("10 Min");
            leftBurnerTimer.setVisibility(View.VISIBLE);
            blinkImage(leftBurnerTimer);

        } else {

            leftBurnerTime.setText("");
            leftBurnerTimer.setVisibility(View.INVISIBLE);
            stopImageBlinking(leftBurnerTimer);
        }

        if (burnerValue[3] == 1) {

            Toast.makeText(getActivity(), "whistle set", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "whistle is not set", Toast.LENGTH_LONG).show();
            //leftBurner.setText("");
        }


    }

    private void blinkImage(ImageView timerIcon) {
        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        timerIcon.startAnimation(animation);
    }

    private void stopImageBlinking(ImageView topTimerIcon) {
        topTimerIcon.clearAnimation();
    }

    private void initView(View view) {

       // leftBurnerSetting = (Button) view.findViewById(R.id.leftBurnerSetting);

        /*leftBurner = (ImageView) view.findViewById(R.id.leftBurner);
        leftBurnerVessel = (ImageView) view.findViewById(R.id.leftBurnerVessel);
        leftBurnerTime = (TextView) view.findViewById(R.id.leftBurnerTime);
        leftBurnerTimer = (ImageView) view.findViewById(R.id.leftBurnerTimer);
        leftBurnerWhistle = (ImageView) view.findViewById(R.id.leftBurnerWhistle);
        leftBurnerVesselEmpty = (ImageView) view.findViewById(R.id.leftBurnerVesselEmpty);


        leftBurnerEdit = (Button) view.findViewById(R.id.leftBurnerEdit);


        centerBurner = (ImageView) view.findViewById(R.id.centerBurner);
        centerBurnerVessel = (ImageView) view.findViewById(R.id.centerBurnerVessel);
        centerBurnerTime = (TextView) view.findViewById(R.id.centerBurnerTime);
        centerBurnerTimer = (ImageView) view.findViewById(R.id.centerBurnerTimer);
        centerBurnerWhistle = (ImageView) view.findViewById(R.id.centerBurnerWhistle);
        centerBurnerVesselEmpty = (ImageView) view.findViewById(R.id.centerBurnerVesselEmpty);

        centerBurnerSetting = (Button) view.findViewById(R.id.centerBurnerSetting);
        centerBurnerEdit = (Button) view.findViewById(R.id.centerBurnerEdit);

        rightBurner = (ImageView) view.findViewById(R.id.rightBurner);
        rightBurnerVessel = (ImageView) view.findViewById(R.id.rightBurnerVessel);
        rightBurnerTime = (TextView) view.findViewById(R.id.rightBurnerTime);
        rightBurnerTimer = (ImageView) view.findViewById(R.id.rightBurnerTimer);
        rightBurnerWhistle = (ImageView) view.findViewById(R.id.rightBurnerWhistle);
        rightBurnerVesselEmpty = (ImageView) view.findViewById(R.id.rightBurnerVesselEmpty);

        rightBurnerSetting = (Button) view.findViewById(R.id.rightBurnerSetting);
        rightBurnerEdit = (Button) view.findViewById(R.id.rightBurnerEdit);

        leftBurnerWidgetBlock = (LinearLayout) view.findViewById(R.id.leftBurnerWidgetsBlock);
        centerBurnerWidgetBlock = (LinearLayout) view.findViewById(R.id.centerBurnerWidgetsBlock);
        rightBurnerWidgetBlock = (LinearLayout) view.findViewById(R.id.rightBurnerWidgetsBlock);
*/
        leftBurnerSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDummyFragment2();
            }
        });


    }

    private void launchMenuActivity() {

        Intent intent = new Intent(this.getActivity(), MenuActivity.class);
        startActivity(intent);

    }

    private void callDummyFragment2() {

        Intent intent = new Intent(getActivity(), EditActivity.class);
        startActivity(intent);

        /*Fragment fragment = new DummyFragment1();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

    }


}
