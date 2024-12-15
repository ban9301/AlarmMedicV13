package com.example.leunglokyin03a;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.leunglokyin03a.MyDBHelper.MyDBHelper;
import com.example.leunglokyin03a.MyDBHelper.MyDbAdapter;

import java.util.ArrayList;

public class photolist extends AppCompatActivity {


    GridView gridView;
    ArrayList<photo> list;
    MyDbAdapter adapter = null;
    private ArrayList<photo> photolist;
    private LinearLayout ml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolist);

        ml = findViewById(R.id.ml);
        gridView = (GridView)findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new MyDbAdapter(this, R.layout.eachphoto, list);
        gridView.setAdapter(adapter);


        Cursor cursor = takephoto.DB.getData("SELECT * FROM photo");
        list.clear();
        while(cursor.moveToNext()){
            Integer id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);

            list.add(new photo(id,name,image));
        }
        adapter.notifyDataSetChanged();
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence[] items = {"Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(photolist.this);
                dialog.setTitle("Action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            Cursor cursor = takephoto.DB.getData("SELECT _id FROM photo");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();while (cursor.moveToNext()) {
                                arrID.add(cursor.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void showDialogDelete(final int photo) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(photolist.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    takephoto.DB.deleteData(photo);
                    Toast.makeText(getApplicationContext(), "Delete successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                upDatePhotoList();
            }
        });
        dialogDelete.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }
    private void upDatePhotoList(){
        Cursor cursor = takephoto.DB.getData("SELECT * FROM photo");
        list.clear();
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);

            list.add(new photo(id, name, image));
        }
        adapter.notifyDataSetChanged();
    };
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

