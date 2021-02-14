package com.example.lista

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast


/**
 * Implementation of App Widget functionality.
 */
class MediaAppWidget : AppWidgetProvider() {
    companion object {
        var currentimage = 0
        var buttonpp = 0
        var currentsong =0
        private var mp: MediaPlayer? = null
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val remoteViews = RemoteViews(context?.packageName, R.layout.media_app_widget)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        var img = arrayOf(R.drawable.bird, R.drawable.bulb, R.drawable.flower, R.drawable.moon, R.drawable.sun)
        var songs = arrayOf(R.raw.chopinmusic,R.raw.lonedigger,R.raw.putindre, R.raw.riverflowsinyou, R.raw.trap)
        if (mp == null){
        mp = MediaPlayer.create(context, songs[currentsong] )
        mp?.setVolume(0.5f, 0.5f)}
        //var buttonimg = arrayOf(R.drawable.playbutton,R.drawable.pausebutton)

        if (intent?.action == context?.getString(R.string.action1)) {
            Toast.makeText(context, "Action1", Toast.LENGTH_SHORT).show()
            currentimage = (currentimage + 1) % img.size
            remoteViews.setImageViewResource(R.id.imageWidget, img[currentimage]);
            appWidgetManager.updateAppWidget(ComponentName(context!!, MediaAppWidget::class.java), remoteViews)
        }

        if (intent?.action == context?.getString(R.string.actionplay)) {
            if (mp!!.isPlaying) {
                Toast.makeText(context, "pause", Toast.LENGTH_SHORT).show()
                mp?.pause()
               // remoteViews.setImageViewResource(R.id.widget_play, buttonimg[buttonpp])
               // appWidgetManager.updateAppWidget(ComponentName(context!!, MediaAppWidget::class.java), remoteViews)
            } else {
                mp?.start()
                //buttonpp = (buttonpp + 1)% buttonimg.size
                //remoteViews.setBitmap(R.id.widget_play, "bitmap",buttonimg[buttonpp])
                //appWidgetManager.updateAppWidget(ComponentName(context!!, MediaAppWidget::class.java), remoteViews)
            }
        }
        super.onReceive(context, intent);
        if (intent?.action == context?.getString(R.string.actionstop)) {
            Toast.makeText(context, "Actionstop", Toast.LENGTH_SHORT).show()
            if (mp!!.isPlaying) {
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp=null

            }
        }
        if (intent?.action == context?.getString(R.string.actionnext)) {
            Toast.makeText(context, "Actionnext", Toast.LENGTH_SHORT).show()
            if (mp!!.isPlaying) {
                if(currentsong<songs.size -1){
                    currentsong ++
                }
                else
                   currentsong = 0
            }
            if(mp!!.isPlaying){
                mp?.stop()
            }
            mp = MediaPlayer.create(context, songs[currentsong])
            mp?.start()
        }
        if (intent?.action == context?.getString(R.string.actionprev)) {
            Toast.makeText(context, "Actionprev", Toast.LENGTH_SHORT).show()
            if (mp!!.isPlaying) {
                if(currentsong>0){
                    currentsong --
                }
                else
                    currentsong = songs.size -1
            }
            if(mp!!.isPlaying){
                mp?.stop()
            }
            mp = MediaPlayer.create(context, songs[currentsong])
            mp?.start()
        }
    }


    internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val widgetText = context.getString(R.string.appwidget_text)
        val views = RemoteViews(context.packageName, R.layout.media_app_widget)
        views.setTextViewText(R.id.widget_t1, widgetText)
        val intentWWW = Intent(Intent.ACTION_VIEW)
        intentWWW.data = Uri.parse("https://www.google.com")
        val pendingWWW = PendingIntent.getActivity(
                context,
                0,
                intentWWW,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widget_b1, pendingWWW)

        val intentAction = Intent(context.getString(R.string.action1))
        intentAction.component = ComponentName(context, MediaAppWidget::class.java)
        val pendingAction = PendingIntent.getBroadcast(
                context,
                0,
                intentAction,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widget_b2, pendingAction)

        val intentActionPlay = Intent(context.getString(R.string.actionplay))
        intentActionPlay.component = ComponentName(context, MediaAppWidget::class.java)
        val pendingActionPlay = PendingIntent.getBroadcast(
                context,
                0,
                intentActionPlay,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widget_play, pendingActionPlay)

        val intentActionPrev = Intent(context.getString(R.string.actionprev))
        intentActionPrev.component = ComponentName(context, MediaAppWidget::class.java)
        val pendingActionPrev = PendingIntent.getBroadcast(
                context,
                0,
                intentActionPrev,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widget_prev, pendingActionPrev)

        val intentActionNext = Intent(context.getString(R.string.actionnext))
        intentActionNext.component = ComponentName(context, MediaAppWidget::class.java)
        val pendingActionNext = PendingIntent.getBroadcast(
                context,
                0,
                intentActionNext,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widget_next, pendingActionNext)
        val intentActionStop = Intent(context.getString(R.string.actionstop))
        intentActionStop.component = ComponentName(context, MediaAppWidget::class.java)
        val pendingActionStop = PendingIntent.getBroadcast(
                context,
                0,
                intentActionStop,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widget_stop, pendingActionStop)
        //views.setImageViewResource(R.id.imageWidget, R.drawable.bulb)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}