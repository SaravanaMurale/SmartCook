package com.clj.blesample.menuoperationactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.model.GasConsumptionPatternDTO;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
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

    @SuppressLint("NewApi")
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

        String[] fromDataMonth = selectedFromDate.split("/");
        String[] toDataMonth = selectedToDate.split("/");

        String from=Month.of(Integer.parseInt(fromDataMonth[1])).name();
        String to=Month.of(Integer.parseInt(toDataMonth[1])).name();

        System.out.println("FromDateMonth"+from+" "+"ToDateMonth"+to);


        List<GasConsumptionPatternDTO> gasConsumptionPatternDTOList=new ArrayList<>();

        if(selectedBurner.equals("11")){
            System.out.println("AllBurnerIsSelected");
            List<GasConsumptionPatternDTO> rightBurnerList=  sqliteManager.allBurnerDataByDate("00",selectedFromDate,selectedToDate);
            List<GasConsumptionPatternDTO> leftBurnerList=sqliteManager.allBurnerDataByDate("01",selectedFromDate,selectedToDate);
            List<GasConsumptionPatternDTO> centerBurnerList=sqliteManager.allBurnerDataByDate("10",selectedFromDate,selectedToDate);

            if(rightBurnerList.size()!=0 && leftBurnerList.size()!=0&& centerBurnerList.size()!=0 ) {


                for (int i = 0; i < rightBurnerList.size(); i++) {

                    int k = rightBurnerList.get(i).getGasUsage() + leftBurnerList.get(i).getGasUsage() + centerBurnerList.get(i).getGasUsage();

                    GasConsumptionPatternDTO gasConsumptionPatternDTO = new GasConsumptionPatternDTO(k, rightBurnerList.get(i).getGasUsageDate());

                    gasConsumptionPatternDTOList.add(gasConsumptionPatternDTO);


                }
            }else {
                Toast.makeText(GraphViewActivity.this,"There is no data for the selected Date",Toast.LENGTH_LONG).show();
                finish();

            }


        }else {
            gasConsumptionPatternDTOList = sqliteManager.searchByDates(selectedBurner, selectedFromDate, selectedToDate);
        }


        if(gasConsumptionPatternDTOList.size()==0 ){
            Toast.makeText(GraphViewActivity.this,"There is no data for the selected Date",Toast.LENGTH_LONG).show();

            return;
        }


        //System.out.println("RangeSizeOfGasConsumptionPatters " + gasConsumptionPatternDTOList.size());

       /* for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {

            System.out.println("DataGCPData " + gasConsumptionPatternDTOList.get(i).getGasUsageDate());
            System.out.println("DataGCPData " + gasConsumptionPatternDTOList.get(i).getGasUsage());


        }*/

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues);

        /*for (int i = 0; i < xAxisDate.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(xAxisDate[i]));
        }*/
        for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {

            String date=gasConsumptionPatternDTOList.get(i).getGasUsageDate();

            //System.out.println("DateToSplit "+date);

            String[] day = date.split("/");
            System.out.println("DayAlone "+day[0]);

            //Only Day
            axisValues.add(i, new AxisValue(i).setLabel(day[0]));

            //Full Date
            //axisValues.add(i, new AxisValue(i).setLabel(gasConsumptionPatternDTOList.get(i).getGasUsageDate()));
        }

        /*for (int i = 0; i < yAxisValue.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisValue[i]));
        }*/

        List<Integer> findMaxValue=new ArrayList<>();
        for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {
            yAxisValues.add(new PointValue(i, gasConsumptionPatternDTOList.get(i).getGasUsage()));

            findMaxValue.add(gasConsumptionPatternDTOList.get(i).getGasUsage());
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

        yAxis.setName("GasUsage(gms)");
        axis.setName("Date");

        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        int max= Collections.max(findMaxValue);
        viewport.top = max+10;
        //viewport.top = 50;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}