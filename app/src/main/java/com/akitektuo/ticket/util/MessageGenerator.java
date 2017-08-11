package com.akitektuo.ticket.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.akitektuo.ticket.R;
import com.akitektuo.ticket.activity.MainActivity;
import com.akitektuo.ticket.adapter.MessageAdapter;
import com.akitektuo.ticket.adapter.MessageItem;
import com.akitektuo.ticket.app.MyApplication;
import com.akitektuo.ticket.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.akitektuo.ticket.database.DatabaseContract.CURSOR_DAY;
import static com.akitektuo.ticket.database.DatabaseContract.CURSOR_ERROR;
import static com.akitektuo.ticket.database.DatabaseContract.CURSOR_HOUR;
import static com.akitektuo.ticket.database.DatabaseContract.CURSOR_MINUTE;
import static com.akitektuo.ticket.database.DatabaseContract.CURSOR_MONTH;
import static com.akitektuo.ticket.database.DatabaseContract.CURSOR_TEXT;
import static com.akitektuo.ticket.database.DatabaseContract.CURSOR_TYPE;
import static com.akitektuo.ticket.database.DatabaseContract.CURSOR_YEAR;
import static com.akitektuo.ticket.util.Constant.TYPE_RECEIVER;
import static com.akitektuo.ticket.util.Constant.TYPE_SENDER;

/**
 * Created by AoD Akitektuo on 11-May-17 at 21:26.
 */

public class MessageGenerator {
    private List<MessageItem> messages;
    private RecyclerView listMessages;
    private EditText editMessage;
    private Activity activity;
    private DatabaseHelper database;

    public MessageGenerator(Activity activity, DatabaseHelper database, EditText editMessage,
                            List<MessageItem> messages, RecyclerView listMessages) {
        setActivity(activity);
        setDatabase(database);
        setEditMessage(editMessage);
        setMessages(messages);
        setListMessages(listMessages);
    }

    private List<MessageItem> getMessages() {
        return messages;
    }

    private void setMessages(List<MessageItem> messages) {
        this.messages = messages;
    }

    private RecyclerView getListMessages() {
        return listMessages;
    }

    private void setListMessages(RecyclerView listMessages) {
        this.listMessages = listMessages;
    }

    private EditText getEditMessage() {
        return editMessage;
    }

    private void setEditMessage(EditText editMessage) {
        this.editMessage = editMessage;
    }

    private Activity getActivity() {
        return activity;
    }

    private void setActivity(Activity activity) {
        this.activity = activity;
    }

    private DatabaseHelper getDatabase() {
        return database;
    }

    private void setDatabase(DatabaseHelper database) {
        this.database = database;
    }

    public void addMessage(String text, boolean safeMode) {
        if (!text.isEmpty()) {
            boolean pass = checkLine(text);
            if (!safeMode) {
                pass = true;
            }
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH), month = Calendar.getInstance().get(Calendar.MONTH),
                    year = Calendar.getInstance().get(Calendar.YEAR), hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    minute = Calendar.getInstance().get(Calendar.MINUTE);
            getMessages().add(new MessageItem(!pass, TYPE_SENDER, text, day, month, year, hour, minute));
            getListMessages().setAdapter(new MessageAdapter(getActivity(), getMessages()));
            getEditMessage().setText("");
            hideKeyboard();
            getListMessages().scrollToPosition(getMessages().size() - 1);
            if (pass) {
                final String line = text.toUpperCase();
                getDatabase().addMessage(false, TYPE_SENDER, text, day, month, year, hour, minute);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                                month = Calendar.getInstance().get(Calendar.MONTH),
                                year = Calendar.getInstance().get(Calendar.YEAR),
                                hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                minute = Calendar.getInstance().get(Calendar.MINUTE);
                        String generateAnswer = "Biletul pentru linia " + line + " a fost activat. Valabil pana la " +
                                getGeneratedTime() + " in " + getGeneratedDate() + ". Cost total:0.50 EUR+Tva. Cod confirmare:"
                                + getGeneratedCode();
                        database.addMessage(false, TYPE_RECEIVER, generateAnswer, day, month, year, hour, minute);
                        if (!MyApplication.isActivityVisible())
                            notifyUser(generateAnswer);
                        refreshMessages();
                    }
                }, 3000);
            }
        }
    }

    private boolean checkLine(String line) {
        line = line.toUpperCase();
        List<String> validLines = new ArrayList<>(Arrays.asList("1", "3", "4", "5", "6", "7", "8", "8L",
                "9", "19", "20", "21", "22", "23", "23L", "24", "24B", "25", "25N", "26", "26L", "27",
                "28", "28B", "29", "30", "31", "32", "32B", "33", "34", "35", "36B", "36L", "37", "38",
                "39", "39L", "40", "40S", "41", "42", "43", "43B", "43P", "46", "46B", "47", "48", "48L",
                "50", "50L", "52", "87B", "100", "101", "102", "102L"));
        for (String x : validLines) {
            if (line.equals(x)) {
                return true;
            }
        }
        return false;
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private String getGeneratedTime() {
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

    private String getGeneratedDate() {
        Date date = new Date(System.currentTimeMillis() + 2700000);
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    private void notifyUser(String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.message)
                        .setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon))
                        .setContentTitle("New message")
                        .setContentText(message);
        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {1500};
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    public void refreshMessages() {
        getMessages().clear();
        Cursor cursor = getDatabase().getMessages();
        if (cursor.moveToFirst()) {
            do {
                getMessages().add(new MessageItem(Boolean.getBoolean(cursor.getString(CURSOR_ERROR)), cursor.getInt(CURSOR_TYPE),
                        cursor.getString(CURSOR_TEXT), cursor.getInt(CURSOR_DAY), cursor.getInt(CURSOR_MONTH),
                        cursor.getInt(CURSOR_YEAR), cursor.getInt(CURSOR_HOUR), cursor.getInt(CURSOR_MINUTE)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        getListMessages().setAdapter(new MessageAdapter(getActivity(), getMessages()));
        getListMessages().scrollToPosition(getMessages().size() - 1);
    }

    private String getGeneratedCode() {
        return String.valueOf(new Random().nextInt(9) + 1) +
                (new Random().nextInt(9) + 1) +
                (new Random().nextInt(9) + 1) +
                (new Random().nextInt(9) + 1) +
                (new Random().nextInt(9) + 1) +
                (new Random().nextInt(9) + 1);
    }
}
