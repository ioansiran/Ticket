package com.akitektuo.ticket.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;

import com.akitektuo.ticket.R;
import com.akitektuo.ticket.activity.MainActivity;
import com.akitektuo.ticket.adapter.MessageItem;
import com.akitektuo.ticket.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Akitektuo on 11.05.2017.
 */

public class NotificationHelper {

    private Context context;
    private String line;
    private List<MessageItem> messages;
    private DatabaseHelper database;
    private RecyclerView listMessages;

    public NotificationHelper(Context context, String line, List<MessageItem> messages, DatabaseHelper database, RecyclerView listMessages) {
        setContext(context);
        setLine(line);
        setMessages(messages);
        setDatabase(database);
        setListMessages(listMessages);
    }

    public String getGeneratedTime() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY), min = Calendar.getInstance().get(Calendar.MINUTE) + 45;
        if (min > 59) {
            hour++;
            min -= 60;
        }
        if (hour > 23) {
            hour = 0;
        }
        String resHour, resMin;
        if (hour < 10) {
            resHour = "0" + hour;
        } else {
            resHour = String.valueOf(hour);
        }
        if (min < 10) {
            resMin = "0" + min;
        } else {
            resMin = String.valueOf(min);
        }
        return resHour + ":" + resMin;
    }

    public String getGeneratedDate() {
        Date date = new Date(System.currentTimeMillis() + 2700000);
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    private void notifyUser(String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.notification)
                        .setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon))
                        .setContentTitle("New message")
                        .setContentText(message);
        Intent notificationIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {1000};
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());
        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public List<MessageItem> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageItem> messages) {
        this.messages = messages;
    }

    public DatabaseHelper getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseHelper database) {
        this.database = database;
    }

    public RecyclerView getListMessages() {
        return listMessages;
    }

    public void setListMessages(RecyclerView listMessages) {
        this.listMessages = listMessages;
    }
}
