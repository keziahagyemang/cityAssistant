package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import timber.log.Timber;

public class signup extends AppCompatActivity {

    Toolbar t;
    ProgressBar pbar;
    TextInputLayout username, em, pword, confpword;
    AppCompatButton create;


    SharedPreferences sp;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference ref;

    Snackbar[] snackbar;

    LinearLayout su;

    final private static String tag = "create an account";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        t = findViewById(R.id.signupToolbar);
        pbar = findViewById(R.id.progressSignup);
        username = findViewById(R.id.etUsername);
        // ph = findViewById(R.id.etPhoneNumber);
        em = findViewById(R.id.etemail);
        pword = findViewById(R.id.etpword);
        confpword = findViewById(R.id.etConfirmpword);
        create = findViewById(R.id.btnCreateAcc);
        su = findViewById(R.id.signupActivity);

        t.setTitle(" Sign Up ");
        t.setTitleMarginStart(200);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToSettings();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                f();

                Timber.tag(tag).d("sign up button has been pressed");
            }
        });


        sp = getSharedPreferences("MyNewUserInf", Context.MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        ref = rootNode.getReference("Users");


    }

    public void f() {
        String n = username.getEditText().getText().toString().trim();
        String e = em.getEditText().getText().toString().trim();
        String p = pword.getEditText().getText().toString().trim();
        String c = confpword.getEditText().getText().toString().trim();

        checknetwork.checkInfo(n, e, p, c, username, em, pword, confpword, su, tag);

        if (checknetwork.checkInfo(n, e, p, c, username, em, pword, confpword, su, tag)) {
            if (checknetwork.isNetworkAvailable(signup.this)) {

                processDetails(n, e, p, c);

                pbar.setVisibility(View.VISIBLE);

            } else {

                Toast.makeText(signup.this,
                        "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }


    }


    public void createAcc() {

        String u = username.getEditText().getText().toString().trim();
        // String p = ph.getEditText().getText().toString().trim();
        String email = em.getEditText().getText().toString().trim();
        String pasw = pword.getEditText().getText().toString().trim();
        String c = confpword.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(u) && TextUtils.isEmpty(email)
                && TextUtils.isEmpty(pasw) && TextUtils.isEmpty(c)) {

            Snackbar.make(su,
                    "Fields are empty. Please provide details", Snackbar.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(u)) {
            username.setError(" provide a username");
        }

        if (TextUtils.isEmpty(email)) {
            em.setError(" provide an email address");
        }

        if (TextUtils.isEmpty(pasw)) {
            pword.setError(" provide a password");

        } else if (pasw.length() < 8) {
            pword.setError(" passwords should be eight(8) or more characters ");

        }

        if (TextUtils.isEmpty(c)) {
            confpword.setError(" Please confirm the password");

        } else if (!c.equals(pasw)) {
            confpword.setError("Passwords are not the same. Re-confirm the password");
        }


        if (TextUtils.isEmpty(c) && TextUtils.isEmpty(pasw)) {
            Snackbar.make(su,
                    "please provide a password and confirm it", Snackbar.LENGTH_SHORT).show();
        }

        if (!TextUtils.isEmpty(u) && !TextUtils.isEmpty(email) && pasw.length() >= 8
                && c.equals(pasw)) {

            if (checknetwork.isNetworkAvailable(signup.this)) {
                // Log.d(tag, " all inputs have been filled");

                // checkEditText(u, email, pasw, c);;

                // processDetails(u, email, pasw, c);

                // pbar.setVisibility(View.VISIBLE)
            } else {

                Toast.makeText(signup.this,
                        "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void processDetails(String u, String email, String pasw, String c) {

        Calendar k = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        Date tt = k.getTime();

        String date = sdf.format(tt);

        final String uId = ref.push().getKey();

        loginDetails loginDetails = new loginDetails(u, email, pasw, c, date, uId);

        firebaseAuth.createUserWithEmailAndPassword(email, pasw).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {

                    Toast.makeText(signup.this,
                            "Sign up failed. Please try again", Toast.LENGTH_SHORT).show();

                    pbar.setVisibility(View.GONE);

                    Timber.tag(tag).d("sign up with email was unsuccessful%s", task.getException());

                } else {

                    assert uId != null;
                    ref.child(uId).setValue(loginDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                showMap();

                                Toast.makeText(signup.this,
                                        "successful", Toast.LENGTH_SHORT).show();

                                pbar.setVisibility(View.GONE);

                                //get user's uid to sharedpref
                                SharedPreferences.Editor editor1 = sp.edit();

                                String d = uId;

                                editor1.putString("newuser", d);
                                editor1.apply();

                                Timber.tag(tag).d("details taken%s", d);


                                Timber.tag(tag).d("account details saved successfully");

                            } else {

                                String mess = task.getException().getMessage();

                                Toast.makeText(signup.this,
                                        "sign up unsuccessful. Please try again"
                                        , Toast.LENGTH_SHORT).show();

                                pbar.setVisibility(View.GONE);

                                Timber.tag(tag).d("sign up with email was unsuccessful%s", mess);

                            }

                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this, "sign up failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

                Timber.tag(tag).d("creating an account failed%s", e.getMessage());
            }
        });


    }


    public void showMap() {
        Intent i = new Intent(signup.this, settings.class);
        startActivity(i);
        finish();
    }

    public void backToSettings() {
        Intent o = new Intent(signup.this, login.class);
        startActivity(o);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToSettings();
        super.onBackPressed();
    }


}