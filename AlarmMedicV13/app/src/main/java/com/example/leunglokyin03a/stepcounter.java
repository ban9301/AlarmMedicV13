package com.example.leunglokyin03a;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leunglokyin03a.MyDBHelper.MyDbAdapter;

public class stepcounter extends AppCompatActivity implements SensorEventListener {
    private TextView textViewStepCounter, textViewStepDetector;
    private SensorManager sensorManager;
    private Sensor mStepCounter , mStepDetector;

    private boolean isCounterSensorPresent, isDetectorSensorPresent;
    int stepCount = 0, stepDetect = 0;
    MyDbAdapter MyDbAdapter;
    int stepid;
    String totalsteps,stepsnow;
    int count2;
    private LinearLayout ml;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stepcounter);
        MyDbAdapter = new MyDbAdapter(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        textViewStepCounter = findViewById(R.id.textViewStepCounter);
        ml = findViewById(R.id.ml);
        textViewStepDetector = findViewById(R.id.textViewStepDector);

        Button backbtn = findViewById(R.id.backbtn);

        Cursor res3 = MyDbAdapter.getsteps();
        while(res3.moveToNext()) {
            if (res3.getInt(0) == 1) {
                stepid = res3.getInt(0);
                textViewStepCounter.setText(res3.getString(1));
                totalsteps = res3.getString(1);
                stepsnow = res3.getString(2);
                textViewStepDetector.setText(res3.getString(2));
            }
        }
        //set sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Permission
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, stepDetect);
        }
        //check any sensor work
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent = true;
        }else{
            textViewStepCounter.setText("Detector Sensor is not present");
            isCounterSensorPresent = false;
        }

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null){
            mStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            isDetectorSensorPresent = true;
        }else{
            textViewStepDetector.setText("Counter Sensor is not present");
            isCounterSensorPresent = false;
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


    //when senor have new data
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor == mStepCounter){
            stepCount = (int)sensorEvent.values[0];
            textViewStepCounter.setText(String.valueOf(stepCount));

            String newsteps = textViewStepCounter.getText().toString();
            if (count2 == 0){
                count2= count2+1;
                long numRows = MyDbAdapter.insertsteps(totalsteps, stepsnow);
                if (numRows <= 0) {
                    Toast.makeText(stepcounter.this, "Insertion Unsuccessful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(stepcounter.this, "Insertion Successful", Toast.LENGTH_SHORT).show();
                }
            }
            if  (count2 > 0) {
                int action = MyDbAdapter.updatetotalsteps("1", newsteps);
                if (action <= 0) {
                    Toast.makeText(stepcounter.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                } else {
                    textViewStepCounter.setText(totalsteps);
                }
            }

        }else if(sensorEvent.sensor == mStepDetector){
            stepDetect = (int) (stepDetect+sensorEvent.values[0]);
            textViewStepDetector.setText(String.valueOf(stepDetect));

        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void Load_setting () {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT", false);
        if (chk_night) {
            ml.setBackgroundColor(Color.parseColor("#222222"));
        } else {
            ml.setBackground(this.getDrawable(R.drawable.background));
        }

    }
    @Override
    protected void onResume() {
        Load_setting ();
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null)
            sensorManager.registerListener(this,mStepDetector, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.unregisterListener(this, mStepCounter);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null)
            sensorManager.unregisterListener(this, mStepDetector);
    }

}