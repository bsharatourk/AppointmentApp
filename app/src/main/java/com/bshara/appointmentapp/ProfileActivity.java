
package com.bshara.appointmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends AppCompatActivity {
    Button LogoutBtn;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        LogoutBtn = (Button) findViewById(R.id.logoutButn);
        firebaseAuth=FirebaseAuth.getInstance();

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                signOutUser();

            }
        });

    }

    private void signOutUser() {
        Intent main = new Intent(ProfileActivity.this , Login.class);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
        finish();
    }

}