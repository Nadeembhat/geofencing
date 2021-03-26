package com.cohesion.geofencing.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cohesion.geofencing.LogLevel
import com.cohesion.geofencing.geoFenceEngine.FenceManager
import com.cohesion.geofencing.Logger
import com.cohesion.geofencing.R
import com.cohesion.geofencing.geoFenceEngine.GeoFence.GeofencingConstants.FENCE_DATA
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback , OnMapLongClickListener{

    private lateinit var mMap: GoogleMap
    private val FINE_LOCATION_ACCESS_REQUEST_CODE = 10001
    private val BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002
    private var locationManager: FenceManager?=null
    private  var mRecyclerView:RecyclerView ?=null
    val timeformat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mRecyclerView = findViewById(R.id.map_log_rv)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        loggerList.clear()
        mAdapter =  LoggerAdapter(loggerList)
        mRecyclerView!!.adapter = mAdapter
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager =  FenceManager(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        FENCE_DATA.forEach {
            mMap.addMarker(MarkerOptions().position(it.latLong).title(it.name))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it.latLong, 15f))
        }
        enableUserLocation()
        mMap.setOnMapLongClickListener(this)
    }
    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            addGeoFenceNodes()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mMap.isMyLocationEnabled = true
                addGeoFenceNodes()
            } else {
                //We do not have the permission..
                val cal = Calendar.getInstance()
                val log = Logger(LogLevel.Error, "\tLocation Access Is Necessary\t" + timeformat.format(cal.time))
                loggerList.add(log)
                mAdapter!!.notifyDataSetChanged()
            }
        }
        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                val cal = Calendar.getInstance()
                val log = Logger(LogLevel.Info, "\tYou can add geofences...\t" + timeformat.format(cal.time))
                loggerList.add(log)
                mAdapter!!.notifyDataSetChanged()
                addGeoFenceNodes()
            } else {
                //We do not have the permission..
                val cal = Calendar.getInstance()
                val log = Logger(LogLevel.Error, "\tBack Location Is Necessary\t" + timeformat.format(cal.time))
                loggerList.add(log)
                mAdapter!!.notifyDataSetChanged()
            }
        }
    }
    override fun onMapLongClick(latLng: LatLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager!!.addGeofence(latLng.latitude ,latLng.longitude,100f,"Node Random")
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        BACKGROUND_LOCATION_ACCESS_REQUEST_CODE
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        BACKGROUND_LOCATION_ACCESS_REQUEST_CODE
                    )
                }
            }
        } else {
            locationManager!!.addGeofence(latLng.latitude ,latLng.longitude,100f,"Node Random")
        }
    }




    private fun addGeoFenceNodes(){
        FENCE_DATA.forEach {
            locationManager!!.addGeofence(it.latLong.latitude ,it.latLong.longitude,it.radius,it.id)
        }
    }
    companion object {
        var mAdapter: LoggerAdapter?=null
       var loggerList:ArrayList<Logger> = arrayListOf()
    }
}