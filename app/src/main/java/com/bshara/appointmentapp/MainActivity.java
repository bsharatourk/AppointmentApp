package com.bshara.appointmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bshara.appointmentapp.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class MainActivity extends AppCompatActivity {

    //biewbinding
    private ActivityMainBinding binding;

    //if code sent failed , it will resend OTP
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificatiodId;

    private static  final String TAG = "MAIN_TAG";

    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.phoneLl.setVisibility(View.VISIBLE);//Show phone layout
        binding.CodeLl.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        //init progress dialog
        pd = new ProgressDialog( this);
        pd.setTitle("Please Wait ...");
        pd.setCanceledOnTouchOutside(false);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            /* This will unfold in two situations:
                1-the phone num will instantly be verified.
                2-google will auto detect the OTP code and will
                fill it auto.
             */
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            /*
                this will unfold when invalid request happen
                for instance if the phone num format isnt valid
             */
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                /*
                The Sms verification has been sent to the provided phone number and we
                now need to ask the user to enter the code and then construct a credential
                by combining the code with a verification ID.
                 */
            }
        };

        binding.phoneCntinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneEt.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(MainActivity.this,"Please Enter Phone Number...", Toast.LENGTH_SHORT).show();
                }
                else{
                    startPhoneNumberVerification(phone);
                }
            }
        });
        binding.resendCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneEt.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(MainActivity.this,"Please Enter Phone Number ...", Toast.LENGTH_SHORT).show();
                }
                else{
                    resendVerification(phone);
                }
            }
        });
        binding.codeSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.codeEt.getText().toString().trim();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(MainActivity.this,"Please Enter Verification Code ...",Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyPhoneNumberWithCode(mVerificatiodId,code);
                }
            }
        });
        //comment
        //jerry Comment
    }

    private void startPhoneNumberVerification(String phone){

    }

    private void resendVerification(String phone){

    }

    private void verifyPhoneNumberWithCode(String mVerificatiodId, String code) {
    }

}