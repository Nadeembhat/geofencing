package com.cohesion.geofencing.geoFenceEngine

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.cohesion.geofencing.util.errorMessage
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices


/**
 * Created by Er Nadeem Bhat on 26/3/21
 *Time : 18 :40
 *Project Name: Geo Fencing
 *Company: Mobinius Technology Pvt Ltd.
 *Email: nadeem.nb@mobinius.com
 * Copyright (c)
 */
class FenceManager(private val context: Context) {
    private var geoFence: GeoFence? = null
    private var client :GeofencingClient ?=null
    private  val TAG= "LocationListner"
    companion object{
        var i=0
    }
        init {

            client = LocationServices.getGeofencingClient(context)
            geoFence = GeoFence(context)
        }
     fun addGeofence(lat: Double, long: Double, radius: Float, name: String) {
        val geofence = geoFence!!.getGeofence(
            i.toString(),
            lat,
            long,
            radius,
            name,
            Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT
        )
        val geofencingRequest: GeofencingRequest = geoFence!!.getGeofencingRequest(geofence!!)
        val pendingIntent = geoFence!!.getPendingIntent()
        // val geofencingEvent = GeofencingEvent.fromIntent(pendingIntent)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        client!!.addGeofences(geofencingRequest, pendingIntent)
            .addOnSuccessListener {
                //Toast.makeText(this, "Geofence Added..", Toast.LENGTH_LONG).show()
                Log.e(TAG, "onSuccess: Geofence Added..." + name)
            }
            .addOnFailureListener { e ->
                val errorMessage = errorMessage(context, e.hashCode())
                Log.e(TAG, "onFailure: $errorMessage")
            }
        i++
    }
}