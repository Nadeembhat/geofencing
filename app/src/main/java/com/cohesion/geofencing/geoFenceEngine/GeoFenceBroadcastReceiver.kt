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

            if (geofencingEvent.hasError()) {
                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                val log = Logger(LogLevel.Error,errorMessage)
                loggerList.add(log)
                MapsActivity.mAdapter!!.notifyDataSetChanged()
                return
            }
            val geofenceList = geofencingEvent.triggeringGeofences
            for (geofence in geofenceList) {
                Log.e(TAG, "onReceive:----\t " + geofence.requestId)
            }
            val transitionType = geofencingEvent.geofenceTransition
            Log.e(TAG, "TransactionType: " + transitionType+"\tGeoFence Size\t"+geofenceList.size)
            when (transitionType) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    Log.e(TAG, context.getString(R.string.geofence_entered))
                    //Entered (Geo Fence Name) at (Time)//dd:MM:yyyy aa:ss
                    val log = Logger(LogLevel.Info,"Entered\t"+Calendar.getInstance().time)
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
                        Log.e(TAG, "Added Geofence is:"+fenceId!!)
                    }
                    // Check geofence against the constants listed in GeofenceUtil.kt to see if the
                    // user has entered any of the locations we track for geofences.
                    val foundIndex = FENCE_DATA.indexOfFirst {
                        it.id == fenceId
                    }
                    // Unknown Geofences aren't helpful to us
                    if ( -1 == foundIndex) {
                        Log.e(TAG, "Unknown Geofence: Abort Mission")
                        return
                    }
                    Log.e(TAG, "TRANSACTION ENTER\t"+foundIndex)
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    Log.e(TAG, context.getString(R.string.geofence_entered))
                    val log = Logger(LogLevel.Info,"GEOFENCE_TRANSITION_EXit\t")
                    loggerList.add(log)
                    MapsActivity.mAdapter!!.notifyDataSetChanged()
                    Log.e(TAG, "GEOFENCE_TRANSITION_EXIT")
                }
            }
        }

}