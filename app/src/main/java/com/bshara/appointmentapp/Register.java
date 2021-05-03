package com.bshara.appointmentapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Register extends AppCompatActivity {
    EditText mFullName,mPhone;

    FirebaseAuth fAuth;

    //DataBase Ref
    DatabaseReference databaseUser;
    private FirebaseFirestore db;



    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog pd;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //DataBase
        databaseUser = FirebaseDatabase.getInstance().getReference("User");
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        //init progress dialog
        pd = new ProgressDialog( this);
        pd.setTitle("Please Wait ...");
        pd.setCanceledOnTouchOutside(false);


        fAuth = FirebaseAuth.getInstance();

    }
    private void AddUser(){


        String fullName = mFullName.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String email = mPhone.getText().toString().trim();

        //FireStore
        CollectionReference ref = db.collection("Client");

        if(!TextUtils.isEmpty(fullName)){
            String id = phone;

            User user = new User(fullName,email,phone);

            ref.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(Register.this,"The Client Is Added.",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_LONG).show();

                }
            });

            databaseUser.child(id).setValue(user);

            Toast.makeText(this,"User Added ",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"You Should Enter A Name",Toast.LENGTH_SHORT).show();
        }

    }

}