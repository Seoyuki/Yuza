package seoyuki.yuza;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jaewon on 2016-10-03.
 */

public class Goal extends BroadcastReceiver {



    private static final int NOTIFICATION_ID = 1000;



    @Override

    public void onReceive(Context context, Intent intent) {



        String key = LocationManager.KEY_PROXIMITY_ENTERING;



        Boolean entering = intent.getBooleanExtra(key, false);



        if (entering) {

            Log.d(getClass().getSimpleName(), "entering");

        }

        else {

            Log.d(getClass().getSimpleName(), "exiting");

        }



        NotificationManager notificationManager =

        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);



        Notification notification = createNotification();

       // notification.setLatestEventInfo(context,"Proximity Alert!", "You are near your point of interest.", pendingIntent);



        notificationManager.notify(NOTIFICATION_ID, notification);



    }



    private Notification createNotification() {

        Notification notification = new Notification();



        notification.icon = R.drawable.ic_launcher;

        notification.when = System.currentTimeMillis();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.flags |= Notification.FLAG_SHOW_LIGHTS;


        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;

        notification.ledOffMS = 1500;
        return notification;

    }

}
