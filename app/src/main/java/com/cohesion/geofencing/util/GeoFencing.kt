package com.cohesion.geofencing.util

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.cohesion.geofencing.R
import com.cohesion.geofencing.coreFrameWorks.GeoFencingBReceiver
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.TimeUnit


/**
 * Created by Er Nadeem Bhat on 26/3/21
 *Time : 01 :12
 *Project Name: Geo Fencing
 *Company: Mobinius Technology Pvt Ltd.
 *Email: nadeem.nb@mobinius.com
 * Copyright (c)
 */
class GeoFencing(base: Context?) : ContextWrapper(base) {
    private val TAG = "Geofence"
   private var pendingIntent: PendingIntent? = null


    fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }


    fun getGeofence(ID: String, latLng: LatLng, radius: Float, transitionTypes: Int): Geofence? {
        return Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, radius)
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

    fun getErrorString(e: Exception): String? {
        if (e is ApiException) {
            when (e.statusCode) {
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> return "GEOFENCE_NOT_AVAILABLE"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> return "GEOFENCE_TOO_MANY_GEOFENCES"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> return "GEOFENCE_TOO_MANY_PENDING_INTENTS"
            }
        }
        return e.localizedMessage
    }
    /**
     * Returns the error string for a geofencing error code.
     */
    fun errorMessage(context: Context, errorCode: Int): String {
        val resources = context.resources
        return when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> resources.getString(
                R.string.geofence_not_available
            )
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> resources.getString(
                R.string.geofence_too_many_geofences
            )
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> resources.getString(
                R.string.geofence_too_many_pending_intents
            )
            else -> resources.getString(R.string.unknown_geofence_error)
        }
    }

    /**
     * Stores latitude and longitude information along with a hint to help user find the location.
     */
    data class LandmarkDataObject(val id: String, val hint: Int, val name: Int, val latLong: LatLng)

    internal object GeofencingConstants {

        /**
         * Used to set an expiration time for a geofence. After this amount of time, Location services
         * stops tracking the geofence. For this sample, geofences expire after one hour.
         */
        val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.HOURS.toMillis(1)

        val LANDMARK_DATA = arrayOf(
            LandmarkDataObject(
                "golden_gate_bridge",
                R.string.golden_gate_bridge_hint,
                R.string.golden_gate_bridge_location,
                LatLng(37.819927, -122.478256)),

            LandmarkDataObject(
                "ferry_building",
                R.string.ferry_building_hint,
                R.string.ferry_building_location,
                LatLng(37.795490, -122.394276)),

            LandmarkDataObject(
                "pier_39",
                R.string.pier_39_hint,
                R.string.pier_39_location,
                LatLng(37.808674, -122.409821)),

            LandmarkDataObject(
                "union_square",
                R.string.union_square_hint,
                R.string.union_square_location,
                LatLng(37.788151, -122.407570))
        )

        val NUM_LANDMARKS = LANDMARK_DATA.size
        const val GEOFENCE_RADIUS_IN_METERS = 100f
        const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
    }
}