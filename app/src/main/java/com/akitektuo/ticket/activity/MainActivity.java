package com.akitektuo.ticket.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.akitektuo.ticket.util.Constant.TYPE_RECEIVER;
import static com.akitektuo.ticket.util.Constant.TYPE_SENDER;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PopupMenu popupMenu;
    private TextView textLimit;
    private EditText editMessage;
    private RecyclerView listMessages;
    private List<MessageItem> messages;

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
        listMessages = (RecyclerView) findViewById(R.id.list_message);
        listMessages.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();

        // get from database
        listMessages.setAdapter(new MessageAdapter(this, messages));
        listMessages.scrollToPosition(messages.size());
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
                addMessage(editMessage.getText().toString());
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void addMessage(final String text) {
        boolean pass;
        try {
            Integer.parseInt(text);
            pass = true;
        } catch (NumberFormatException exception) {
            pass = false;
        }
        messages.add(new MessageItem(!pass, TYPE_SENDER, text, Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE)));
        listMessages.setAdapter(new MessageAdapter(this, messages));
        editMessage.setText("");
        hideKeyboard();
        listMessages.scrollToPosition(messages.size());
        if (pass) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String generateAnswer = "Biletul pentru linia " + text + " a fost activat. Valabil pana la " +
                            getGeneratedTime() + " in " + getGeneratedDate() + ". Cost total:0.50 EUR+Tva. Cod confirmare:" + (new Random().nextInt(900000) + 100000);
                    messages.add(new MessageItem(false, TYPE_RECEIVER, generateAnswer, Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                            Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE)));
                    notifyUser(generateAnswer);
                    listMessages.setAdapter(new MessageAdapter(getApplicationContext(), messages));
                    listMessages.scrollToPosition(messages.size());
                    //add to db
                }
            }, 30000);
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
        long[] pattern = {500};
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
