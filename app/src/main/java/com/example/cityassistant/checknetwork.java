package com.example.cityassistant;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class checknetwork {




    /* function to check internet connection*/
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null;
    }


    // check info inputted by user
    public static boolean checkInfo(String u, String e, String p, String c, TextInputLayout uname,
                                    TextInputLayout ema, TextInputLayout pasw, TextInputLayout confp,
                                    LinearLayout su, String tag) {

        if (TextUtils.isEmpty(u) && TextUtils.isEmpty(e) && TextUtils.isEmpty(p) && TextUtils.isEmpty(c)) {

            Snackbar.make(su,
                    "Fields are empty. Please provide details", Snackbar.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(u)) {
            uname.setError(" provide a username");

            return false;
        }

        // checks if the email is valid
        //Regular expression to accept valid email id
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        //Creating a pattern object
        Pattern pattern = Pattern.compile(regex);
        //Creating a Matcher object
        Matcher matcher = pattern.matcher(e);

        //Verifying whether given phone number is valid
        if (matcher.matches()) {
            Timber.tag(tag).d("email is valid");
        } else {
            // System.out.println("Given email id is not valid");
            ema.setError("email is not valid");
            ema.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(e)) {
            ema.setError(" provide an email address");
            ema.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(p)) {
            pasw.setError(" provide a password");
            pasw.requestFocus();
            return false;

        } else if (p.length() < 8) {
            pasw.setError(" passwords should be eight(8) or more characters ");
            pasw.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(c)) {
            confp.setError(" Please confirm the password");
            confp.requestFocus();
            return false;

        } else if (!c.equals(p)) {
            confp.setError("Passwords are not the same. Re-confirm the password");
            confp.requestFocus();
            return false;
        }


        if (TextUtils.isEmpty(c) && TextUtils.isEmpty(p)) {
            Snackbar.make(su,
                    "please provide a password and confirm it", Snackbar.LENGTH_SHORT).show();

            return false;
        }

        if (!TextUtils.isEmpty(u) && !TextUtils.isEmpty(e) && matcher.matches()
                && !TextUtils.isEmpty(c) && p.length() >= 8 && p.equals(c)) {

            Log.d(tag, " all inputs have been filled");


            return true;

        }

        return false;

    }

    public static boolean checkInfoAddPlace(String name, String desc, Double latitude, Double longitude,
                                            TextInputLayout nop, TextInputLayout des, TextInputLayout ltd,
                                            TextInputLayout lgd, Context context, String tag) {

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(desc) &&
                latitude.equals(0.00) && longitude == 0.00) {

            Toast.makeText(context, " The fields are empty. Please provide details",
                    Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(name)) {
            nop.setError("please provide name of the place");

            return false;
        }

        if (TextUtils.isEmpty(desc)) {
            des.setError("please provide the classification of the place");

            return false;
        }


        if (latitude.equals(0.00)) {
            ltd.setError("please provide the latitude of the place");

            return false;
        }

        if (longitude.equals(0.00)) {
            lgd.setError("please provide the longitude of the place");

            return false;
        }

        if (latitude.equals(0.00) && longitude.equals(0.00)) {
            Toast.makeText(context, " please provide geographic coordinates of the place",
                    Toast.LENGTH_SHORT).show();

            return false;
        }

        if (latitude.equals(0.00) && longitude == 0.00) {
            Toast.makeText(context, "provide both the latitude and longitude of the place",
                    Toast.LENGTH_SHORT).show();

            return false;
        }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc) &&
                !(latitude.equals(0.00)) && !(longitude == 0.00)) {

            Timber.tag(tag).d("All inputs have been filled");

            return true;
        }

        return false;

    }

    /*
    //provide direction for the user
    public void Directions(Context c, Context nextActivity) {
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try { 
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex) {}

        try 
        { 
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
        } catch(Exception ex) {}

        if(ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat
                    .requestPermissions(c, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else if(selectedCarmenFeature!=null && gps_enabled && network_enabled) {

            Intent intent = new Intent(this, nextActivity.);
            intent.putExtra("Json", selectedCarmenFeature.toJson());
            startActivity(intent);
        }
        else
        {
            Toast.makeText(c,"Turn on your location.",Toast.LENGTH_SHORT).show();
        }
    }

     */


}
