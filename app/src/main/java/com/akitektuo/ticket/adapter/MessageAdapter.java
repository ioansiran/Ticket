package com.akitektuo.ticket.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akitektuo.ticket.R;
import com.akitektuo.ticket.database.DatabaseHelper;

import java.util.List;

import static com.akitektuo.ticket.util.Constant.messageGenerator;

/**
 * Created by AoD Akitektuo on 20-Apr-17 at 18:28.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<MessageItem> items;
    private DatabaseHelper database;

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
        final MessageItem item = items.get(position);
        database = new DatabaseHelper(context);
        viewHolder.layoutMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Message options");
                builder.setItems(R.array.message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            AlertDialog.Builder builderDelete = new AlertDialog.Builder(context)
                                    .setTitle("Delete")
                                    .setMessage("This message will be deleted")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            database.deleteMessage(item.getText(), item.getDay(), item.getMonth(),
                                                    item.getYear(), item.getHour(), item.getMinute());
                                            messageGenerator.refreshMessages();
                                            Toast.makeText(context, "Message deleted...", Toast.LENGTH_LONG).show();
                                        }
                                    }).setNegativeButton("Cancel", null);
                            final AlertDialog alertDialogDelete = builderDelete.create();
                            alertDialogDelete.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    alertDialogDelete.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                    alertDialogDelete.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                }
                            });
                            alertDialogDelete.show();
                        }
                    }
                });
                builder.setNeutralButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        if (item.getType() == 0) {
            viewHolder.layoutSender.setVisibility(View.VISIBLE);
            viewHolder.layoutReceiver.setVisibility(View.GONE);
            if (item.isError()) {
                viewHolder.imageSenderError.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imageSenderError.setVisibility(View.GONE);
            }
            viewHolder.textSenderMessage.setText(item.getText());
            viewHolder.textSenderDate.setText(context.getString(R.string.date, item.getDay(),
                    convertMonth(item.getMonth()), item.getYear(), convertTime(item.getHour()), convertTime(item.getMinute())));
        } else {
            viewHolder.layoutReceiver.setVisibility(View.VISIBLE);
            viewHolder.layoutSender.setVisibility(View.GONE);
            if (item.isError()) {
                viewHolder.imageReceiverError.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imageReceiverError.setVisibility(View.GONE);
            }
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
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sept";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
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
        RelativeLayout layoutMessage;
        LinearLayout layoutSender;
        ImageView imageSenderError;
        TextView textSenderMessage;
        TextView textSenderDate;
        LinearLayout layoutReceiver;
        ImageView imageReceiverError;
        TextView textReceiverMessage;
        TextView textReceiverDate;

        ViewHolder(View view) {
            super(view);
            layoutMessage = (RelativeLayout) view.findViewById(R.id.layout_message);
            layoutSender = (LinearLayout) view.findViewById(R.id.layout_sender);
            imageSenderError = (ImageView) view.findViewById(R.id.image_sender_error);
            textSenderMessage = (TextView) view.findViewById(R.id.text_sender_message);
            textSenderDate = (TextView) view.findViewById(R.id.text_sender_date);
            layoutReceiver = (LinearLayout) view.findViewById(R.id.layout_receiver);
            imageReceiverError = (ImageView) view.findViewById(R.id.image_receiver_error);
            textReceiverMessage = (TextView) view.findViewById(R.id.text_receiver_message);
            textReceiverDate = (TextView) view.findViewById(R.id.text_receiver_date);
        }
    }

}
