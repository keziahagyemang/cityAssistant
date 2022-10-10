package com.example.cityassistant;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    final public static String tag = " Splash screen coming up";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        setDelaytime();

    }


    public void setDelaytime() {

        int SPLASH_TIME_OUT = 2000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                toMainPage();

                //getShPref();

                Timber.tag(tag).d("time out of launch screen ");

            }
        }, SPLASH_TIME_OUT);

    }

    public void getShPref() {

        SharedPreferences ss = getApplicationContext()
                .getSharedPreferences("MyUserInf", Context.MODE_PRIVATE);

        SharedPreferences d = getApplicationContext()
                .getSharedPreferences("MyNewUserInf", Context.MODE_PRIVATE);

        String ui = ss.getString("user", "");
        String newU = d.getString("newuser", "");

        try {

            if (TextUtils.isEmpty(ui) || TextUtils.isEmpty(newU)) {

                Timber.tag(tag).d("user is new or has not logged in");

                toLogin();
            }

            if(! TextUtils.isEmpty(ui) || ! TextUtils.isEmpty(newU)){

                Timber.tag(tag).d("user is already logged in");

                toMainPage();
            }


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //check if user has already logged in
    public void toLogin() {
        Intent it = new Intent(MainActivity.this, login.class);
        startActivity(it);
        finish();

        Timber.tag(tag).d(" user is not logged in ");
    }


    //use shared preference to sign in user automatically
    public void toMainPage() {
        Intent inte = new Intent(MainActivity.this, settings.class);
        startActivity(inte);
        finish();

        Timber.tag(tag).d(" user has logged in ");

    }

}
