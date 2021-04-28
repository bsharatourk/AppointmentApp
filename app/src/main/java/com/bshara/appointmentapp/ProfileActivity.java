package com.bshara.appointmentapp;

import android.accounts.Account;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bshara.appointmentapp.Fragments.HomeFragment;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(ProfileActivity.this);

        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        //Checked if Logged in
      /*  if (getIntent() != null)
        {
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN,false);
            if (isLogin)
            {
                dialog.show();

                //Check if user exist
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        if (account != null)
                        {
                            final DocumentReference currentUser = userRef.document(account.getPhoneNumber().toString());
                            currentUser.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                DocumentSnapshot userSnapShot = task.getResult();
                                                if (!userSnapShot.exists())
                                                {
                                                    showUpdateDialog(account.getPhoneNumber().toString());
                                                }
                                                else
                                                {
                                                    //if there is already a registered user
                                                    Common.currentUser = userSnapShot.toObject(User.class);
                                                    bottomNavigationView.setSelectedItemId(R.id.action_home);
                                                }
                                                if (dialog.isShowing())
                                                    dialog.dismiss();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Toast.makeText(ProfileActivity.this, ""+accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void showUpdateDialog(final String phoneNumber) {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("One More Step!");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information, null);

        Button btn_update = (Button) sheetView.findViewById(R.id.btn_update);
        final TextInputEditText edt_name = (TextInputEditText) sheetView.findViewById(R.id.edt_name);
        final TextInputEditText edt_address = (TextInputEditText) sheetView.findViewById(R.id.edt_address);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!dialog.isShowing())
                    dialog.show();

                User user = new User(edt_name.getText().toString(),
                        edt_address.getText().toString(),
                        phoneNumber);
                userRef.document(phoneNumber)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                bottomSheetDialog.dismiss();
                                if (dialog.isShowing())
                                    dialog.dismiss();

                                Common.currentUser = user;
                                bottomNavigationView.setSelectedItemId(R.id.action_home);

                                Toast.makeText(ProfileActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bottomSheetDialog.dismiss();
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
*/

    }
}