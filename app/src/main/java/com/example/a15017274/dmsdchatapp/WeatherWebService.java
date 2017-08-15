package com.example.a15017274.dmsdchatapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeatherWebService extends AppCompatActivity {

    private WeatherAdapter wa;
    private ArrayList<Weather> weathers;
    private ListView lvWeather;
    private Button btnAdd;
    private EditText etMessages;
    private TextView tvTitle;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messageRef, profileRef;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_web_service);

        btnAdd = (Button) findViewById(R.id.btnAddMsg);
        etMessages = (EditText) findViewById(R.id.etMessage);
        tvTitle = (TextView) findViewById(R.id.tvTitle);


        weathers = new ArrayList<Weather>();
        lvWeather = (ListView) findViewById(R.id.lvMessages);
        wa = new WeatherAdapter(this, R.layout.row, weathers);

        lvWeather.setAdapter(wa);
        wa.notifyDataSetChanged();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        messageRef = firebaseDatabase.getReference("/messages");
        profileRef = firebaseDatabase.getReference("/profiles/" + firebaseUser.getUid());


        HttpRequest request = new HttpRequest("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast");
        request.setMethod("GET");
        request.setAPIKey("api-key", "NPm0sfjqcq3ZyBBsIHXQC1ww6a2Q7q98");
        request.execute();

        try {
            String jsonString = request.getResponse();
            System.out.println(">>" + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray item = jsonObject.getJSONArray("items");
            JSONObject itemArr = item.getJSONObject(0);
            JSONArray forecastArr = itemArr.getJSONArray("forecasts");
            String location = "Woodlands";
            for (int i = 0; i < forecastArr.length(); i++) {
                JSONObject forecast = forecastArr.getJSONObject(i);
                String area = forecast.getString("area");

                if (area.equalsIgnoreCase(location)) {
                    String forecastInner = forecast.getString("forecast");
                    tvTitle.setText("Weather Forecast @ Woodlands: " + forecastInner);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                weathers.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Weather w = postSnapshot.getValue(Weather.class);
                    weathers.add(w);
                }

                for (int i = 0; i < weathers.size(); i++) {
                    Log.d("Database content", i + ". " + weathers.get(i));
                    wa = new WeatherAdapter(WeatherWebService.this, R.layout.row, weathers);

                }
                lvWeather.setAdapter(wa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userDisplayName = dataSnapshot.getValue(String.class);
                user = userDisplayName;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                String dateCurrent = String.valueOf(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", date));

                Weather w = new Weather(user, etMessages.getText().toString(), dateCurrent);
                messageRef.push().setValue(w);


            }
        });

    }
}
