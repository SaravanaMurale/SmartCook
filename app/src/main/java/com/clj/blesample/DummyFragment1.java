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
import com.clj.blesample.menuoperationactivity.NotificationActivity;


public class DummyFragment1 extends Fragment {

    ImageView leftBurner;
    ImageView notificationIcon;

    TextView selectLeft,selectCenter,selectRight,selectSim,selectHigh,selectOff;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dummy, container, false);

       /* leftBurner = (ImageView) view.findViewById(R.id.leftBurner);
        notificationIcon = (ImageView) view.findViewById(R.id.notificationIcon);

        selectLeft=(TextView)view.findViewById(R.id.selectLeft);
        selectCenter=(TextView)view.findViewById(R.id.selectCenter);
        selectRight=(TextView)view.findViewById(R.id.selectRight);

        selectSim=(TextView)view.findViewById(R.id.selectSim);
        selectHigh=(TextView)view.findViewById(R.id.selectHigh);
        selectOff=(TextView)view.findViewById(R.id.selectOff);*/

/*
        selectLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectLeft.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
                selectCenter.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));
                selectRight.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));

            }
        });

        selectCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectCenter.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
                selectLeft.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));
                selectRight.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));

            }
        });

        selectRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectRight.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
                selectCenter.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));
                selectLeft.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));


            }
        });

        selectSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectSim.setBackgroundColor(getResources().getColor(R.color.burner_on_green));
                selectHigh.setBackground(getResources().getDrawable(R.drawable.rounded_border));
                selectOff.setBackground(getResources().getDrawable(R.drawable.rounded_border));
            }
        });

        selectHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectHigh.setBackgroundColor(getResources().getColor(R.color.burner_on_green));
                selectSim.setBackground(getResources().getDrawable(R.drawable.rounded_border));
                selectOff.setBackground(getResources().getDrawable(R.drawable.rounded_border));

            }
        });

        selectOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectOff.setBackgroundColor(getResources().getColor(R.color.burner_on_green));
                selectHigh.setBackground(getResources().getDrawable(R.drawable.rounded_border));
                selectSim.setBackground(getResources().getDrawable(R.drawable.rounded_border));


            }
        });



        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);

            }
        });

        leftBurner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivity(intent);


            }
        });*/


        return view;
    }


    private void launchMenuActivity() {

        Intent intent = new Intent(this.getActivity(), MenuActivity.class);
        startActivity(intent);

    }

    private void callDummyFragment2() {


        /*Fragment fragment = new DummyFragment1();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

    }


}
