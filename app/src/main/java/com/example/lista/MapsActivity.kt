package com.example.lista

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_store_list.*

class Fail: OnFailureListener{
    override fun onFailure(p0: Exception) {
        Log.i("MapMarker", "Client create fail", p0)

    }

}

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val TAG ="MapMarker"
    private lateinit var db: FirebaseFirestore
    var arstores = arrayListOf<Store>()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(perms, 0)
            return
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Log.i(TAG, "get data from firestore")
        db = FirebaseFirestore.getInstance()
        val geoClient = LocationServices.getGeofencingClient(this)
        var id =0
        ibstore.setOnClickListener {
            val intent = Intent(this, StoreListActivity::class.java)
             startActivity(intent)
        }
        db.collection("Stores")
            .addSnapshotListener { result, e ->
                if (e != null) {
                    Log.w(TAG, "Listener failed.", e)
                    return@addSnapshotListener
                }
                mMap.clear()
                arstores.clear()
                arstores.addAll(result!!.toObjects(Store::class.java))
                for (store in arstores) {
                    Log.i(TAG, "Geo pass data")
                    val lat: Double = store.location!!.latitude
                    val lng: Double = store.location!!.longitude
                    val radius: Float = store.radius!!
                    Log.i(TAG, "Lokalizacja: ${lat} i ${lng}, promien: ${radius}")
                    Log.i(TAG, "Geo create")
                    val geo = Geofence.Builder()
                        .setRequestId("Geo${id++}")
                        .setCircularRegion(lat, lng, radius)
                        //.setNotificationResponsiveness(1000)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build()
                    Log.i(TAG, "Geo finish build")
                    val geoRequest = GeofencingRequest.Builder()
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                        .addGeofence(geo)
                        .build()

                    val aint = Intent(applicationContext, GeoReceiver::class.java)
                    aint.putExtra("storename", store.store)
                    aint.putExtra("promotion", store.promotion)
                    val geoPendingIntent = PendingIntent.getBroadcast(
                        this, id, aint, PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    geoClient.addGeofences(geoRequest, geoPendingIntent).run {
                        addOnSuccessListener {  Log.i(TAG, "Cliens create succes") }
                        addOnFailureListener(Fail())
                    }
                }
            }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(perms, 0)
            return
        }
        mMap.isMyLocationEnabled = true

        switch1.setOnCheckedChangeListener{ compoundButton, onSwitch->
            if(onSwitch) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    db.collection(user.uid + "store")
                            .addSnapshotListener { result, e ->
                                if (e != null) {
                                    Log.w(TAG, "Listener failed.", e)
                                    return@addSnapshotListener
                                }
                                mMap.clear()
                                arstores.clear()
                                arstores.addAll(result!!.toObjects(Store::class.java))
                                for (store in arstores) {
                                    val lat: Double = store.location!!.latitude
                                    val lng: Double = store.location!!.longitude
                                    val geoPosition = LatLng(lat, lng)
                                    mMap.addMarker(
                                        MarkerOptions().position(geoPosition).title(store.store)
                                    )
                                }
                            }
                }
            } else{
                db.collection("Stores")
                        .addSnapshotListener { result, e ->
                            if (e!=null){
                                Log.w(TAG, "Listener failed.", e)
                                return@addSnapshotListener
                            }
                            mMap.clear()
                            arstores.clear()
                            arstores.addAll(result!!.toObjects(Store::class.java))
                            for(store in arstores) {
                                val lat: Double = store.location!!.latitude
                                val lng: Double = store.location!!.longitude
                                val geoPosition = LatLng(lat, lng)
                                mMap.addMarker(
                                    MarkerOptions().position(geoPosition).title(store.store)
                                )
                            }
                        }
            }
        }
        db.collection("Stores")
            .addSnapshotListener { result, e ->
                if (e!=null){
                    Log.w(TAG, "Listener failed.", e)
                    return@addSnapshotListener
                }
                mMap.clear()
                arstores.clear()
                arstores.addAll(result!!.toObjects(Store::class.java))
                for(store in arstores) {
                    val lat: Double = store.location!!.latitude
                    val lng: Double = store.location!!.longitude
                    val geoPosition = LatLng(lat, lng)
                    mMap.addMarker(
                        MarkerOptions().position(geoPosition).title(store.store)
                    )
                }
            }
        zoomOnMe()
    }
    fun requestMyGpsLocation(context: Context, callback: (location: Location) -> Unit) {
        val request = LocationRequest()
        //        request.interval = 10000
        //        request.fastestInterval = 5000
        request.numUpdates = 1
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(context)

        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val location = locationResult?.lastLocation
                    if (location != null)
                        callback.invoke(location)
                }
            }, null)
        }
    }
    fun zoomOnMe() {
        requestMyGpsLocation(this) { location ->
            mMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude), 12F
                )
            )
        }
    }
}


