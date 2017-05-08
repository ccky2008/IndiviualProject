package com.bignerdranch.android.fyp.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.activities.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by David on 3/28/2017.
 */

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        createNotification(remoteMessage.getNotification().getBody());
    }


    private void createNotification( String messageBody) {
        boolean x = QueryPreferences.getIsStudent(getApplicationContext());
        /*
        if (x) {
            Log.i("x", "This is true");
        } else {
            Log.i("x", "This is false");
        }*/
        Intent intent = new Intent( this , LoginActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        if (QueryPreferences.getIsStudent(getApplicationContext())) {
            mNotificationBuilder.setContentTitle("An appointment is updated");
        } else {
            mNotificationBuilder.setContentTitle("You have a new appointment!");
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }

}
