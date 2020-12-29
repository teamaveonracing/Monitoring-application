package com.prince.authentication_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    int mYCoordinate;
    String dataId;
    LineDataSet lineDataSet = new LineDataSet(null,null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;
    LineChart lineChart;
    boolean flag_run_task = true;
    private Handler mHandler = new Handler();
    ArrayList<Entry> dynamicDataEntry = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        lineChart = findViewById(R.id.line_chart_view);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("ChartValues");
        insertInitialValue();
        performStuff();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    DataSnapshot data = dataSnapshot.child(dataId);
                    DataPoint dataPoint = data.getValue(DataPoint.class);
                    mYCoordinate = dataPoint.getyCoordinate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    void insertInitialValue(){
        int i = 5;
        dataId = mDatabaseReference.push().getKey();
        DataPoint dataPoint = new DataPoint(i);
        mDatabaseReference.child(dataId).setValue(dataPoint);

    }
    void showChart(ArrayList<Entry> dataVals){
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("Dataset1");
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();

    }
    private void performStuff(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for( int i=0 ; i<500 ; i++){
                    final int xCoordinate = i;
                    if(!flag_run_task) break;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            dynamicDataEntry.add(new Entry(xCoordinate,mYCoordinate));
                            if(xCoordinate>10){
                                dynamicDataEntry.remove(0);
                            }
                            showChart(dynamicDataEntry);

                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        flag_run_task = false;
    }
}
