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

public class feedbMain extends AppCompatActivity {

    Toolbar tt;

    public static final String tag = "feedback viewing ";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    LinearLayoutManager mlayout;
    RecyclerView recyc;
    ArrayList<feedbackconst> fb;
    recviewfeedb recviewfeedb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedb_main);

        tt = findViewById(R.id.toolbarFeedbMain);

        tt.setTitle(" Feedbacks / Issues");

        tt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toadminpage();
            }
        });

        mlayout = new LinearLayoutManager(feedbMain.this);
        mlayout.setStackFromEnd(true);
        mlayout.setReverseLayout(true);

        recyc = findViewById(R.id.recycfeedb);
        recyc.setLayoutManager(mlayout);

        fb = new ArrayList<>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Feedback");
    }


    @Override
    protected void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    feedbackconst f = ds.getValue(feedbackconst.class);
                    fb.add(f);
                }

                recviewfeedb = new recviewfeedb(feedbMain.this, fb);
                recyc.setAdapter(recviewfeedb);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String der = error.getMessage();

                Toast.makeText(feedbMain.this, "error" + der,
                        Toast.LENGTH_SHORT).show();

                Timber.tag(tag).d(der);
            }
        });


    }

    public void toadminpage() {
        Intent i = new Intent(feedbMain.this, accDet.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {

        toadminpage();

        super.onBackPressed();
    }
}