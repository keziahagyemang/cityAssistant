package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class adminSignup extends AppCompatActivity {

    Toolbar t;
    ProgressBar pbar;
    TextInputLayout username, em, pword, confpword;
    AppCompatButton create;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference ref;

    LinearLayout su;

    final private static String tag = "create an admin account";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        t = findViewById(R.id.adminsignupToolbar);
        pbar = findViewById(R.id.progressAdminSignup);
        username = findViewById(R.id.etUsernameAdmin);
        em = findViewById(R.id.etemailAdmin);
        pword = findViewById(R.id.etpwordAdmin);
        confpword = findViewById(R.id.etConfirmpwordAdmin);
        create = findViewById(R.id.btnCreateAccAdmin);
        su = findViewById(R.id.adminsignupAct);

        t.setTitle(" Add an Admin");
        t.setTitleMarginStart(200);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainpage();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                f();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        ref = rootNode.getReference("Admin");

    }

    public void f() {
        String n = username.getEditText().getText().toString().trim();
        String e = em.getEditText().getText().toString().trim();
        String p = pword.getEditText().getText().toString().trim();
        String c = confpword.getEditText().getText().toString().trim();


        checknetwork.checkInfo(n, e, p, c, username, em, pword, confpword, su, tag);

        if (checknetwork.checkInfo(n, e, p, c, username, em, pword, confpword, su, tag)) {
            if (checknetwork.isNetworkAvailable(adminSignup.this)) {

                checkInfo(n, e, p, c);

                pbar.setVisibility(View.VISIBLE);

            } else {

                Toast.makeText(adminSignup.this,
                        "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void checkInfo(String un, String email, String password, String cp) {

        Calendar k = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        Date tt = k.getTime();

        String date = sdf.format(tt);

        final String uId = ref.push().getKey();

        adminconst adminconst = new adminconst(un, email, password, cp, uId, date);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            Toast.makeText(adminSignup.this,
                                    "Sign up failed. Please try again", Toast.LENGTH_SHORT).show();

                            pbar.setVisibility(View.GONE);

                            Log.d(tag, "sign up with email was unsuccessful" + task.getException());

                        } else {

                            assert uId != null;
                            ref.child(uId).setValue(adminconst).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        backToMainpage();

                                        Toast.makeText(adminSignup.this,
                                                "Admin added successfully", Toast.LENGTH_SHORT).show();

                                        pbar.setVisibility(View.GONE);

                                        Log.d(tag, "account details saved successfully");

                                    } else {
                                        String mess = task.getException().getMessage();

                                        Toast.makeText(adminSignup.this,
                                                "sign up unsuccessful. Please try again"
                                                , Toast.LENGTH_SHORT).show();

                                        pbar.setVisibility(View.GONE);

                                        Log.d(tag, "sign up with email was unsuccessful" + mess);
                                    }
                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(adminSignup.this, "sign up failed", Toast.LENGTH_SHORT).show();

                        Log.d(tag, "creating an account failed" + e.getMessage());
                    }
                });

    }

    public void backToMainpage() {
        Intent o = new Intent(adminSignup.this, accDet.class);
        startActivity(o);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMainpage();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}