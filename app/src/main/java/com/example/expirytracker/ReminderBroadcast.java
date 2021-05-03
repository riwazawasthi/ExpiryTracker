package com.example.expirytracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class ReminderBroadcast extends BroadcastReceiver {

    int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("rMsg");
        id = intent.getIntExtra("id", 0);
        if(intent.getStringExtra("rMsg") != null && intent.getStringExtra("rMsg").equals(msg)) {
            NotificationManager notMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);




            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Expiry Reminder";
                String description = "Channel for Reminder";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("id", name, importance);
                channel.setDescription(description);

                notMan.createNotificationChannel(channel);

            }


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "id")
                    .setContentTitle("Expiry Reminder")
                    .setContentText(msg + " expiring in 2 days!")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            // notificationManager.notify(200, builder.build());

            Intent i = new Intent(context, TaskAdderActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, i, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            notMan.notify(id, builder.build());
        }
    }
}