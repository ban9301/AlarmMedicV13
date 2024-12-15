package com.example.leunglokyin03a;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.leunglokyin03a.MyDBHelper.MyDbAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class changeRecord extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private MyDbAdapter MyDbAdapter;
    public String oldMediumName, oldtime, olddate, oldeveryday, oldDuration;
    public int id;
    String currentDate;
    Boolean setDate=false;
    Boolean present=false;
    String checkDate;
    Boolean setTime=false;
    String checkTime;
    long timeSystem=System.currentTimeMillis();
    Spinner TimesEveryday_spinner;
    Spinner Duration_spinner;
    public int min, hour;
    private LinearLayout ml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changerecord);
        EditText edtMediumName3= findViewById(R.id.edtMediumName3);
        TextView texttime3 = findViewById(R.id.txttime3);
        TextView textdate3 = findViewById(R.id.txtdate3);
        TextView txtTimesEveryday = findViewById(R.id.txtTimesEveryday);
        TextView txtDuration = findViewById(R.id.txtDuration);
        Button change = findViewById(R.id.change);
        Button backbtn = findViewById(R.id.backbtn);
        ml = findViewById(R.id.ml);

        MyDbAdapter = new MyDbAdapter(this);
        Calendar cal=Calendar.getInstance();
        checkDate= DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime());
        Button time_button3=findViewById(R.id.time_button3);
        Button date_button3=findViewById(R.id.date_button3);
        Button delete3 = findViewById(R.id.delete3);

        ArrayAdapter<CharSequence> TimesEveryday =
                ArrayAdapter.createFromResource(this,
                        R.array.TimesEveryday,
                        android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> Duration =
                //spinner
                ArrayAdapter.createFromResource(this,
                        R.array.Duration,
                        android.R.layout.simple_spinner_item);
        TimesEveryday_spinner = findViewById(R.id.SpTimesEveryday2);
        Duration_spinner = findViewById(R.id.SpDuration2);

        Duration_spinner.setAdapter(Duration);
        TimesEveryday_spinner.setAdapter(TimesEveryday);

        //check valid date dd/mm/yy all are numbers
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        checkTime=sdf.format(timeSystem);

        time_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show time picker
                DialogFragment timepicker=new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        date_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show date picker
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        Cursor res = MyDbAdapter.getdata();


        Toast.makeText(getApplicationContext(), "hi"+ store._id, Toast.LENGTH_LONG).show();
        while(res.moveToNext()) {
            if (res.getInt(0) == store._id) {
                //get the right information from mainactivity
                id = store._id;
                store.idalarm = res.getInt(0);
                edtMediumName3.setText(res.getString(1));
                oldMediumName = res.getString(1);

                texttime3.setText(res.getString(2));
                oldtime = res.getString(2);

                textdate3.setText(res.getString(3));
                olddate = res.getString(3);

                oldeveryday = res.getString(4);
                txtTimesEveryday.setText(res.getString(4));

                oldDuration = res.getString(5);
                txtDuration.setText(res.getString(5));
            }
        }
        delete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mediumid = id;
//                if(mediumName.isEmpty()) {
//                    Toast.makeText(changeRecord.this, "Please Enter Data", Toast.LENGTH_SHORT).show();
//                }

                    int action = MyDbAdapter.deleteOneRecord1(String.valueOf(mediumid));
                    if (action <= 0) {
                        Toast.makeText(changeRecord.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                        edtMediumName3.setText(oldMediumName);
                    } else {
                        Toast.makeText(changeRecord.this, "Deleted", Toast.LENGTH_SHORT).show();
                        edtMediumName3.setText(oldMediumName);
                        cancelAlarm();
                        Intent intent = new Intent(changeRecord.this, MainActivity.class);
                        startActivity(intent);
                    }

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMediumName = edtMediumName3.getText().toString();
                String newtime = texttime3.getText().toString();
                String newdate = textdate3.getText().toString();
                String newDuration = Duration_spinner.getSelectedItem().toString();
                String neweveryday = TimesEveryday_spinner.getSelectedItem().toString();

                if (newMediumName.isEmpty() || newtime.isEmpty() || newdate.isEmpty()) {
                    Toast.makeText(changeRecord.this, "Enter Data", Toast.LENGTH_SHORT).show();
                } else {
                    int action1 = MyDbAdapter.updateName(oldMediumName,newMediumName);
                    int action2 = MyDbAdapter.updatetime(oldtime,newtime);
                    int action3 = MyDbAdapter.updatedate(olddate,newdate);
                    int action4 = MyDbAdapter.updateeveryday(oldeveryday,neweveryday);
                    int action5 = MyDbAdapter.updateDuration(oldDuration,newDuration);
                    if (action1 <= 0 && action2 <= 0 && action3 <= 0 && action4 <= 0 && action5 <= 0 ) {
                        Toast.makeText(changeRecord.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                        edtMediumName3.setText(oldMediumName);
                        texttime3.setText(oldtime);
                        textdate3.setText(olddate);
                    } else {
                        Toast.makeText(changeRecord.this, "Updated", Toast.LENGTH_SHORT).show();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hour);
                        c.set(Calendar.MINUTE, min);
                        c.set(Calendar.SECOND, 0);
                        updateTimeText(c);
                        startAlarm(c);
                        Intent intent = new Intent(changeRecord.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String current= DateFormat.getInstance().format(c.getTime());
        String[] currentDateArray=current.split(" ");
        currentDate=currentDateArray[0];
        String [] currentArray=currentDate.split("/");
        TextView textView=findViewById(R.id.txtdate3);
        textView.setText(currentDate);
        String[] systemDate=checkDate.split("/");
        if(Integer.parseInt(currentArray[2])>Integer.parseInt(systemDate[2])){
            setDate=true;}
        else if(Integer.parseInt(currentArray[2])==Integer.parseInt(systemDate[2])){
            if(Integer.parseInt(currentArray[1])>Integer.parseInt(systemDate[1])){
                setDate=true;
            }
            else if(Integer.parseInt(currentArray[1])==Integer.parseInt(systemDate[1])&& Integer.parseInt(currentArray[0])>=Integer.parseInt(systemDate[0])){
                setDate=true;
                if(Integer.parseInt(currentArray[0])==Integer.parseInt(systemDate[0])){
                    present=true;
                }
            }
        }
        if(!setDate){
            Toast.makeText(changeRecord.this,"Please Select Date which is yet to come",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String minFinal="";
        if(minute<10){
            minFinal="0"+minute;
        }
        else{
            minFinal+=minute;
        }
        TextView txttime3 =findViewById(R.id.txttime3);
        txttime3.setText(""+hourOfDay+":"+minFinal);
        String[] sysTime=checkTime.split(":");
        hour=hourOfDay;
        min=minute;
        if(present){
            if(hourOfDay> Integer.parseInt(sysTime[0])){
                setTime=true;
            }
            else if(hourOfDay==Integer.parseInt(sysTime[0])&& minute-2>= Integer.parseInt(sysTime[1])){
                setTime=true;
            }
            else{
                Toast.makeText(changeRecord.this,"Please Select Time with at least an interval of 2 mins",Toast.LENGTH_SHORT).show();

            }
        }else{setTime=true;}
    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, store.idalarm, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
    private void updateTimeText(Calendar c) {
        TextView txttime2 =findViewById(R.id.txttime2);
        String timeText = " ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        txttime2.setText(timeText);
    }
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, store.idalarm, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
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
    protected void onResume(){
        Load_setting();
        super.onResume();
    }
}

