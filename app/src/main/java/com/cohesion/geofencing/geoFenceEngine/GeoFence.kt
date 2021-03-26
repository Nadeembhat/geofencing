package com.cohesion.geofencing.geoFenceEngine

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng


/**
 * Created by Er Nadeem Bhat on 26/3/21
 *Time : 01 :12
 *Project Name: Geo Fencing
 *Company: Mobinius Technology Pvt Ltd.
 *Email: nadeem.nb@mobinius.com
 * Copyright (c)
 */
class GeoFence(base: Context?) : ContextWrapper(base) {
    private var pendingIntent: PendingIntent? = null

    fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }


    fun getGeofence(ID: String, lat: Double,long:Double, radius: Float, name:String,transitionTypes: Int): Geofence? {
        return Geofence.Builder()
            .setCircularRegion(lat, long, radius)
            .setRequestId(ID)
            .setTransitionTypes(transitionTypes)
            .setLoiteringDelay(5000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

    fun getPendingIntent(): PendingIntent {
        if (pendingIntent != null) {
            return pendingIntent!!
        }
        val intent = Intent(this, GeoFencingBReceiver::class.java)
        pendingIntent =
            PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent!!
    }

    data class FenceDataObject(val id: String, val name: String, val latLong: LatLng,val radius:Float)

    internal object GeofencingConstants {

        /*<wpt lat="12.91987" lon="77.57675"> 12.91987, 77.57675
        <name>Mobinius</name>
        <time>2014-09-24T14:00:00Z</time>
    </wpt>

    <wpt lat="12.90778" lon="77.58316">
      <name>J.P Nagar</name>
      <time>2014-09-24T14:00:10Z</time>
    </wpt>

    <wpt lat="12.94828" lon="77.60338">
        <name>Abhaya Hospital</name>
        <time>2014-09-24T14:00:20Z</time>
    </wpt>

    <wpt lat="12.92694" lon="77.54537">
        <name>Banashankari</name>
        <time>2014-09-24T14:00:30Z</time>
    </wpt>*/
        val FENCE_DATA = arrayOf(
            FenceDataObject(
                "NODE 0",
                "Mobinius",
                LatLng(12.91987, 77.57675),
                300f
            ),

            FenceDataObject(
                "NODE 1",
                "J P Nagar",
                LatLng(12.90778, 77.58316),
                300f
            ),

            FenceDataObject(
                "NODE 2",
                "Abhaya Hospital",
                LatLng(12.94828,77.60338),
                200f
            ),

            FenceDataObject(
                "NODE 3",
                "Banashankari",
                LatLng(12.92694, 77.54537),
                400f
            )
        )
    }
}