package com.example.leunglokyin03a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Spinner;
import com.example.leunglokyin03a.MyDBHelper.MyDbAdapter;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class form extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private MyDbAdapter myDbAdapter;
    String currentDate;
    Boolean setDate=false;
    Boolean present=false;
    String checkDate;
    Boolean setTime=false;
    String checkTime;
    long timeSystem=System.currentTimeMillis();
    EditText edtMediumName;
    Spinner Duration_spinner;
    Spinner TimesEveryday_spinner;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    int min1, hour1;

    private ScrollView ml;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicationform);
        Button date_button2 = findViewById(R.id.date_button2);
        Button btnsubmit = findViewById(R.id.btnsubmit);
        TextView txtdate2=findViewById(R.id.txtdate2);
        ml = findViewById(R.id.ml);
        Button btncancel = (Button) findViewById(R.id.btncancel);
        myDbAdapter = new MyDbAdapter(this);
        Calendar cal=Calendar.getInstance();
        checkDate= DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime());
        edtMediumName = findViewById(R.id.edtMediumName);
        Button time_button2=findViewById(R.id.time_button);
        TextView txttime2 =findViewById(R.id.txttime2);

//        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
//                AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
//
//        if (alarmMgr!= null) {
//            alarmMgr.cancel(alarmIntent);
//        }

        ArrayAdapter<CharSequence> TimesEveryday =
                ArrayAdapter.createFromResource(this,
                        R.array.TimesEveryday,
                        android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> Duration =
                ArrayAdapter.createFromResource(this,
                        R.array.Duration,
                        android.R.layout.simple_spinner_item);
        TimesEveryday_spinner = findViewById(R.id.SpTimesEveryday);
        Duration_spinner = findViewById(R.id.SpDuration);

        Duration_spinner.setAdapter(Duration);
        TimesEveryday_spinner.setAdapter(TimesEveryday);
//        Spinner  SpTimesEveryday_spinner = findViewById(R.id.SpTimesEveryday);
//        Spinner SpDuration_spinner = findViewById(R.id.SpDuration);
//
//        ArrayAdapter<CharSequence> SpTimesEveryday_spinner = ArrayAdapter.createFromResource(this,
//                R.array.TimesEveryday, android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> SpDuration_spinner = ArrayAdapter.createFromResource(this,
//                R.array.Duration, android.R.layout.simple_spinner_item);
//
//
//        SpTimesEveryday_spinner.setAdapter(SpTimesEveryday_spinner);
//        SpDuration_spinner.setAdapter(SpDuration_spinner);

        //To check valid date dd/mm/yy all are numbers
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        checkTime=sdf.format(timeSystem);
       time_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker=new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        date_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker=new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Duration = Duration_spinner.getSelectedItem().toString();
                String everyday = TimesEveryday_spinner.getSelectedItem().toString();
                String MediumName = edtMediumName.getText().toString();
                String time = txttime2.getText().toString();
                String date = txtdate2.getText().toString();
                if (MediumName.equals("") || time.equals("") || date.equals("")){
                    Toast.makeText(form.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
                } else {
                    myDbAdapter.insert(MediumName,time, date, everyday, Duration);
                    Toast.makeText(form.this, "Successful",Toast.LENGTH_LONG).show();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, hour1);
                    c.set(Calendar.MINUTE, min1);
                    c.set(Calendar.SECOND, 0);
                    updateTimeText(c);
                    startAlarm(c);
                    Intent intent = new Intent(form.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        TextView textView=findViewById(R.id.txtdate2);
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
            Toast.makeText(form.this,"Please Select Date which is yet to come",Toast.LENGTH_SHORT).show();
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
        TextView txttime2 =findViewById(R.id.txttime2);
        txttime2.setText(""+hourOfDay+":"+minFinal);
        String[] sysTime=checkTime.split(":");
        hour1=hourOfDay;
        min1=minute;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c);
        if(present){
            if(hourOfDay> Integer.parseInt(sysTime[0])){
                setTime=true;
            }
            else if(hourOfDay==Integer.parseInt(sysTime[0])&& minute-2>= Integer.parseInt(sysTime[1])){
                setTime=true;
            }
            else{
                Toast.makeText(form.this,"Please Select Time with atleast an interval of 2 mins",Toast.LENGTH_SHORT).show();

            }
        }else{setTime=true;}
    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, store.idalarm, intent, 0);
        alarmManager.cancel(pendingIntent);
        //mTextView.setText("Alarm canceled");
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
    private byte[] imageToByte(ImageView img1){
        Bitmap bitmap = ((BitmapDrawable)img1.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] img = byteArray.toByteArray();
        return img;
    }
    @Override
    protected void onResume(){
        Load_setting();
        super.onResume();
    }
}
