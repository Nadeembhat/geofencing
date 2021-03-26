package com.cohesion.geofencing.geoFenceEngine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cohesion.geofencing.LogLevel
import com.cohesion.geofencing.Logger
import com.cohesion.geofencing.R
import com.cohesion.geofencing.geoFenceEngine.GeoFence.GeofencingConstants.FENCE_DATA
import com.cohesion.geofencing.util.errorMessage
import com.cohesion.geofencing.view.MapsActivity
import com.cohesion.geofencing.view.MapsActivity.Companion.loggerList
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Er Nadeem Bhat on 26/3/21
 *Time : 01 :17
 *Project Name: Geo Fencing
 *Company: Mobinius Technology Pvt Ltd.
 *Email: nadeem.nb@mobinius.com
 * Copyright (c)
 */
private const val TAG = "GeoFencingBReceiver"
class GeoFencingBReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val timeformat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
            if (geofencingEvent.hasError()) {
                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                val cal = Calendar.getInstance()
                val log = Logger(LogLevel.Error, "Error\t" + timeformat.format(cal.time))
                loggerList.add(log)
                MapsActivity.mAdapter!!.notifyDataSetChanged()
                return
            }
            val transitionType = geofencingEvent.geofenceTransition
            when (transitionType) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    val cal = Calendar.getInstance()
                    val log = Logger(LogLevel.Info, "Entered\t" + timeformat.format(cal.time))
                    loggerList.add(log)
                    MapsActivity.mAdapter!!.notifyDataSetChanged()
                    val fenceId = when {
                        geofencingEvent.triggeringGeofences.isNotEmpty() ->
                            geofencingEvent.triggeringGeofences[0].requestId
                        else -> {
                            Log.e(TAG, "No Geofence Trigger Found! Abort mission!")
                            return
                        }
                    }

                    fenceId.forEach {
                        Log.e(TAG, "Added Geofence is:")
                    }
                    // Check geofence against the constants listed in GeoFence.kt to see if the
                    // user has entered any of the locations we track for geofences.
                    val foundIndex = FENCE_DATA.indexOfFirst {
                        it.id == fenceId
                    }
                    // Unknown Geofences aren't helpful to us
                    if (-1 == foundIndex) {
                        Log.e(TAG, "Unknown Geofence: Abort Mission")
                        return
                    }
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    Log.e(TAG, context.getString(R.string.geofence_entered))
                    val cal = Calendar.getInstance()
                    val log = Logger(LogLevel.Info, "Exit\t" + timeformat.format(cal.time))
                    loggerList.add(log)
                    MapsActivity.mAdapter!!.notifyDataSetChanged()
                }
            }
        }

}