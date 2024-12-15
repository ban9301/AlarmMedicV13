package com.example.leunglokyin03a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.leunglokyin03a.MyDBHelper.MyDBHelper;

import java.io.ByteArrayOutputStream;

public class takephoto extends AppCompatActivity {


    ImageView img1;
    Button btn1, btn2, btn3;
    public static MyDBHelper DB;
    EditText editText;
    private static final int CAMERA_REQUEST_CODE = 200;
    private LinearLayout ml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takephoto);
        img1 = findViewById(R.id.photo);
        btn1 = findViewById(R.id.capture);
        btn2 = findViewById(R.id.save);
        btn3 = findViewById(R.id.listview);
        ml = findViewById(R.id.ml);
        editText = findViewById(R.id.edittext);
        DB = new MyDBHelper(this);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    DB.insertdata(editText.getText().toString(), imageToByte(img1));
                    Toast.makeText(takephoto.this, "Data Saved", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(takephoto.this, "Data Not Saved",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(takephoto.this, photolist.class);
                startActivity(intent);
            }
        });
    }
    private byte[] imageToByte(ImageView img1){
        Bitmap bitmap = ((BitmapDrawable)img1.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] img = byteArray.toByteArray();
        return img;
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap b1 = (Bitmap) extras.get("data");
                img1.setImageBitmap(b1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume(){
        Load_setting();
        super.onResume();
    }
}
