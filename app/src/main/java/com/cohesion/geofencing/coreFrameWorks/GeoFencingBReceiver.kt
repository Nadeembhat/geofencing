package com.cohesion.geofencing.coreFrameWorks

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cohesion.geofencing.R
import com.cohesion.geofencing.util.GeofencingConstants.LANDMARK_DATA
import com.cohesion.geofencing.util.GeofencingConstants.sendGeofenceEnteredNotification
import com.cohesion.geofencing.util.errorMessage
import com.cohesion.geofencing.view.MapsActivity
import com.cohesion.geofencing.view.MapsActivity.Companion.ACTION_GEOFENCE_EVENT
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


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
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent.hasError()) {
                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }

            val geofenceList = geofencingEvent.triggeringGeofences
            for (geofence in geofenceList) {
                Log.e(
                    TAG,
                    "onReceive: " + geofence.requestId
                )
            }
            val transitionType = geofencingEvent.geofenceTransition

            when (transitionType) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    /*Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show()
                    notificationHelper.sendHighPriorityNotification(
                        "GEOFENCE_TRANSITION_ENTER", "",
                        MapsActivity::class.java
                    )*/
                    Log.v(TAG, context.getString(R.string.geofence_entered))

                    val fenceId = when {
                        geofencingEvent.triggeringGeofences.isNotEmpty() ->
                            geofencingEvent.triggeringGeofences[0].requestId
                        else -> {
                            Log.e(TAG, "No Geofence Trigger Found! Abort mission!")
                            return
                        }
                    }
                    // Check geofence against the constants listed in GeofenceUtil.kt to see if the
                    // user has entered any of the locations we track for geofences.
                    val foundIndex =LANDMARK_DATA.indexOfFirst {
                        it.id == fenceId
                    }

                    // Unknown Geofences aren't helpful to us
                    if ( -1 == foundIndex ) {
                        Log.e(TAG, "Unknown Geofence: Abort Mission")
                        return
                    }

                    val notificationManager = ContextCompat.getSystemService(
                        context,
                        NotificationManager::class.java
                    ) as NotificationManager

                    notificationManager.sendGeofenceEnteredNotification(
                        context, foundIndex
                    )
                }
                Geofence.GEOFENCE_TRANSITION_DWELL -> {
                    Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show()
//                    notificationHelper.sendHighPriorityNotification(
//                        "GEOFENCE_TRANSITION_DWELL", "",
//                        MapsActivity::class.java
//                    )
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show()
//                    notificationHelper.sendHighPriorityNotification(
//                        "GEOFENCE_TRANSITION_EXIT",
//                        "",
//                        MapsActivity::class.java
//                    )
                }
            }
        }
    }
}