package com.bshara.appointmentapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bshara.appointmentapp.Common.Common;
import com.bshara.appointmentapp.Fragments.HomeFragment;
import com.bshara.appointmentapp.Fragments.ShoppingFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        //Init
        userRef = FirebaseFirestore.getInstance().collection("Client");
        dialog = new SpotsDialog.Builder().setContext(HomeActivity.this).setCancelable(false).build();


        //Check intent if its login = true , enable full access
        //if is login = false let user around shopping to view
        if(getIntent() !=null)
        {
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN,false);
            if(isLogin)
            {
                dialog.show();

                // TODO: Check if user exists
            }

        }

        //View
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_home){
                    fragment = new HomeFragment();
                }
                else if(menuItem.getItemId() == R.id.action_shopping){
                    fragment = new ShoppingFragment();
                }



                return loadFragment(fragment);
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);

    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null ){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
            .commit();
            return true;
        }
        return false;
    }

    // this dialog should be in the check if user exists
    private void showUpdateDialog(final String phoneNumber , String fullname , String email){

        if (dialog.isShowing())
        {
            dialog.dismiss();
        }

        //Init dialog
        bottomSheetDialog = new BottomSheetDialog(HomeActivity.this);
        bottomSheetDialog.setTitle("One More Step!");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information,null);

        Button btn_update = (Button)sheetView.findViewById(R.id.btn_update);
        final TextInputEditText edt_name = (TextInputEditText)sheetView.findViewById(R.id.edit_name);
        final TextInputEditText edt_phone = (TextInputEditText)sheetView.findViewById(R.id.edit_Phone_number);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog.isShowing())
                {
                    dialog.show();
                }

                final User user = new User(edt_name.getText().toString(),edt_phone.getText().toString());
                userRef.document(phoneNumber).set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                bottomSheetDialog.dismiss();
                                if (dialog.isShowing())
                                {
                                    dialog.dismiss();
                                }

                                Common.currentUser = user;
                                bottomNavigationView.setSelectedItemId(R.id.action_home);
                                Toast.makeText(HomeActivity.this,"Thank You",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bottomSheetDialog.dismiss();
                        if (dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(HomeActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}
