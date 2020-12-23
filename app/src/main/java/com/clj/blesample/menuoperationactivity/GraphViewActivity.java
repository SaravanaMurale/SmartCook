package com.clj.blesample.menuoperationactivity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clj.blesample.R;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.model.GasConsumptionPatternDTO;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class GraphViewActivity extends AppCompatActivity {

    LineChartView lineChartView;

    String[] xAxisDate = {"10/11", "11/11", "12/11", "13/11", "14/11", "15/11", "16/11"};

    int[] yAxisValue = {80, 30, 70, 20, 15, 50, 10};

    SqliteManager sqliteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);

        lineChartView = findViewById(R.id.chart);
        sqliteManager = new SqliteManager(GraphViewActivity.this);

        Intent intent = getIntent();
        String selectedFromDate = intent.getStringExtra("FROMDATE");
        String selectedToDate = intent.getStringExtra("TODATE");
        String selectedBurner = intent.getStringExtra("BURNER");

        System.out.println("SelectedData " + selectedFromDate + " " + selectedToDate + " " + selectedBurner);

        List<GasConsumptionPatternDTO> gasConsumptionPatternDTOList = sqliteManager.searchByDates(selectedBurner, selectedFromDate, selectedToDate);

        System.out.println("RangeSizeOfGasConsumptionPatters " + gasConsumptionPatternDTOList.size());

        for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {

            System.out.println("DataGCPData " + gasConsumptionPatternDTOList.get(i).getGasUsageDate());
            System.out.println("DataGCPData " + gasConsumptionPatternDTOList.get(i).getGasUsage());


        }

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues);

        /*for (int i = 0; i < xAxisDate.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(xAxisDate[i]));
        }*/
        for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {
            axisValues.add(i, new AxisValue(i).setLabel(gasConsumptionPatternDTOList.get(i).getGasUsageDate()));
        }

        /*for (int i = 0; i < yAxisValue.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisValue[i]));
        }*/

        for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {
            yAxisValues.add(new PointValue(i, gasConsumptionPatternDTOList.get(i).getGasUsage()));
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
        viewport.top = 10;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}