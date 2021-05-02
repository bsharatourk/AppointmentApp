package com.bshara.appointmentapp;

import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    AuthStateListener mAuthl;
    ProgressDialog loginProgress;
    DatabaseReference mDataBase;

    @Override
    protected void onStart() {
        super.onStart();
       // fAuth.addAuthStateListener(mAuthl);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("OnCreate", "i am here");

        mEmail = findViewById(R.id.emailBar);
        mPassword = findViewById(R.id.pinCode);
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createBtn);
        mDataBase = FirebaseDatabase.getInstance().getReference().child("User");
        fAuth = FirebaseAuth.getInstance();

        mAuthl = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.e("Profile intent", "got here now");
               // startActivity(new Intent(Login.this , ProfileActivity.class));
            }
        };

        fAuth.addAuthStateListener(mAuthl);
        progressBar = findViewById(R.id.progressBar);

    }

    private void checkLogin(){
        String email=mEmail.getText().toString();
        String pass=mPassword.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            loginProgress.setMessage("Check Login....");
            loginProgress.show();
            fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loginProgress.dismiss();
                        checkUserExist();
                    }
                    else{
                        loginProgress.dismiss();
                        Toast.makeText(Login.this,"Error Login",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void checkUserExist() {
        String userId = fAuth.getCurrentUser().getUid();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                if (dataSnapShot.hasChild(userId)){
                    Log.e("intent", "i am here");
                    Intent loginintent = new Intent(Login.this, ProfileActivity.class);
                    startActivity(loginintent);
                }
                else{
                    Toast.makeText(Login.this,"You Need To Setup Your Account",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void LoginBtn(View view) {
        checkLogin();
    }

    public void RegisterActivity(View view) {
        startActivity(new Intent(getApplicationContext(),Register.class));
    }
}
