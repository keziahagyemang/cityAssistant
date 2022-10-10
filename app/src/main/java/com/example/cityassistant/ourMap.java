package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import timber.log.Timber;

public class ourMap extends AppCompatActivity {


    int c = 0;

    String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};

    EditText search;
    ImageButton sett, curLocation, direction, searchLocation;
    LinearLayout stt;
    ProgressBar pbar;

    public static String tag = " our map activity";


    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference ref, adminref, refLoc;
    FirebaseUser user;
    private String userID;
    String locationId;

    private final List<LatLng> latLngList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_map);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        search = findViewById(R.id.editSearch);
        sett = findViewById(R.id.settings);
        curLocation = findViewById(R.id.getCurDir);
        direction = findViewById(R.id.direButton);
        searchLocation = findViewById(R.id.searchOurMap);

        // pbar = findViewById(R.id.progressForAdminAuth);


        sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add an adapter to this
                openDialog();
            }
        });

        curLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestPermissions();

            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ourMap.this, "under development", Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sett.setVisibility(View.GONE);
                searchLocation.setVisibility(View.VISIBLE);

                searchLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchText(search.getText().toString());
                    }
                });

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        refLoc = rootNode.getReference("Places");
        locationId = refLoc.getKey();
        adminref = rootNode.getReference("Admin");
        ref = rootNode.getReference("Users");
        user = firebaseAuth.getCurrentUser();
        assert user != null;
        userID = user.getUid();

    }


    // get string of the user
    public void searchText(String str) {


    }

    //when a user presses on the settings icon
    public void openDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        AppCompatButton logout, feedb, addDir;
        ImageButton close;

        AlertDialog.Builder builder = new AlertDialog.Builder(ourMap.this);

        View view = LayoutInflater.from(ourMap.this).inflate(R.layout.activity_settings, viewGroup, false);
        builder.setCancelable(true);

        builder.setView(view);

       /*
        close = view.findViewById(R.id.closebutton);
        addDir = view.findViewById(R.id.addlocSett);
        feedb = view.findViewById(R.id.feedbSett);
        logout = view.findViewById(R.id.LogoutSett);





        AlertDialog alertDialog = builder.create();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        addDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pbar.setVisibility(View.VISIBLE);


                // check database if the user is an admin
                //get the details of the user from the admin table
                adminref.child("uId").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        adminconst adm = snapshot.getValue(adminconst.class);

                        assert adm != null;

                        Query dda = adminref
                                .orderByChild("uId")
                                .equalTo(userID);

                        if (userID.equals(dda.toString())) {
                            Timber.tag(tag).d("user is an admin");

                            pbar.setVisibility(View.GONE);

                            Toast.makeText(ourMap.this, "Welcome", Toast.LENGTH_SHORT).show();

                            toAddALocation();

                        } else if (!userID.equals(dda.toString())) {
                            Toast.makeText(ourMap.this, "you are not an admin",
                                    Toast.LENGTH_SHORT).show();

                            pbar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(ourMap.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        feedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ourMap.this, feedb.class);
                startActivity(i);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ourMap.this);
                AlertDialog ald = builder.create();

                builder.setMessage("Do you want to log out?")
                        .setTitle("Logout?")
                        .setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        userLogout();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

                ald.show();

            }
        });

        alertDialog.show();

        */


    }

    public void onalocationPressed(Double lat, Double lng) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ourMap.this);
        View view = LayoutInflater.from(ourMap.this).inflate(R.layout.locationinfo, null);
        builder.setView(view);

        AlertDialog alertDialog1 = builder.create();
        alertDialog1.setCancelable(false);


        ImageButton close = findViewById(R.id.closebtnLocInfo);
        ImageView image = findViewById(R.id.imageLocInfo);
        AppCompatButton directions = findViewById(R.id.directionLocInfo);
        TextView name = findViewById(R.id.nameLocInfo);
        TextView typeOfInstitution = findViewById(R.id.typeOfInstitutionLocInfo);

        refLoc.child(locationId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkTheme ct = snapshot.getValue(checkTheme.class);

                assert ct != null;

                name.setText(ct.getNameOfPlace());
                typeOfInstitution.setText(ct.getTypeOfInstitution());
                Glide.with(ourMap.this).load(ct.getImageUri()).into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ourMap.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        alertDialog1.show();

    }


    // location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permissionGranted = true;

        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PERMISSION_GRANTED) {
                    permissionGranted = false;
                    break;
                }
            }
        } else {
            //PERMISSION REQ
            permissionGranted = false;
        }

        if (!permissionGranted) {


            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

            builder1.setTitle("Please grant permission to location to continue")
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

        } else {

            addMarkerToUserLocation();
        }
    }

    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUIRED[0]) == PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUIRED[1]) == PERMISSION_GRANTED) {
            //onMapReady(mapplsMap);
            addMarkerToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_REQUIRED[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_REQUIRED[1])
            ) {

                //showPermissionRequiredDialog
                new AlertDialog.Builder(this)
                        .setTitle("Please Accept all the permissions")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("OK", (dialog, which) ->
                                ActivityCompat.requestPermissions(ourMap.this, PERMISSIONS_REQUIRED, 100))
                        .show();
            } else {

                //askPermission
                ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, 100);
            }
        }
    }

    public void addMarkerToUserLocation() {


    }

    public void checkAdminStatus(String email, String password) {


        Query dda = adminref
                .orderByChild("uId")
                .equalTo(userID);

        Query ddu = ref
                .orderByChild("uId")
                .equalTo(userID);

        if (userID.equals(dda.toString())) {
            Timber.tag(tag).d("user is an admin");

            pbar.setVisibility(View.GONE);

            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();

            toAddALocation();

        }


        if (userID.equals(ddu.toString())) {
            Timber.tag(tag).d("user is not an admin");

            Toast.makeText(ourMap.this, "you are not an admin",
                    Toast.LENGTH_SHORT).show();

            pbar.setVisibility(View.GONE);
        }

    }


    public void authAdmin(DataSnapshot snapshot) {
        loginDetails ld = snapshot.getValue(loginDetails.class);

        assert ld != null;

        String e = ld.getE();
        String p = ld.getPasw();

        checkAdminStatus(e, p);


    }

    public void userLogout() {

        firebaseAuth.signOut();

        Intent n = new Intent(ourMap.this, login.class);
        startActivity(n);
        finish();
    }


    public void toAddALocation() {
        Intent n = new Intent(ourMap.this, accDet.class);
        startActivity(n);
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();


        // get a snapshot from the user table
        /*
        ref.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                authAdmin(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ourMap.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

         */

        Timber.tag(tag).d("on start");
    }


    @Override
    public void onBackPressed() {

        c = c + 1;

        if (c == 1) {
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();
        } else if (c >= 2) {
            super.onBackPressed();
        }

        Timber.tag(tag).d("app exited");
    }


}