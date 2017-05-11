package com.akitektuo.ticket.background;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.akitektuo.ticket.adapter.MessageAdapter;
import com.akitektuo.ticket.adapter.MessageItem;

import java.util.Calendar;
import java.util.Random;

import static com.akitektuo.ticket.util.Constant.TYPE_RECEIVER;

/**
 * Created by Akitektuo on 11.05.2017.
 */

public class NotificationService extends Service {

    public static NotificationHelper notificationHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH), month = Calendar.getInstance().get(Calendar.MONTH),
                        year = Calendar.getInstance().get(Calendar.YEAR), hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        minute = Calendar.getInstance().get(Calendar.MINUTE);
                String generateAnswer = "Biletul pentru linia " + line + " a fost activat. Valabil pana la " +
                        getGeneratedTime() + " in " + getGeneratedDate() + ". Cost total:0.50 EUR+Tva. Cod confirmare:"
                        + (new Random().nextInt(900000) + 100000);
                messages.add(new MessageItem(false, TYPE_RECEIVER, generateAnswer, day, month, year, hour, minute));
                database.addMessage(false, TYPE_RECEIVER, generateAnswer, day, month, year, hour, minute);
                notifyUser(generateAnswer);
                listMessages.setAdapter(new MessageAdapter(getApplicationContext(), messages));
                listMessages.scrollToPosition(messages.size() - 1);
            }
        }, 30000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
