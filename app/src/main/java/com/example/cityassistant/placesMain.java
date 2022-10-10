package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import timber.log.Timber;

public class placesMain extends AppCompatActivity {

    Toolbar tt;

    public static final String tag = "places viewing ";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    LinearLayoutManager mlayout;
    RecyclerView recycl;
    ArrayList<checkTheme> ct;
    recviewplaces recviewplaces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_main);


        tt = findViewById(R.id.toolbarPlacesMain);

        tt.setTitle(" Locations");

        tt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toadminpage();
            }
        });


        mlayout = new LinearLayoutManager(placesMain.this);
        mlayout.setStackFromEnd(true);
        mlayout.setReverseLayout(true);

        recycl = findViewById(R.id.recycPlacesMain);
        recycl.setLayoutManager(mlayout);

        ct = new ArrayList<>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Places");
    }

    @Override
    protected void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    checkTheme ch = ds.getValue(checkTheme.class);
                    ct.add(ch);
                }

                recviewplaces = new recviewplaces(placesMain.this, ct);
                recycl.setAdapter(recviewplaces);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String der = error.getMessage();

                Toast.makeText(placesMain.this, "error" + der,
                        Toast.LENGTH_SHORT).show();

                Timber.tag(tag).d(der);
            }
        });


    }


    public void toadminpage() {
        Intent i = new Intent(placesMain.this, accDet.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {

        toadminpage();

        super.onBackPressed();
    }


}