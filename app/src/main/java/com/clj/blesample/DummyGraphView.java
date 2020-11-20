package com.clj.blesample;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.model.GasConsumptionPatternDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class DummyGraphView extends AppCompatActivity {

    LineChartView lineChartView;

    String[] axisData = {"10/11", "11/11", "12/11", "13/11", "14/11", "15/11", "16/11"};

    int[] yAxisData = {80, 30, 70, 20, 15, 50, 10};

    SqliteManager sqliteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_graph_view);

        lineChartView = findViewById(R.id.chart);
        sqliteManager = new SqliteManager(DummyGraphView.this);

        Intent intent = getIntent();
        String selectedFromDate = intent.getStringExtra("FROMDATE");
        String selectedToDate = intent.getStringExtra("TODATE");
        String selectedBurner = intent.getStringExtra("BURNER");

        List<GasConsumptionPatternDTO> gasConsumptionPatternDTOList = sqliteManager.searchByDates(selectedBurner,selectedFromDate,selectedToDate );
        for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {

            System.out.println("RangeSizeOfGasConsumptionPatters " + gasConsumptionPatternDTOList.size());


        }

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues);

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
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

        line.setColor(Color.parseColor("#9C27B0"));

        axis.setTextSize(16);

        axis.setTextColor(Color.parseColor("#03A9F4"));

        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);

        yAxis.setName("GasUsage in KG");

        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}