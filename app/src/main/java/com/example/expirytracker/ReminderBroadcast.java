package com.example.expirytracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class ReminderBroadcast extends BroadcastReceiver {

    int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("rMsg");
        id = intent.getIntExtra("id", 0);
        String fin = intent.getStringExtra("fin");
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


            FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = curuser.getUid();



            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task");
            db.child(fin).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            Info temp = snapshot.getValue(Info.class);
                            Map<String, Object> val = new TreeMap<>();
                            val.put(fin, temp);

                            DatabaseReference pastTask = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pasttask");
                            pastTask.updateChildren(val);

                        } else {
                            Log.e("TAG", " it's null.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }


            });
            db.child(fin).removeValue();



            Intent i = new Intent(context, TaskAdderActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, i, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            notMan.notify(id, builder.build());
        }
    }
}