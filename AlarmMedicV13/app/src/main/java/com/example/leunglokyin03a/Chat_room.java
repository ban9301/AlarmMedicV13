package com.example.leunglokyin03a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leunglokyin03a.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat_room extends AppCompatActivity {
    ImageButton sendBtn;
    EditText msgEt,senderEt;
    ListView listView;
    ArrayAdapter msgAdapter;
    DatabaseReference myRef;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());
    String str = formatter.format(curDate);
    private RelativeLayout ml;

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;

    private String user_name, room_name;
    private DatabaseReference root;
    private String temp_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_chat_room);
        ml = findViewById(R.id.ml);
        room_name = getIntent().getExtras().get("room_name").toString();
        getSupportActionBar().setTitle("Room - "+room_name);


        myRef = FirebaseDatabase.getInstance().getReference().child(room_name);





        sendBtn=findViewById(R.id.sendBtn);
        senderEt=findViewById(R.id.senderEt);
        msgEt=findViewById(R.id.msgEt);
        listView=findViewById(R.id.listview);


        ArrayList<String> msgList=new ArrayList<String>();

        msgAdapter=new ArrayAdapter<String>(this,R.layout.listitem,msgList);

        listView.setAdapter(msgAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sender=senderEt.getText().toString();
                String msg=msgEt.getText().toString();


                if(msg.length()>0 && sender.length()>0)
                {
                    // msgAdapter.add(sender+">"+msg);


                    myRef.push().setValue(sender+" : "+msg + "               "+ str );


                    msgEt.setText("");
                }



            }
        });




        loadMsg();


    }

    private void loadMsg() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                msgAdapter.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
