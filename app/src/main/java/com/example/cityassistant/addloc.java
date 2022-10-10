package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class addloc extends AppCompatActivity {

    TextInputLayout nop, desc, lat, lng;
    Toolbar tt;
    ProgressBar pbar;
    AppCompatButton addPlace;
    ImageButton addImage;
    ImageView imageView;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference ref;
    FirebaseUser user;
    private String placeId;
    private String userId;

    private static final int permissionCodes = 1001;
    private static final int pickImage = 1000;
    Uri imagePath;

    private StorageReference mStorageref;

    final private static String tag = "add a place";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addloc);

        nop = findViewById(R.id.nameOfPlaceAddDir);
        desc = findViewById(R.id.DescripAddDir);
        lat = findViewById(R.id.latAddDir);
        lng = findViewById(R.id.lngAddDir);

        tt = findViewById(R.id.toolbarAddloc);
        pbar = findViewById(R.id.progressAddloc);
        addPlace = findViewById(R.id.btnAddPlace);
        addImage = findViewById(R.id.addImageAddDir);
        imageView = findViewById(R.id.imageAddDir);

        tt.setTitle(" Add A Place ");
        tt.setTitleMarginStart(200);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMap();
            }
        });

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkInfo();

            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkImagePerm();

            }

        });

        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        ref = rootNode.getReference("Places");
        user = firebaseAuth.getCurrentUser();
      //  userId = user.getUid();
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        mStorageref = mStorage.getReference("locations");
        placeId = ref.push().getKey();

    }

    public void checkInfo() {
        String name = nop.getEditText().getText().toString();
        String typeOfBusiness = desc.getEditText().getText().toString();
        Double ltd = Double.valueOf(lat.getEditText().getText().toString());
        Double lgd = Double.valueOf(lng.getEditText().getText().toString());


/*
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(typeOfBusiness) &&
                TextUtils.isEmpty(ltd) && TextUtils.isEmpty(lgd)) {
            Toast.makeText(this, " The fields are empty. Please provide details",
                    Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(name)) {
            nop.setError("please provide name of the place");
        }

        if (TextUtils.isEmpty(typeOfBusiness)) {
            nop.setError("please provide the classification of the place");
        }

        if (TextUtils.isEmpty(ltd)) {
            nop.setError("please provide the latitude of the place");
        }

        if (TextUtils.isEmpty(lgd)) {
            nop.setError("please provide the longitude of the place");
        }

        if (TextUtils.isEmpty(ltd) && TextUtils.isEmpty(lgd)) {
            Toast.makeText(this, " please provide geographic coordinates of the place",
                    Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(ltd) || TextUtils.isEmpty(lgd)) {
            Toast.makeText(this, "provide the latitude and longitude of the place",
                    Toast.LENGTH_SHORT).show();
        }


 */


        checknetwork.checkInfoAddPlace(name, typeOfBusiness, ltd, lgd, nop, desc, lat,
                lng, addloc.this, tag);

        if (checknetwork.checkInfoAddPlace(name, typeOfBusiness, ltd, lgd, nop, desc, lat,
                lng, addloc.this, tag)) {

            if (checknetwork.isNetworkAvailable(addloc.this)) {

                Timber.tag(tag).d(" all inputs have been filled");

                checkImageToProceed(name, typeOfBusiness, ltd, lgd);

                pbar.setVisibility(View.VISIBLE);
            } else {

                Toast.makeText(addloc.this, "No internet connection",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void checkImageToProceed(String n, String t, Double ld, Double lg) {

        if (imagePath != null) {

            pbar.setVisibility(View.VISIBLE);
            addImage.setVisibility(View.INVISIBLE);

            StorageReference img = mStorageref.child(placeId);
            img.putFile(imagePath).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(addloc.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Timber.tag(tag).d(e.getMessage());

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Calendar k = Calendar.getInstance();
                            SimpleDateFormat sdf =
                                    new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
                            Date tt = k.getTime();

                            String date = sdf.format(tt);
                            String imageUri = uri.toString();
                            String userWhoPosted = userId;

                            checkTheme checkTheme = new checkTheme(ld, lg, n, t, placeId, imageUri,
                                    userWhoPosted, date);

                            ref.child(placeId).setValue(checkTheme)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(addloc.this, "place added successfully",
                                                        Toast.LENGTH_SHORT).show();

                                                Timber.tag(tag).d("place added successfully");

                                                backToMap();

                                                pbar.setVisibility(View.GONE);

                                            } else {
                                                Toast.makeText(addloc.this, "error occurred" + task.getException(),
                                                        Toast.LENGTH_SHORT).show();

                                                Timber.tag(tag).d(task.getException().getMessage());

                                                pbar.setVisibility(View.GONE);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(addloc.this, e.toString(), Toast.LENGTH_SHORT).show();

                                            Timber.tag(tag).e(e.getMessage());

                                            pbar.setVisibility(View.GONE);

                                        }
                                    });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addloc.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            Timber.tag(tag).e(e.getMessage());

                            pbar.setVisibility(View.GONE);
                        }
                    });

                    Toast.makeText(addloc.this, " successful", Toast.LENGTH_SHORT).show();

                    Timber.tag(tag).d("  image was successfully uploaded");

                }
            });

        } else {
            Toast.makeText(this, "Please add an image", Toast.LENGTH_SHORT).show();

            Timber.tag(tag).d(" no image has been added");
        }

    }

    public void checkImagePerm() {

        // check permission to use device's camera
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {

            // permission has not been granted, request for permission
            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

            // show popup for the permission
            requestPermissions(new String[]{Arrays.toString(permissions)}, permissionCodes);

        } else {
            // permission already granted
            pickImageFromGallery();
            Timber.tag(tag).d("permission has already been granted");
        }

    }

    public void pickImageFromGallery() {
        Intent intt = new Intent(Intent.ACTION_PICK);
        intt.setType("image/*");
        startActivityForResult(intt, pickImage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionCodes) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted
                pickImageFromGallery();
            } else {

                pickImageFromGallery();
                //permission was denied
                // Toast.makeText(this, "permission denied. provide storage permission", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == pickImage && data != null) {
            imagePath = data.getData();
            imageView.setImageURI(imagePath);
            addImage.setVisibility(View.INVISIBLE);

        } else {
            Toast.makeText(this, "please select file", Toast.LENGTH_SHORT).show();
        }
    }

    public void backToMap() {
        Intent t = new Intent(addloc.this, accDet.class);
        startActivity(t);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMap();
        super.onBackPressed();
    }


}