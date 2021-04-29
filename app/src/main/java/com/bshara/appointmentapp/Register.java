package com.bshara.appointmentapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    EditText mFullName,mPhone,mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn, numTextView;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    EditText codeEt;

    //Layouts

    LinearLayout register;
    LinearLayout code;

    //Added


    //if code sent failed , it will resend OTP
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;

    private static  final String TAG = "MAIN_TAG";

    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog pd;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.RegLl);
        code = findViewById(R.id.CodeLl);

        register.setVisibility(View.VISIBLE);
        code.setVisibility(View.INVISIBLE);

        codeEt = findViewById(R.id.codeEt);
        firebaseAuth = FirebaseAuth.getInstance();

        //init progress dialog
        pd = new ProgressDialog( this);
        pd.setTitle("Please Wait ...");
        pd.setCanceledOnTouchOutside(false);

        mFullName = findViewById(R.id.fullName);
        mPhone = findViewById(R.id.phoneNum);
        mPassword = findViewById(R.id.pinCode);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.Aregister);
        numTextView = findViewById(R.id.codeSentDescription);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

       /* if(fAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        }*/

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            /* This will unfold in two situations:
                1-the phone num will instantly be verified.
                2-google will auto detect the OTP code and will
                fill it auto.
             */
                signInWithPhoneAuthCredntial(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            /*
                this will unfold when invalid request happen
                for instance if the phone num format isnt valid
             */
                pd.dismiss();
                Toast.makeText(Register.this , ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                /*
                The Sms verification has been sent to the provided phone number and we
                now need to ask the user to enter the code and then construct a credential
                by combining the code with a verification ID.
                 */
                Log.d(TAG, "onCodeSent "+ verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                Toast.makeText(Register.this, "Verification Code Sent", Toast.LENGTH_SHORT).show();

                //numTextView.setText("Please Type The Verification Code We Sent \nto " + findViewById(R.id.phoneNum).toString());
            }
        };
    }
    public void RegisterBtn(View view) {
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

        //register user to fireBase

        fAuth.createUserWithEmailAndPassword(fullName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startPhoneNumberVerification("+972"+mPhone.getText().toString());
                    Toast.makeText(Register.this, "User Created, Inter the code", Toast.LENGTH_SHORT).show();
                    register.setVisibility(View.INVISIBLE);
                    code.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(Register.this,"Error !!"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void startPhoneNumberVerification(String phone){
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone) //Phone number must be with coutry code for example israel +972
                        .setTimeout(60L , TimeUnit.SECONDS) // the timeout and unit
                        .setActivity(this) // activity (for the callback binding )
                        .setCallbacks(mCallbacks) // OnVerificationStateChangedCallBacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredntial(PhoneAuthCredential credential) {
        pd.setMessage("Logging In");

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Successfully signed in
                        pd.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(Register.this , "Logged In as"+ phone, Toast.LENGTH_SHORT).show();

                        //start profile activity
                        startActivity(new Intent(Register.this , ProfileActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //faile Sign in
                        pd.dismiss();
                        Toast.makeText(Register.this , "Logged In as"+e.getMessage() , Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void resendVerification(String phone , PhoneAuthProvider.ForceResendingToken token){
        pd.setMessage("Resending Code");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone) //Phone number must be with coutry code for example israel +972
                        .setTimeout(60L , TimeUnit.SECONDS) // the timeout and unit
                        .setActivity(this) // activity (for the callback binding )
                        .setCallbacks(mCallbacks) // OnVerificationStateChangedCallBacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        pd.setMessage("Verifing Code");
        pd.show();

        PhoneAuthCredential credential = PhoneAuthProvider
                .getCredential(verificationId,code);
        signInWithPhoneAuthCredntial(credential);
    }

    public void ResendBtn(View view) {
        String phone = "+972"+findViewById(R.id.phoneNum).toString();
        resendVerification(phone , forceResendingToken);
    }

    public void SubmitBtn(View view) {
        String code = codeEt.getText().toString();
        if (TextUtils.isEmpty(code)){
            Toast.makeText(this,"Please Enter Verification Code ...",Toast.LENGTH_SHORT).show();
        }
        else{
            verifyPhoneNumberWithCode(mVerificationId,code);
        }
    }
}
