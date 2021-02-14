package com.example.lista

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


class GeoReceiver : BroadcastReceiver() {
    private val TAG ="MapMarker"
    private var id =0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent)
        for(geo in event.triggeringGeofences){
           // Toast.makeText(context, "Geofence with id ${geo.requestId} triggered", Toast.LENGTH_SHORT).show()
            Log.d(TAG, " Inntent odebrany")
        if(event.geofenceTransition==Geofence.GEOFENCE_TRANSITION_ENTER){
            Log.i(TAG,"Wejście")
            createChannel(context)
            val geolocation = Intent(context,MapsActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                id,
                geolocation,
                PendingIntent.FLAG_ONE_SHOT
            )
            val notification = NotificationCompat.Builder(context,context.getString(R.string.storechannelID))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Witaj w sklepie: "+intent.getStringExtra("storename"))
                .setContentText(intent.getStringExtra("promotion"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            NotificationManagerCompat.from(context).notify(id++,notification)
        }
        if(event.geofenceTransition==Geofence.GEOFENCE_TRANSITION_EXIT){
            Log.i(TAG,"Wejście")
            createChannel(context)
            val geolocation = Intent(context,MapsActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                id,
                geolocation,
                PendingIntent.FLAG_ONE_SHOT
            )
            val notification = NotificationCompat.Builder(context,context.getString(R.string.storechannelID))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Pa Pa")
                .setContentText(intent.getStringExtra("storename"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            NotificationManagerCompat.from(context).notify(id++,notification)
        }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(context: Context){
        val channel = NotificationChannel(
            context.getString(R.string.channelId),
        context.getString(R.string.storechannelName),
        NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}