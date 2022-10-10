package com.example.cityassistant;


import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class login extends AppCompatActivity {

    Toolbar t;
    TextInputLayout pword, em;
    AppCompatButton login, fpassword;
    ProgressBar pbar;

    int c = 0;

    SharedPreferences sp;
    FirebaseAuth firebaseAuth;

    // FirebaseUser firebaseUser;

    String f;

    RelativeLayout lg;

    Snackbar[] snackbar;

    final public static String tag = "Login ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        t = findViewById(R.id.loginToolbar);
        em = findViewById(R.id.emailLogin);
        pword = findViewById(R.id.pwordLogin);
        login = findViewById(R.id.btnlogin);
        fpassword = findViewById(R.id.btnforgotpw);
        // signup = findViewById(R.id.btnSignup);
        pbar = findViewById(R.id.progressLogin);
        lg = findViewById(R.id.loginActivity);

        t.setTitle(" Log In As Admin");
        t.setTitleMarginStart(200);
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toForgotPassword();

                Timber.tag(tag).d(" forgot password pressed");

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkstuffd();

                Timber.tag(tag).d(" log in button pressed");
            }
        });

        /*
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignup();
            }
        });
         */

        //shared preference init

        //sp = getSharedPreferences("MyUserInf", Context.MODE_PRIVATE);


        firebaseAuth = FirebaseAuth.getInstance();



    }


    public void checkstuffd() {

        String email = em.getEditText().getText().toString().trim();
        String password = pword.getEditText().toString().trim();

        // checks if the email is valid
        //Regular expression to accept valid email id
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        //Creating a pattern object
        Pattern pattern = Pattern.compile(regex);
        //Creating a Matcher object
        Matcher matcher = pattern.matcher(email);

        //Verifying whether given phone number is valid
        if(matcher.matches()) {
            System.out.println("Given email id is valid");
        } else {
            System.out.println("Given email id is not valid");
        }


        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {

            Toast.makeText(login.this,
                    "Fields are empty. Please provide details", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(email)) {

            em.setError("provide an email");
            em.requestFocus();
        }

        if (TextUtils.isEmpty(password)) {
            pword.setError("provide a password");
            pword.requestFocus();

        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
        matcher.matches()) {

            if (checknetwork.isNetworkAvailable(login.this)) {

                pbar.setVisibility(View.VISIBLE);

                Timber.tag(tag).d(" all inputs have been filled");

                processDetails(email, password);

            } else {

                Toast.makeText(login.this,
                        "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void processDetails(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(login.this, task -> {
                    if (task.isSuccessful()) {

                        showMap();
                        pbar.setVisibility(View.GONE);

                        //get user's uid to sharedpref

                      /*
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("user", f);
                        editor.apply();
                       Timber.tag(tag).d("details taken%s", f);

                       */


                        Timber.tag(tag).d(" login successful");


                    } else {

                        Snackbar.make(lg,
                                "log in unsuccessful. Try again", Snackbar.LENGTH_SHORT).show();

                        pbar.setVisibility(View.GONE);

                        Timber.tag(tag).d("task not successful%s", task.getException());
                    }

                }).addOnFailureListener(e -> {

                    Snackbar.make(lg, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    pbar.setVisibility(View.GONE);
                    Timber.tag(tag).d(" log in failed%s", e);

                });
    }


    public void showMap() {
        Intent i = new Intent(login.this, accDet.class);
        startActivity(i);
        finish();
    }


    public void toForgotPassword() {

        Intent i = new Intent(login.this, fpassword.class);
        startActivity(i);
        finish();

    }

    public void backToLogin() {

        Intent i = new Intent(login.this, settings.class);
        startActivity(i);
        finish();

    }


    @Override
    public void onBackPressed() {

        backToLogin();
        super.onBackPressed();


    }


}