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
        val FENCE_DATA = arrayOf(
            FenceDataObject(
                "NODE 0",
                "Mobinius",
                LatLng(39.2297964261881, -84.25413151084008),
                300f
            ),

            FenceDataObject(
                "NODE 1",
                "Mobinius",
                LatLng(39.21293531668262, -84.2547283023091),
                300f
            ),

            FenceDataObject(
                "NODE 2",
                "Mobinius",
                LatLng(39.09729839408703, -84.50707454904466),
                200f
            ),

            FenceDataObject(
                "NODE 3",
                "Mobinius",
                LatLng(39.24654881141455, -84.37935852562588),
                400f
            )
        )
    }
}