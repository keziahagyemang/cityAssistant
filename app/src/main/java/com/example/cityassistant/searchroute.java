package com.example.cityassistant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import timber.log.Timber;

public class searchroute extends AppCompatActivity {

    private static final String tag = "find a route ";
    
    Toolbar tt;
    EditText from, to;
    AppCompatButton search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchroute);
        
        
        tt = findViewById(R.id.routeToolbar);
        
        setSupportActionBar(tt);
        tt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMap();
            }
        });


        from = findViewById(R.id.fromEditText);
        to = findViewById(R.id.toEditText);
        search = findViewById(R.id.searchDir);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provideRoute();
            }
        });


    }

    public void provideRoute(){
        String fr = from.getText().toString().trim();
        String t = to.getText().toString().trim();

        if( TextUtils.isEmpty(fr) && TextUtils.isEmpty(t)){
            Toast.makeText(this, "please provide source and destination", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(fr)){
            Toast.makeText(this, "please provide current location", Toast.LENGTH_SHORT).show();
        }


        if(TextUtils.isEmpty(t)){
            Toast.makeText(this, "please provide destination location", Toast.LENGTH_SHORT).show();
        }

        if( TextUtils.isEmpty(fr) && TextUtils.isEmpty(t) ){

            if (checknetwork.isNetworkAvailable(searchroute.this)) {

                Timber.tag(tag).d(" source and destination has been provided");

               processInput(fr, t);

            } else {

                Toast.makeText(searchroute.this,
                        "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void processInput(String f, String d){

    }
    
    public void toMap(){
        Intent t = new Intent(searchroute.this, settings.class);
        startActivity(t);
        finish();
        
    }

    @Override
    public void onBackPressed() {
        
        toMap();
        super.onBackPressed();
    }
}
