package com.orl.katerina.zametki.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.orl.katerina.zametki.DB.MyDBHelper;
import com.orl.katerina.zametki.Main_with_list;
import com.orl.katerina.zametki.R;

public class TimeNotification extends BroadcastReceiver {
    public TimeNotification() {
    }

    NotificationManager nm;
    @Override
    public void onReceive(final Context context, Intent intent) {
        final Intent myIntent = intent;
        final Thread myThread = new Thread(new Runnable() {

            @Override
            public void run() {
                        String text= myIntent.getStringExtra("text");
                        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = new Notification(R.drawable.ic_alert, "Заметки", System.currentTimeMillis());
                        Intent intentN = new Intent(context, Main_with_list.class);
                        notification.setLatestEventInfo(context, "Заметки", text,
                                PendingIntent.getActivity(context, 2, intentN,PendingIntent.FLAG_CANCEL_CURRENT));
                        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
                        nm.notify(2, notification);
                Log.d("aaa", "notify send");
                MyDBHelper myDBHelper= MyDBHelper.getInstance(context);
                Log.d("aaa", "call DB");
                myDBHelper.startNotify();

                                   }                });

        myThread.start();
    }
}
