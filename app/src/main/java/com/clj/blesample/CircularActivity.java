package com.clj.blesample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;
import com.triggertrap.seekarc.SeekArc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class CircularActivity extends AppCompatActivity {


    LineChartView lineChartView;

    String[] xAxisDate = {"10/11", "11/11", "12/11", "13/11", "14/11", "15/11", "16/11"};

    double[] yAxisValue = {0.404, 0.505, 0.606, 0.702, 0.801, 0.903, 0.104};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);

        lineChartView = findViewById(R.id.chartLine);



        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues);

        for (int i = 0; i < xAxisDate.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(xAxisDate[i]));
        }


        for (int i = 0; i < yAxisValue.length; i++) {
            yAxisValues.add(new PointValue(i, (float) yAxisValue[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        lineChartView.setLineChartData(data);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setHasSeparationLine(true);

        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);

        line.setColor(Color.parseColor("#d32f2f"));

        axis.setTextSize(16);

        axis.setTextColor(Color.parseColor("#03A9F4"));

        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);

        yAxis.setName("GasUsage in Grams");

        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());

        viewport.top = 2;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);







    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    class UpdateTimeTask extends TimerTask {
        public void run() {

            //hide your layout

        }

    }
}