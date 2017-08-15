package com.example.a15017274.dmsdchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetDisplayName extends AppCompatActivity {

    private static final String TAG = "SetDisplayNameActivity";
    private EditText etName;
    private Button btnSubmit;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference profileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_display_name);

        etName = (EditText) findViewById(R.id.etDisplayName);
        btnSubmit = (Button) findViewById(R.id.submitName);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        profileRef = firebaseDatabase.getReference("profiles/" + firebaseUser.getUid());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                profileRef.setValue(name);

                Intent intent = new Intent(getBaseContext(), WeatherWebService.class);
                startActivity(intent);
            }
        });

    }
}
