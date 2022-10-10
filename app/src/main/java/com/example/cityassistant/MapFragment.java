package com.example.cityassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MapFragment extends Fragment {

/*
    private static final String tag = "our map fragment";

    private MapView mapView;




    MapplsAccountManager mapplsAccountManager = MapplsAccountManager.getInstance();




 */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*
        mapplsAccountManager.setRestAPIKey(getRestAPIKey());
        mapplsAccountManager.setMapSDKKey(getMapSDKKey());
        mapplsAccountManager.setAtlasClientId(getAtlasClientId());
        mapplsAccountManager.setAtlasClientSecret(getAtlasClientSecret());
        //Mappls.getInstance(this);


        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

 */

    }

/*
    public String getMapSDKKey() {

        Log.d(tag, "retrieving MapSDKKey");

        return "53b6387f19367f93621fafdc19de70da";

    }

    public String getRestAPIKey() {

        Log.d(tag, "retrieving RestAPIKey ");

        return "53b6387f19367f93621fafdc19de70da";
    }

    public String getAtlasClientSecret() {
        Log.d(tag, "retrieving AtlasClientSecret");

        return " lrFxI-iSEg-rOctzIvMuCAQXOym5UFR7e7ZRehd02axrKfF600O9aIMIfmk1I4trRB4ZnFHaEVyKcDowb7_DBb3zth2K3gmuWvZCXuiuecw";
    }

    public String getAtlasClientId() {
        Log.d(tag, "retrieving AtlasClientId");

        return "OkryzDZsLGNQZH78-Y52UgztWggmGBTo0iTRm1InIxHiOGjo1DoZa_a0gPtl4PmyqTvtekWllFsqVp4B4EWLj9N66TOGL2";
    }




    @Override
    public void onMapReady(@NonNull MapplsMap mapplsMap) {


        CameraPosition position = new CameraPosition.Builder()  // Sets the new camera position
                .target(new LatLng(7.334941, -2.312303)) // Sunyani, Ghana
                .zoom(14) // Sets the zoom to level 14
                .tilt(45) // Set the camera tilt to 45 degrees
                .build();
        mapplsMap.setCameraPosition(position);


        mapplsMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(7.334941, -2.312303), 14));
        mapplsMap.easeCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(7.334941, -2.312303), 14));
        mapplsMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(7.334941, -2.312303), 14));

/*
        mapplsMap.addOnMapClickListener(new MapplsMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {

                String string = String.format(Locale.US, "User clicked at: %s", point.toString());
                Toast.makeText(ourMap.this, string, Toast.LENGTH_LONG).show();
                return false;

            }
        });




    }

    @Override
    public void onMapError(int i, String s) {


    }

*/


}