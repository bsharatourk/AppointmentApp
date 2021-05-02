package com.bshara.appointmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText mFullName,mPassword;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFullName = findViewById(R.id.fullName);
        mPassword = findViewById(R.id.pinCode);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

    }

    public void RegisterActivity(View view) {
        startActivity(new Intent(getApplicationContext(),Register.class));
    }

    public void LoginBtn(View view) {
        String fullName = mFullName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(fullName)){
            mFullName.setError("Full Name Is Required.");
            return;
        }
        if(fullName.length()<6 || fullName.contains("[0-9]+")==true ){
            mPassword.setError("FullName Is Short Or Incorrect.");
            return;
        }
        if(TextUtils.isEmpty(password)){
            mPassword.setError("Password Is Required.");
            return;
        }
        if(password.length()<7 ){
            mPassword.setError("Password Must Be 7+ Characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Authenticate the user

        fAuth.signInWithEmailAndPassword(fullName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this , "Logged In Successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                }else{
                    Toast.makeText(Login.this,"Error !!"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}