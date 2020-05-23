package com.dicarlo.notificationapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.UUID;

import static android.Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE;
import static androidx.core.content.PermissionChecker.PERMISSION_DENIED;

public class MainActivity extends AppCompatActivity {

    private String lastNotificationChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("notification","test");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int i = ContextCompat.checkSelfPermission(this, BIND_NOTIFICATION_LISTENER_SERVICE);
        Log.d("notag", ""+i);
        ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE}, 1);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ID1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("textTitle")
                .setContentText("Content")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // WRONG: get a random channel id every time
                String channelId = UUID.randomUUID().toString();

                // But keep the same name
                CharSequence name = "STILL THE SAME CHANNEL I PROMISE !";
                String description = "Annoying Notification";

                // Not sure this change anything
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);
                final NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(1, builder.setChannelId(channelId).build());
                Snackbar.make(view, "Sending notification", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // If there was channel before, delete it
                if (lastNotificationChannel != null) {
                    notificationManager.deleteNotificationChannel(lastNotificationChannel);
                }

                // Set the last channel
                lastNotificationChannel = channelId;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
