package com.clj.blesample;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.model.GasConsumptionPatternDTO;
import com.sdsmdg.harjot.crollerTest.Croller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DummyActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    float value;

    TextView textView;

    EditText editText;
    Button saveData, loadData;
    public static final String FILE_NAME = "scsk.txt";
    FileOutputStream fos;

    Button moveToFragment, saveInSqlite, searchByBurner, searchByDate;

    List<String> burnerList = new ArrayList<>();
    List<String> listInString = new ArrayList<>();
    List<Integer> gasUsageLisst = new ArrayList<>();
    String date1 = "1/11/2020";
    String date2 = "2/11/2020";
    String date3 = "3/11/2020";
    String date4 = "4/11/2020";
    String date5 = "5/11/2020";
    String date6 = "6/11/2020";
    String date7 = "7/11/2020";
    String date8 = "8/11/2020";
    String date9 = "9/11/2020";
    String date10 = "10/11/2020";

    SqliteManager sqliteManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dummy);

        sqliteManager = new SqliteManager(DummyActivity.this);

        gasUsageList();

        usageDateList();

        burnerList();




        /*editText = (EditText) findViewById(R.id.editText);
        saveData = (Button) findViewById(R.id.saveData);
        loadData = (Button) findViewById(R.id.loadData);*/


        moveToFragment = (Button) findViewById(R.id.moveToFragment);
        saveInSqlite = (Button) findViewById(R.id.saveInSqlite);
        searchByBurner = (Button) findViewById(R.id.searchByBurner);
        searchByDate = (Button) findViewById(R.id.searchByDate);

        searchByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<GasConsumptionPatternDTO> gasConsumptionPatternDTOList = sqliteManager.searchByDates("00", "01/11/2020", "10/11/2020");
                for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {

                    System.out.println("RangeSizeOfGasConsumptionPatters " + gasConsumptionPatternDTOList.size());


                }
            }
        });

        searchByBurner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GasConsumptionPatternDTO> gasConsumptionPatternDTOList = sqliteManager.searchByBurner("00");

                for (int i = 0; i < gasConsumptionPatternDTOList.size(); i++) {

                    System.out.println("SizeOfGasConsumptionPatters " + gasConsumptionPatternDTOList.size());

                }

            }
        });

        saveInSqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDataToSqliteDB();

            }
        });

        moveToFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                moveToFragment.setVisibility(View.GONE);

                Fragment fragment = new DummyFragment1();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer1, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        /*GradientDrawable bgShape = (GradientDrawable) round.getBackground();
        bgShape.setColor(Color.BLACK);*/


      /*  saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*try {
                    fos=new FileOutputStream(FILE_NAME,true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*//*

                try {
                    fos = openFileOutput(FILE_NAME, MODE_APPEND);
                    fos.write(editText.getText().toString().getBytes());

                    editText.getText().clear();

                    System.out.println("FileDirectory " + getFilesDir());
                    System.out.println("WrittenSuccessfully");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        });


        loadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fis = null;

                try {
                    fis = openFileInput(FILE_NAME);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sbr = new StringBuilder();
                    String txt;

                    while ((txt = br.readLine()) != null) {

                        sbr.append(txt).append("\n");

                    }

                    editText.setText(sbr.toString());
                    Toast.makeText(DummyActivity.this, sbr.toString(), Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
*/

    }

    private void burnerList() {

        burnerList.add(0, "00");
        burnerList.add(1, "01");
        burnerList.add(2, "10");
        burnerList.add(3, "00");
        burnerList.add(4, "01");
        burnerList.add(5, "10");
        burnerList.add(6, "00");
        burnerList.add(7, "01");
        burnerList.add(8, "10");
        burnerList.add(9, "00");

    }

    private void usageDateList() {
        /*Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        System.out.println(sDate1 + "\t" + date1);

        dateList.add();*/

        listInString.add(0, date1);
        listInString.add(1, date2);
        listInString.add(2, date3);
        listInString.add(3, date4);
        listInString.add(4, date5);
        listInString.add(5, date6);
        listInString.add(6, date7);
        listInString.add(7, date8);
        listInString.add(8, date9);
        listInString.add(9, date10);


    }

    private void gasUsageList() {

        gasUsageLisst.add(0, 10);
        gasUsageLisst.add(1, 2);
        gasUsageLisst.add(2, 8);
        gasUsageLisst.add(3, 6);
        gasUsageLisst.add(4, 4);
        gasUsageLisst.add(5, 8);
        gasUsageLisst.add(6, 1);
        gasUsageLisst.add(7, 5);
        gasUsageLisst.add(8, 7);
        gasUsageLisst.add(9, 2);

    }


    private void saveDataToSqliteDB() {

        for (int i = 0; i < listInString.size(); i++) {

            try {
                Date dateFormet = new SimpleDateFormat("dd/MM/yyyy").parse(listInString.get(i));

                System.out.println("StringToDateFormet" + dateFormet);
                System.out.println("GasUsageValue" + gasUsageLisst.get(i));

                sqliteManager.addGasConsumptionPattern(dateFormet, gasUsageLisst.get(i), burnerList.get(i));

                Toast.makeText(DummyActivity.this, "Value Inserted " + i, Toast.LENGTH_LONG).show();

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }


}



