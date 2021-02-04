package com.clj.blesample.menuoperationactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import com.clj.blesample.R;

public class GetInTouchActivity extends AppCompatActivity {


    TextView text2,text3,text6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_service);

        text2=(TextView)findViewById(R.id.text2);
        text3=(TextView)findViewById(R.id.text3);
        text6=(TextView)findViewById(R.id.text6);

        Linkify.addLinks(text2,Linkify.ALL);
        text2.setLinkTextColor(getResources().getColor(R.color.white));

        Linkify.addLinks(text3,Linkify.ALL);
        text3.setLinkTextColor(getResources().getColor(R.color.white));


        Linkify.addLinks(text6,Linkify.ALL);
        text6.setLinkTextColor(getResources().getColor(R.color.white));


    }
}