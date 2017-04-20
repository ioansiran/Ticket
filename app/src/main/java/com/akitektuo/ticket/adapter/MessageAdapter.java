package com.akitektuo.ticket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akitektuo.ticket.R;

import java.util.List;

/**
 * Created by AoD Akitektuo on 20-Apr-17 at 18:28.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<MessageItem> items;

    public MessageAdapter(Context context, List<MessageItem> objects) {
        this.context = context;
        items = objects;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder viewHolder, int position) {
        MessageItem item = items.get(position);
        if (item.getType() == 0) {
            viewHolder.layoutSender.setVisibility(View.VISIBLE);
            viewHolder.layoutReceiver.setVisibility(View.GONE);
            viewHolder.textSenderMessage.setText(item.getText());
            viewHolder.textSenderDate.setText(context.getString(R.string.date, item.getDay(),
                    convertMonth(item.getMonth()), item.getYear(), convertTime(item.getHour()), convertTime(item.getMinute())));
        } else {
            viewHolder.layoutReceiver.setVisibility(View.VISIBLE);
            viewHolder.layoutSender.setVisibility(View.GONE);
            viewHolder.textReceiverMessage.setText(item.getText());
            viewHolder.textReceiverDate.setText(context.getString(R.string.date, item.getDay(),
                    convertMonth(item.getMonth()), item.getYear(), convertTime(item.getHour()), convertTime(item.getMinute())));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String convertMonth(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "ERROR";
        }
    }

    private String convertTime(int time) {
        if (time < 10) {
            return "0" + time;
        }
        return String.valueOf(time);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutSender;
        TextView textSenderMessage;
        TextView textSenderDate;
        LinearLayout layoutReceiver;
        TextView textReceiverMessage;
        TextView textReceiverDate;

        ViewHolder(View view) {
            super(view);
            layoutSender = (LinearLayout) view.findViewById(R.id.layout_sender);
            textSenderMessage = (TextView) view.findViewById(R.id.text_sender_message);
            textSenderDate = (TextView) view.findViewById(R.id.text_sender_date);
            layoutReceiver = (LinearLayout) view.findViewById(R.id.layout_receiver);
            textReceiverMessage = (TextView) view.findViewById(R.id.text_receiver_message);
            textReceiverDate = (TextView) view.findViewById(R.id.text_receiver_date);
        }
    }

}
