package barnes.matt.tallpinesrally;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class GcmIntentService extends IntentService {
    public static final int ENTRY_NOTIFICATION_ID = 1;
    public static final int RESULTS_NOTIFICATION_ID = 2;
    private NotificationManager mNotificationManager;
    //NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                boolean updateResults = extras.getString("result").equals("true");

                if (updateResults) {
                    //get result update from service, post to db
                    sendNotification("Latest Results have been posted!", RESULTS_NOTIFICATION_ID);
                }

                boolean updateTeams = extras.getString("team").equals("true");

                if (updateTeams) {
                    //new TeamFeed(MainActivity.getInstance()).getTeamUpdates(false);
                    new TeamFeed(this).getTeamUpdates(false);
                    sendNotification("Entry List has been updated.", ENTRY_NOTIFICATION_ID);
                }

                // send appropriate notification
                //sendNotification("Received: " + extras.toString());


            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, int id) {
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.tp_icon)
                        .setContentTitle("Tall Pines 2014")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(id, mBuilder.build());
    }
}