package com.example.cityassistant;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import timber.log.Timber;


public class accDet extends AppCompatActivity {

    int c = 0;

    final public static String tag = "Admin main  page ";

    Toolbar t;
    AppCompatButton logout, addPlace, AddAdmin, ViewLocations, viewFeedbacks;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_det);

        t = findViewById(R.id.accDetToolbar);
        logout = findViewById(R.id.AccDetLogout);
        addPlace = findViewById(R.id.addPlaceAccDet);
        AddAdmin = findViewById(R.id.addAnAdminAccDet);
        ViewLocations = findViewById(R.id.viewLocationsAccDet);
        viewFeedbacks = findViewById(R.id.viewfeedbacksAdmin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        t.setTitle(" Admin");
        t.setTitleMarginStart(200);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backtoMap();

            }
        });




        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(accDet.this, addloc.class);
                startActivity(i);
                finish();
            }
        });

        AddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(accDet.this, adminSignup.class);
                startActivity(i);
                finish();
            }
        });

        ViewLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(accDet.this, placesMain.class);
                startActivity(i);
                finish();


            }
        });

        viewFeedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(accDet.this, feedbMain.class);
                startActivity(r);
                finish();
            }
        });

    }


    public void backtoMap() {
        Intent i = new Intent(accDet.this, settings.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onBackPressed() {

        c = c + 1;

        if (c == 1) {
            Toast.makeText(this, "press again to go back to the map",
                    Toast.LENGTH_SHORT).show();
        } else if (c >= 2) {

            backtoMap();
            super.onBackPressed();
        }

        Timber.tag(tag).d("app exited");
    }


}