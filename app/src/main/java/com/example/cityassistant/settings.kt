package com.example.cityassistant

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.example.cityassistant.LocationListeningCallback
import android.widget.ImageButton
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.location.LocationComponent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.example.cityassistant.R
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import timber.log.Timber
import com.example.cityassistant.settings
import android.content.Intent
import com.example.cityassistant.feedb
import com.example.cityassistant.login
import androidx.appcompat.widget.AppCompatButton
import android.widget.TextView
import com.example.cityassistant.searchroute
import androidx.core.content.ContextCompat
import android.content.DialogInterface
import android.location.Location
import android.view.*
import android.widget.ImageView
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.example.cityassistant.checkTheme
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.Style.Companion.MAPBOX_STREETS
import java.util.ArrayList

class settings : AppCompatActivity(), PermissionsListener, OnMapReadyCallback {

    private val callback = LocationListeningCallback(this@settings)


    var PERMISSIONS_REQUIRED = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val mapb: MapboxMap? = null
    private var locationEngine: LocationEngine? = null
    private val locationComponentPlugin: LocationComponentPlugin? = null
    private val userOrigin: Location? = null

    var annotationApi: AnnotationPlugin? = null
    var pointAnnotationManager: PointAnnotationManager? = null
    var annotationConfig: AnnotationConfig? = null
    var marker: ArrayList<PointAnnotationManager>? = null

    private val origin: Point? = null
    private val destination: Point? = null
    private var permissionsManager: PermissionsManager? = null
    private val destinationMarker: Marker? = null
    private val locationComponent: LocationComponent? = null

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    var refLoc: DatabaseReference = rootNode.getReference("Places")
    var llat: Double? = null
    var llng: Double? = null
    var mylat: MutableList<Double>? = null
    var mylong: List<Double>? = null
    var locationId: String = refLoc.key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 7.334941, -2.312303 - sunyani's coordinates
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        // This contains the MapView in XML and needs to be called after the access token is configured.

        // Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.searchplaces)
        val mapView: MapView = findViewById(R.id.mapView)
        mapView.getMapboxMap().loadStyleUri(MAPBOX_STREETS,
            object : Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {
                addAnnotation()
                enableLocationComponent(style)
            }
        })

        val tt: Toolbar = findViewById(R.id.mapToolbar)
        val direction: ImageButton = findViewById(R.id.direButton)
        val currentLoc: ImageButton = findViewById(R.id.getCurDir)

        setSupportActionBar(tt)


        getCurDir.setOnClickListener{

        }

        direButton.setOnClickListener {
            openDirection()
        }
        mylat = ArrayList()
        mylong = ArrayList()


    }

    fun addAnnotation() {

        // Create an instance of the Annotation API and get the PointAnnotationManager.
        // Set options for the resulting symbol layer.
        // Define a geographic coordinate.
        // Specify the bitmap you assigned to the point annotation
        // The bitmap will be added to map style automatically.

        // annotationApi = mapView.an;
        //  AnnotationType ann = pointAnnotationManager = (PointAnnotationManager) annotationApi.createAnnotationManager(mapView);
        val pointAnnotationOptions = PointAnnotationOptions().withPoint(
            Point.fromLngLat(
                llat!!, llng!!
            )
        )
            .withIconImage(R.drawable.ic_baseline_location_on_24.toString())

// Add the resulting pointAnnotation to the map.
        pointAnnotationManager!!.create(pointAnnotationOptions)
    }

    fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            val locationComponent = mapb!!.locationComponent
            initLocationEngine()

            // Activate with options
            // locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(settings.this, loadedMapStyle).build());

            // Enable to make component visible
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationComponent.isLocationComponentEnabled = true
            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING
            // Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(this)
        }
    }

    private fun initLocationEngine() {
        val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
        val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationEngine!!.requestLocationUpdates(request, callback, mainLooper)
        locationEngine!!.getLastLocation(callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //permissions
    override fun onExplanationNeeded(permissionsToExplain: List<String>) {

        // permissionsToExplain.add("please accept the permissions");
        //  Toast.makeText(this, "please accept the permission to display your current location"
        //     , Toast.LENGTH_SHORT).show();
        Timber.tag(tag).d("user didnt allow permission")
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapView!!.getMapboxMap().getStyle(object : com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded() {
                override fun onStyleLoaded(style: Style) {
                    enableLocationComponent(style)
                }
            })
        } else {
            Timber.tag(tag).d(" permissions were denied")
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val i = menuInflater
        i.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val iid = item.itemId
        if (iid == R.id.feedbSett) {
            val t = Intent(this@settings, feedb::class.java)
            startActivity(t)
            finish()
        }
        if (iid == R.id.addlocSett) {
            val e = Intent(this@settings, login::class.java)
            startActivity(e)
            finish()
        }
        if (iid == R.id.LogoutSett) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onalocationPressed(lat: Double?, lng: Double?) {
        val builder = AlertDialog.Builder(this@settings)
        val view = LayoutInflater.from(this@settings).inflate(R.layout.locationinfo, null)
        builder.setView(view)
        val alertDialog1 = builder.create()
        alertDialog1.setCancelable(false)
        val close = findViewById<ImageButton>(R.id.closebtnLocInfo)
        val image = findViewById<ImageView>(R.id.imageLocInfo)
        val directions = findViewById<AppCompatButton>(R.id.directionLocInfo)
        val name = findViewById<TextView>(R.id.nameLocInfo)
        val typeOfInstitution = findViewById<TextView>(R.id.typeOfInstitutionLocInfo)

        /*
        refLoc.child(locationId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkTheme ct = snapshot.getValue(checkTheme.class);

                assert ct != null;

                name.setText(ct.getNameOfPlace());
                typeOfInstitution.setText(ct.getTypeOfInstitution());
                Picasso.get().load(ct.getImageUri()).into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(settings.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

         */close.setOnClickListener { alertDialog1.dismiss() }
        directions.setOnClickListener {
            val i = Intent(this@settings, searchroute::class.java)
            // i.putExtra("nameOfLocation", );
        }
        alertDialog1.show()
    }

    // locations permission
    /*
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


            // addMarkerToUserLocation();
        }
    }


 */
    fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                PERMISSIONS_REQUIRED[0]
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                PERMISSIONS_REQUIRED[1]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // we should get Location of the device

            //onMapReady(mapplsMap);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_REQUIRED[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    PERMISSIONS_REQUIRED[1]
                )
            ) {
                //showPermissionRequiredDialog
                AlertDialog.Builder(this)
                    .setTitle("Please Accept all the permissions")
                    .setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }
                    .setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
                        ActivityCompat.requestPermissions(
                            this@settings,
                            PERMISSIONS_REQUIRED,
                            100
                        )
                    }
                    .show()
            } else {

                //askPermission
                ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, 100)
            }
        }
    }

    //Will hide the keyboard
    fun hideSoftKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun openDirection() {
        val i = Intent(this@settings, searchroute::class.java)
        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Timber.tag(tag).d("app exited")
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    override fun onStart() {
        super.onStart()
        refLoc!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val ct = ds.getValue(checkTheme::class.java)!!
                    val ll = ct.getLat()
                    val lg = ct.getLng()
                    mylat!!.add(ll)
                    mylat!!.add(lg)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@settings, "error occured$error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        if (locationEngine != null) {
            locationEngine!!.removeLocationUpdates(callback)
        }
        mapView!!.onStop()
    }

    companion object {
        private const val tag = "our map fragment"

        // variables for calculating and drawing a route
        private const val MapTag = "DirectionsActivity"
    }
}