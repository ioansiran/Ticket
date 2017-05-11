package com.akitektuo.ticket.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akitektuo.ticket.R;
import com.akitektuo.ticket.adapter.MessageAdapter;
import com.akitektuo.ticket.adapter.MessageItem;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PopupMenu popupMenu;
    private TextView textLimit;
    private EditText editMessage;
    private RecyclerView listMessages;
    private List<MessageItem> messages;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_back).setOnClickListener(this);
        Button buttonMenu = (Button) findViewById(R.id.button_menu);
        buttonMenu.setOnClickListener(this);
        popupMenu = new PopupMenu(this, buttonMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        textLimit = (TextView) findViewById(R.id.text_limit);
        textLimit.setText(getString(R.string.limit, 0));
        editMessage = (EditText) findViewById(R.id.edit_text_message);
        editMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textLimit.setText(getString(R.string.limit, editMessage.getText().toString().length()));
                if (editMessage.getText().toString().length() > 160) {
                    editMessage.setText(editMessage.getText().toString().substring(0, 160));
                    Toast.makeText(getApplicationContext(), "limit reached", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.button_send).setOnClickListener(this);
        findViewById(R.id.button_send).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addMessage(editMessage.getText().toString(), false);
                return true;
            }
        });
        listMessages = (RecyclerView) findViewById(R.id.list_message);
        listMessages.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();
        database = new DatabaseHelper(this);
        refreshMessages();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.button_menu:
                popupMenu.show();
                break;
            case R.id.button_send:
                addMessage(editMessage.getText().toString(), true);
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void addMessage(String text, boolean safeMode) {
        if (!text.isEmpty()) {
            boolean pass = checkLine(text);
            if (!safeMode) {
                pass = true;
            }
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH), month = Calendar.getInstance().get(Calendar.MONTH),
                    year = Calendar.getInstance().get(Calendar.YEAR), hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    minute = Calendar.getInstance().get(Calendar.MINUTE);
            messages.add(new MessageItem(!pass, TYPE_SENDER, text, day, month, year, hour, minute));
            listMessages.setAdapter(new MessageAdapter(this, messages));
            editMessage.setText("");
            hideKeyboard();
            listMessages.scrollToPosition(messages.size() - 1);
            if (pass) {
                final String line = text.toUpperCase();
                database.addMessage(false, TYPE_SENDER, text, day, month, year, hour, minute);
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
                                + (new Random().nextInt(900000) + 100000);
                        database.addMessage(false, TYPE_RECEIVER, generateAnswer, day, month, year, hour, minute);
                        notifyUser(generateAnswer);
                        refreshMessages();
                    }
                }, 30000);
            }
        }
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

    private String getGeneratedDate() {
        Date date = new Date(System.currentTimeMillis() + 2700000);
//        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH), month = Calendar.getInstance().get(Calendar.MONTH) + 1,
//                year = Calendar.getInstance().get(Calendar.YEAR);
//        String resDay, resMonth;
//        if (day < 10) {
//            resDay = "0" + day;
//        } else {
//            resDay = String.valueOf(day);
//        }
//        if (month < 10) {
//            resMonth = "0" + month;
//        } else {
//            resMonth = String.valueOf(month);
//        }
//        return resDay + "/" + resMonth + "/" + year;
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    private void notifyUser(String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                        .setContentTitle("New message")
                        .setContentText(message);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {1000};
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private void refreshMessages() {
        messages.clear();
        Cursor cursor = database.getMessages();
        if (cursor.moveToFirst()) {
            do {
                messages.add(new MessageItem(Boolean.getBoolean(cursor.getString(CURSOR_ERROR)), cursor.getInt(CURSOR_TYPE),
                        cursor.getString(CURSOR_TEXT), cursor.getInt(CURSOR_DAY), cursor.getInt(CURSOR_MONTH),
                        cursor.getInt(CURSOR_YEAR), cursor.getInt(CURSOR_HOUR), cursor.getInt(CURSOR_MINUTE)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        listMessages.setAdapter(new MessageAdapter(this, messages));
        listMessages.scrollToPosition(messages.size() - 1);
    }
}
