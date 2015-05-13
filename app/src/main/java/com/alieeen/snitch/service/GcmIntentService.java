package com.alieeen.snitch.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alieeen.snitch.DetailsActivity_;
import com.alieeen.snitch.R;
import com.alieeen.snitch.SnitchApplication;
import com.alieeen.snitch.database.EventsDataSource;
import com.alieeen.snitch.helper.ImageHandler;
import com.alieeen.snitch.model.Event;
import com.alieeen.snitch.rest.SnitchHttpClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.text.SimpleDateFormat;

/**
 * Created by alinekborges on 16/04/15.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    static final String TAG = "GCMClient";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                // sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                // sendNotification("Deleted messages on server: " +
                // extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());


                // Post notification of received message.
                // sendNotification(extras.toString());
                String p = intent.getExtras().getString("str");
                String[] parameters = p.split(";");

                Log.i("TAG", "send notification");
                sendNotification(parameters);

                Event event = new Event();
                event.setCameraName(parameters[0]);

                String dateTime;                    // Date Time
                try
                {
                    // Convert date time format
                    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    event.setDateTime(dateFormat.format(oldFormat.parse(parameters[1])));
                }
                catch (Exception e)
                {
                    event.setDateTime(parameters[1]);
                }

                String eventId = parameters[2];
                int i = Integer.parseInt(eventId.replaceAll("[\\D]", ""));
                event.setNumber(String.valueOf(i));
                event.setImageName(String.valueOf(i));

                event.setViewed(false);

                // Save Event in database
                EventsDataSource.saveEvent(getApplicationContext(), event);

                //new SaveImage().execute(eventId, eventId);
                sendNotification(parameters);

                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        BroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String[] parameters) {
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent new_intent = new Intent(this, DetailsActivity_.class);
        new_intent.putExtra("camName", parameters[0]);
        new_intent.putExtra("dateTime", parameters[1]);
        new_intent.putExtra("url", parameters[2]);
        new_intent.putExtra("imgName", parameters[2]);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new_intent, PendingIntent.FLAG_CANCEL_CURRENT);

        SnitchApplication application = (SnitchApplication)getApplication();
        int totalNotifications = application.getTotalNotifications();
        totalNotifications++;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.tech_ideas_22)
                .setContentTitle("Snitch NEW")
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText("Novo alarme!"))
                .setContentText("(" + totalNotifications + ") " + "Novo alaaaarme!");

        Log.i(TAG, "sending notification......");

        application.setTotalNotifications(totalNotifications);

        mBuilder.setContentIntent(contentIntent);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private class SaveImage extends AsyncTask<String, String, Bitmap>
    {
        String imgName;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args)
        {
            Bitmap bitmap = null;
            try
            {
                imgName = args[1];
                String eventId = args[0];

                SnitchHttpClient snitchHttp = new SnitchHttpClient();
                bitmap = snitchHttp.downloadBitmap(getApplicationContext(), eventId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image)
        {
            if(image != null)
            {
                ImageHandler imgHandler = new ImageHandler();
                imgHandler.Save(getApplicationContext(), image, imgName);
            }
        }
    }
}


