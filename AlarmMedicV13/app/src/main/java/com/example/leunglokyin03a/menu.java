package com.example.leunglokyin03a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.net.URI;

public class menu extends AppCompatActivity {
    private CardView MedicineReminder , MedicinePhoto, EmergencyCall,stepcounter,Chat,Setting;
    private RelativeLayout ml;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        MedicineReminder= findViewById(R.id.MedicineReminder);
        EmergencyCall = findViewById(R.id.EmergencyCall);
        MedicinePhoto = findViewById(R.id.MedicinePhoto);
        Setting = findViewById(R.id.setting);
        ml = findViewById(R.id.ml);
        stepcounter = findViewById(R.id.stepcounter);
        Chat = findViewById(R.id.chat);

        MedicinePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,takephoto.class);
                startActivity(intent);
            }
        });

        MedicineReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,MainActivity.class);
                startActivity(intent);
            }
        });
        EmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,Emergencycall.class);
                startActivity(intent);
            }
        });
        stepcounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,stepcounter.class);
                startActivity(intent);
            }
        });
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,Preference.class);
                startActivity(intent);
            }
        });
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,chat.class);
                startActivity(intent);
            }
        });
    }
    private void Load_setting () {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT", false);
        if (chk_night) {
            ml.setBackgroundColor(Color.parseColor("#222222"));
        } else {
            ml.setBackground(this.getDrawable(R.drawable.cut_card_background));
        }

    }

    @Override
    protected void onResume(){
        Load_setting();
        super.onResume();
    }
}
