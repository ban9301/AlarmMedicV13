package com.example.leunglokyin03a;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leunglokyin03a.MyDBHelper.MyDbAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Emergencycall extends AppCompatActivity {
    private EditText edtPhone;
    private Button setphone;
    TextView phonenumber;
    private Button btnSend;
    public String newPhonenumber;
    public String oldPhonenumber;
    int phoneid;
    public int count;
    FusedLocationProviderClient fusedLocationProviderClient;
    MyDbAdapter MyDbAdapter;
    private LinearLayout ml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergencycall);
        MyDbAdapter = new MyDbAdapter(this);
        btnSend = findViewById(R.id.btnLocation);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        setphone = (Button) findViewById(R.id.btnSend);
        ml = findViewById(R.id.ml);

        Cursor res2 = MyDbAdapter.getdataPhone();
        while(res2.moveToNext()) {
            if (res2.getInt(0) == 1) {
                phoneid = res2.getInt(0);
                edtPhone.setText(res2.getString(1));
                oldPhonenumber = res2.getString(1);
            }
        }

        setphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res2 = MyDbAdapter.getdataPhone();
                while(res2.moveToNext()) {
                    if (res2.getInt(0) == 1) {
                        oldPhonenumber = res2.getString(1);
                    }
                }

                String s = edtPhone.getText().toString();
                int c = s.length();
                if (c == 8) {
                    newPhonenumber = edtPhone.getText().toString();
                    if (count==0){
                        MyDbAdapter.insertPhone(newPhonenumber);
                        phonenumber.setText(newPhonenumber);
                        count= count+1;
                    }else {
                        int action = MyDbAdapter.updatePhone(oldPhonenumber, newPhonenumber);
                        if (action <= 0) {
                            Toast.makeText(Emergencycall.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                            phonenumber.setText(newPhonenumber);
                            final String url = "http://10.0.2.2:8080/myphp/osmad/addComment.php";
                            new MyAsyncTask().execute(url);
                        } else {
                            Toast.makeText(Emergencycall.this, "Updated", Toast.LENGTH_SHORT).show();
                            phonenumber.setText(newPhonenumber);
                        }
                    }
                } else {
                    Toast.makeText(Emergencycall.this,"You should enter 8 numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(Emergencycall.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
        } else {
            //when it doesn't , get permission
            ActivityCompat.requestPermissions(Emergencycall.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        if (ActivityCompat.checkSelfPermission(Emergencycall.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res2 = MyDbAdapter.getdataPhone();
                while(res2.moveToNext()) {
                    if (res2.getInt(0) == 1) {
                        oldPhonenumber = res2.getString(1);
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(Emergencycall.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Emergencycall.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        sendSMS();
                    }else{
                        if (ActivityCompat.checkSelfPermission(Emergencycall.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                        } else {
                            //when it doesn't , get permission
                            ActivityCompat.requestPermissions(Emergencycall.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        }
                        if (ActivityCompat.checkSelfPermission(Emergencycall.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            //when it doesn't , get permission
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                        }
                    }
                }
            }
        });
    }

    private void sendSMS () {
        Cursor res2 = MyDbAdapter.getdataPhone();
        while(res2.moveToNext()) {
            if (res2.getInt(0) == 1) {
                oldPhonenumber = res2.getString(1);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();

                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(Emergencycall.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        SmsManager smsManager = SmsManager.getDefault();
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();
                        String Address = addresses.get(0).getAddressLine(0);
                        smsManager.sendTextMessage(oldPhonenumber, null, "I am at "+Address+"\nLatitude: "+latitude+"\nLongitude: "+longitude, null, null);
                        Uri uri = Uri.parse("tel:"+oldPhonenumber);
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public String executeHttpPost(String url) {
        String result = "";

        // HttpClient acts like a Browser (without the UI)
        HttpClient client = new DefaultHttpClient();

        //Create Object to represent a POST request
        HttpPost request = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        // $_POST[] values to PHP
        nameValuePairs.add(new BasicNameValuePair("Phone", edtPhone.getText().toString()));
        // This will store the response from the server
        HttpResponse response;

        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //Actually call the server
            response = client.execute(request);

            // Extract text message from server
            result = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            result = "[ERROR]" + e.toString();
            Log.v("myLog","result: " + result);
        }

        return result;
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"Connecting to server...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... url) {
            return executeHttpPost(url[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (oldPhonenumber != null) {
                phonenumber.setText(result);
                oldPhonenumber = result;
            }
        }
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
