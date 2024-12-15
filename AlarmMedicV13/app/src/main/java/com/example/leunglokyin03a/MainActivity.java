package com.example.leunglokyin03a;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.leunglokyin03a.MyDBHelper.MyDBHelper;
import com.example.leunglokyin03a.MyDBHelper.MyDbAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.VISIBLE;
import static com.example.leunglokyin03a.store.idalarm;

public class MainActivity extends AppCompatActivity {
    public static int itemid;
    private MyDbAdapter myDbAdapter;
    public Cursor retCursor = null;
    SearchView search;
    public static MyDBHelper DB;
    ArrayList<photo> list;
    MyDbAdapter adapter = null;
    private LinearLayout ml;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] from = new String[]{"MediumName", "time", "date", "everyday", "Duration"};
        int[] to = new int[]{R.id.txtMediumName, R.id.txttime, R.id.txtdate, R.id.txteveryday, R.id.txtduration};
        ImageView image1 = findViewById(R.id.image1);
        ml = findViewById(R.id.ml);

//        ListView photolist = (ListView) findViewById(R.id.photolist);
//        list = new ArrayList<>();
//        adapter = new MyDbAdapter(this, R.layout.activity_main, list);
//        photolist.setAdapter(adapter);
//
//        Cursor cursor = takephoto.DB.getData("SELECT * FROM photo");
//        list.clear();
//        while(cursor.moveToNext()){
//            String name = cursor.getString(0);
//            byte[] image = cursor.getBlob(1);
//            list.add(new photo(name,image));
//        }
//        adapter.notifyDataSetChanged();
//        Button btndeleter = (Button) findViewById(R.id.deleterecord);
//        Button delete = findViewById(R.id.delete);


        /*
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, requestId, intent,
                        PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        */
        //set SQLite data to listview
        //R.layout.row is the layout of each listview
        //retCursor select all the record in the table
        //from is column name
        //match all data to different textview
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,R.layout.row,retCursor,from,to);
        ImageButton add = (ImageButton) findViewById(R.id.add);
        ListView contentlist = (ListView) findViewById(R.id.contentlist);

        ListView listView = (ListView) findViewById(R.id.contentlist);
        search = (SearchView) findViewById(R.id.search);
        //set listview Adapter to cursorAdapter
        listView.setAdapter(cursorAdapter);

        //call MyDbAdapter
        myDbAdapter = new MyDbAdapter(this);

        //myDbAdapter.deleteAllRecords();

        //insert data
//        myDbAdapter.insert("a","10am", "10", "10", "20");
//        myDbAdapter.insert("b","10am", "11");
//        myDbAdapter.insert("c", "10pm", "6");
        //select all data

//        while(cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            String name = cursor.getString(1);
//            byte[] image = cursor.getBlob(2);
//
//            list.add(new photo(name,image));
//        }

        retCursor = myDbAdapter.selectAll();

        //input the data of retCursor to adapter
        cursorAdapter.swapCursor(retCursor);
//        Cursor cursor = takephoto.DB.getData("SELECT * FROM photo");
//        while(cursor.moveToNext()) {
//            if (cursor.getInt(0) == store._id) {
//                //get the right information from mainactivity
//                Integer id = cursor.getInt(0);
//                String name = cursor.getString(1);
//                byte[] image = cursor.getBlob(2);
////                contentlist.add(new photo(id, name, image));
//            }
//        }
//        Toast.makeText(this,"Done",Toast.LENGTH_LONG).show();

        //insert data to database
        //myDbAdapter.insert("John","9112 2342");
       //myDbAdapter.insert("Mary","6123 1232");
        //myDbAdapter.insert("Joe", "6234 2342");



        //select all data in data base
        //retCursor = myDbAdapter.selectAll();

        //select some data in data base
        //retCursor = myDbAdapter.selectOneRecord("Mary");

//        retCursor = myDbAdapter.selectAll();

        //update data
//        myDbAdapter.updateRecord(2,"Mary Lee");
//        Log.v("mylog", "------------------------------------------");
//        retCursor =myDbAdapter.selectAll();


//        retCursor = myDbAdapter.selectAll();
        //delete all data
//        myDbAdapter.deleteAllRecords();
        //print "------------------------------------------" in mylog
//        Log.v("mylog", "------------------------------------------");
//        retCursor =myDbAdapter.selectAll();
//        Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
//        deleterecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        deleterecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        btndeleter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ( Visibilitycounter%2 ==0) {
//                    delete.setVisibility(VISIBLE);
//                }
//                else {
//                    delete.setVisibility(View.INVISIBLE);
//                }
//                Visibilitycounter+=1;
//            }
//        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newname) {
                retCursor = myDbAdapter.selectname(newname);
                cursorAdapter.changeCursor(retCursor);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.idalarm = store.idalarm +1;
                Intent intent = new Intent(MainActivity.this, form.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, changeRecord.class);
                retCursor = (Cursor) parent.getItemAtPosition(position);
                int idIndex = retCursor.getColumnIndex("_id");
                int itemid = retCursor.getInt(idIndex);
                store._id=itemid;
                startActivity(intent);
            }
        });
//        while(res.moveToNext()) {
//            if (res.getInt(0) == store._id) {
//                edtMediumName3.setText(res.getString(1));
//                oldMediumName = res.getString(1);
//                texttime3.setText(res.getString(2));
//                oldtime = res.getString(2);
//                textdate3.setText(res.getString(3));
//                olddate = res.getString(3);
//            }
//        }
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                retCursor = (Cursor) parent.getItemAtPosition(position);
                int MediumNameIndex = retCursor.getColumnIndex("_id");
                String MediumNameid = retCursor.getString(MediumNameIndex);
                idalarm = retCursor.getColumnIndex("_id");
                new AlertDialog.Builder (MainActivity.this)
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to remove this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int action=myDbAdapter.deleteOneRecord1(String.valueOf(MediumNameid));
                                if (action <= 0) {
                                    Toast.makeText(MainActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    cancelAlarm();
                                    retCursor = myDbAdapter.selectAll();
                                    cursorAdapter.swapCursor(retCursor);
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });


    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, idalarm, intent, 0);
        alarmManager.cancel(pendingIntent);
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
