package org.icspl.icsconnect.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import org.icspl.icsconnect.R;

public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_ID = "org.icspl.icsconnect";
    public static final String CHANNEL_NAME = "ICS Connect";
    public NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(mChannel);
    }

    private NotificationManager getManager() {

        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public  Notification.Builder getICSNotification(String title, String Message, Uri vsoundUri){

        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(Message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(vsoundUri, AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setAutoCancel(true);
    }
}
