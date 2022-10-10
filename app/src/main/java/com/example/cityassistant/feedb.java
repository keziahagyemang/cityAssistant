package com.example.cityassistant;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class feedb extends AppCompatActivity {

    private static final String tag = "feedback activity";

    AppCompatButton send;
    TextInputLayout feedback;
    Toolbar tt;
    ProgressBar pbar;

    FirebaseDatabase rootNode;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedb);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        send = findViewById(R.id.btnFeedb);
        feedback = findViewById(R.id.EditTextFeedback);
        tt = findViewById(R.id.feedbToolbar);
        pbar = findViewById(R.id.progressFeedb);

        tt.setTitle(" Feedbacks / Issues");
        tt.setTitleMarginStart(100);

        tt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToSettings();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkFeedback();

            }
        });


    }

    public void checkFeedback() {
        String f = feedback.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(f)) {
            Toast.makeText(this, "Textbox is empty. please provide feedback or issue",
                    Toast.LENGTH_SHORT).show();
        } else {

            if (checknetwork.isNetworkAvailable(feedb.this)) {

                Timber.tag(tag).d(" all inputs have been filled");

                saveFeedback(f);

                pbar.setVisibility(View.VISIBLE);
            } else {

                Toast.makeText(feedb.this, "No internet connection",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void saveFeedback(String f) {

        rootNode = FirebaseDatabase.getInstance();
        ref = rootNode.getReference("Feedback");

        Calendar k = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        Date tt = k.getTime();

        String date = sdf.format(tt);

        final String uId = ref.push().getKey();

        feedbackconst feedbackconst = new feedbackconst(f, uId, date);

        assert uId != null;
        ref.child(uId).setValue(feedbackconst).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(feedb.this, "sent successfully", Toast.LENGTH_SHORT).show();

                Timber.tag(tag).d("sent successfully");
                backToSettings();

                pbar.setVisibility(View.GONE);

            } else {

                Toast.makeText(feedb.this, "error occurred" + task.getException(),
                        Toast.LENGTH_SHORT).show();

                pbar.setVisibility(View.GONE);

                Timber.tag(tag).d(task.getException());
            }

        }).addOnFailureListener(e -> {

            Toast.makeText(feedb.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            pbar.setVisibility(View.GONE);

            Timber.tag(tag).d(e);
        });

    }


    public void backToSettings() {
        Intent n = new Intent(feedb.this, settings.class);
        startActivity(n);
        finish();
    }

    @Override
    public void onBackPressed() {

        backToSettings();
        super.onBackPressed();
    }



}