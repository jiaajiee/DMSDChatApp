package com.example.a15017274.dmsdchatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText etEmail, etPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void register(View v) {
        //Toast.makeText(getApplicationContext(), "register", Toast.LENGTH_SHORT).show();

        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        if (etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "No empty fields, please.", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: implement Firebase Authentication
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Email sent.");
                                                    Toast.makeText(MainActivity.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                                    Toast.makeText(MainActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                String reason = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, "Registration failed: " + reason, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    public void login(View v) {

        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        if (etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "No empty fields, please.", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: implement Firebase Authentication
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    Toast.makeText(MainActivity.this, "Registration Successful.", Toast.LENGTH_SHORT).show();

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    } else {

                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.w(TAG, "isEmailVerified ? " + user.isEmailVerified());
                        if (user.isEmailVerified()) {
                            Intent i = new Intent(getBaseContext(), SetDisplayName.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(MainActivity.this, "Please proceed to verify your email first.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(this, SetDisplayName.class);
            startActivity(i);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

}





