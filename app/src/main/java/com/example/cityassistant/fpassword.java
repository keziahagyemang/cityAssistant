package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class fpassword extends AppCompatActivity {

    AppCompatButton send;
    ProgressBar pbar;
    Toolbar t;
    TextInputLayout em;

    FirebaseAuth firebaseAuth;

    Snackbar[] snackbar;

    RelativeLayout fpa;


    final private static String tag = "forgot password ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpassword);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        t = findViewById(R.id.forgotpasswordToolbar);
        pbar = findViewById(R.id.progressFpassword);
        em = findViewById(R.id.emailFpassword);
        send = findViewById(R.id.btnsendemail);

        fpa = findViewById(R.id.fp);

        t.setTitle(" Forgot Password");
        t.setTitleMarginStart(100);

        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        getIntent();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetemail();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void resetemail() {


        String email = em.getEditText().getText().toString();


        // checks if the email is valid
        //Regular expression to accept valid email id
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        //Creating a pattern object
        Pattern pattern = Pattern.compile(regex);
        //Creating a Matcher object
        Matcher matcher = pattern.matcher(email);

        //Verifying whether given phone number is valid
        if (matcher.matches()) {
            Timber.tag(tag).d("email is valid");
        } else {
            // System.out.println("Given email id is not valid");
            em.setError("email is not valid");
            em.requestFocus();

        }


        if (TextUtils.isEmpty(email)) {

            em.setError(" provide an email to reset your password");

        } else {

            if (checknetwork.isNetworkAvailable(fpassword.this)) {

                pbar.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    pbar.setVisibility(View.GONE);

                                    Toast.makeText(fpassword.this,
                                            "Please check your email,  a reset link has been sent to you", Toast.LENGTH_SHORT).show();

                                    Log.d(tag, " password reset successful, email sent ");

                                    onBack();

                                } else {

                                    pbar.setVisibility(View.GONE);

                                    Snackbar.make(fpa, " sending reset link to email failed, try again",
                                            Snackbar.LENGTH_SHORT).show();

                                    Log.d(tag, "sign up with email was unsuccessful" + task.getException().getMessage());

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Snackbar.make(fpa, " password reset failed",
                                        Snackbar.LENGTH_SHORT).show();

                                Log.d(tag, "resetting the account password with email failed" + e.getMessage());
                            }
                        });
            } else {

                Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void onBack() {
        Intent i = new Intent(fpassword.this, login.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        onBack();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

}