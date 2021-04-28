package com.bshara.appointmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bshara.appointmentapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //viewbinding
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
                signInWithPhoneAuthCredntial(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            /*
                this will unfold when invalid request happen
                for instance if the phone num format isnt valid
             */
                pd.dismiss();
                Toast.makeText(MainActivity.this , ""+e.getMessage(),Toast.LENGTH_SHORT).show();
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

                mVerificatiodId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                //hide phone layout , show code layout
                binding.phoneLl.setVisibility(View.GONE);
                binding.CodeLl.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Verification Code Sent", Toast.LENGTH_SHORT).show();

                binding.codeSentDescription.setText("Please Type The Verification Code We Sent \nto " + binding.phoneEt.getText().toString().trim());
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
                    resendVerification(phone , forceResendingToken);
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

    private void verifyPhoneNumberWithCode(String verificatiodId, String code) {
        pd.setMessage("Verifing Code");
        pd.show();

        PhoneAuthCredential credential = PhoneAuthProvider
                .getCredential(verificatiodId,code);
        signInWithPhoneAuthCredntial(credential);
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
                        Toast.makeText(MainActivity.this , "Logged In as"+ phone, Toast.LENGTH_SHORT).show();

                        //start profile activity
                        startActivity(new Intent(MainActivity.this , ProfileActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //faile Sign in
                        pd.dismiss();
                        Toast.makeText(MainActivity.this , "Logged In as"+e.getMessage() , Toast.LENGTH_SHORT).show();

                    }
                });
    }

    //new master comment

}