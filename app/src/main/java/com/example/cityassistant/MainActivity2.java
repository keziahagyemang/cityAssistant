package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.maps.Style;

import java.util.List;

import timber.log.Timber;

public class MainActivity2 extends AppCompatActivity implements  PermissionsListener, LocationEngineCallback<LocationEngineResult> {

    String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final String tag = "main activity 2";
    Toolbar t;



    MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private Location userOrigin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main2);
        mapView = findViewById(R.id.mapViewforNavigation);



        t = findViewById(R.id.navigationToolbar);
        setSupportActionBar(t);

        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backtoMap();

            }
        });


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

        // present a dialog or a toast on why the user should allow for the permissions
    }

    public void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // initLocationComponent();

           // initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {

            enableLocation();

        } else {

            Timber.tag(tag).d(" permissions were denied");
            finish();
        }
    }

    @Override
    public void onSuccess(LocationEngineResult result) {

        // The LocationEngineCallback interface's method which fires when the device's location has changed.
        Location lastLocation = result.getLastLocation();

        if (lastLocation != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(),
                    lastLocation.getLongitude()), 13.0));
        }


    }

    @Override
    public void onFailure(@NonNull Exception exception) {

        // The LocationEngineCallback interface's method which fires when the device's location can not be captured
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();

        Timber.tag(tag).d(exception);
    }

    public void backtoMap() {
        Intent n = new Intent(MainActivity2.this, settings.class);
        startActivity(n);
        finish();
    }


    @Override
    public void onBackPressed() {
        backtoMap();
        super.onBackPressed();
    }




}