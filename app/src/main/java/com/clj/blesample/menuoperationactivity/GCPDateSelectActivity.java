package com.clj.blesample.menuoperationactivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.utils.MathUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class  GCPDateSelectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LineChart lineChart;

    GraphView graphView;


    String[] burners = {"Select Burner", "Center", "Left", "Right","All Burner"};
    Spinner spinner;

    DatePickerDialog.OnDateSetListener setListenerFromDate, setListenerToDate;
    TextView fromDate, toDate;
    Button submit;


    LineGraphSeries<DataPoint> series;
    SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
    private String selectedFromDate, selectedToDate, selectedBurner;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        lineChart = (LineChart) findViewById(R.id.lineChart);
        spinner = (Spinner) findViewById(R.id.burnerSpinner);

        fromDate = (TextView) findViewById(R.id.fromDate);
        toDate = (TextView) findViewById(R.id.toDate);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFromDate != null && selectedToDate != null && selectedBurner != null) {
                    Intent intent = new Intent(GCPDateSelectActivity.this, GraphViewActivity.class);
                    intent.putExtra("FROMDATE", selectedFromDate);
                    intent.putExtra("TODATE", selectedToDate);
                    intent.putExtra("BURNER", selectedBurner);
                    startActivity(intent);
                } else {
                    Toast.makeText(GCPDateSelectActivity.this, "Please Select", Toast.LENGTH_LONG).show();
                }


            }
        });


        spinner.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, burners);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);

        /*graphView = (GraphView) findViewById(R.id.graphView);
        series = new LineGraphSeries<>(getDataPoint());
        graphView.addSeries(series);*/


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DATE);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(GCPDateSelectActivity.this, android.R.style.Theme_Holo_Light_Dialog, setListenerFromDate, year, month, date);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(GCPDateSelectActivity.this, android.R.style.Theme_Holo_Light_Dialog, setListenerToDate, year, month, date);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        setListenerFromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                fromDate.setText(date);

                Date dat=MathUtil.stringToDateConversion(date);
                String stringFromDate=MathUtil.dateToStringConversion(dat);

                selectedFromDate = stringFromDate;

                System.out.println("selectedFromDate " + stringFromDate);


            }
        };


        setListenerToDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                toDate.setText(date);

                Date dat=MathUtil.stringToDateConversion(date);
                String stringToDate=MathUtil.dateToStringConversion(dat);

                selectedToDate = stringToDate;

                System.out.println("selectedToDate " + selectedToDate);

            }
        };


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String burner = parent.getItemAtPosition(position).toString();
        System.out.println("SelectedBurners" + burner);

        if (burner.equals("Left")) {
            selectedBurner = "01";
        } else if (burner.equals("Center")) {
            selectedBurner = "10";
        } else if (burner.equals("Right")) {
            selectedBurner = "00";
        }else if(burner.equals("All Burner")){
            selectedBurner="11";
        }

        //selectedBurner = burner;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

