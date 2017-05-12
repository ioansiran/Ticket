package com.akitektuo.ticket.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akitektuo.ticket.R;
import com.akitektuo.ticket.adapter.MessageItem;
import com.akitektuo.ticket.database.DatabaseHelper;
import com.akitektuo.ticket.util.MessageGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.akitektuo.ticket.util.Constant.messageGenerator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PopupMenu popupMenu;
    private TextView textLimit;
    private EditText editMessage;

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
                messageGenerator.addMessage(editMessage.getText().toString(), false);
                return true;
            }
        });
        RecyclerView listMessages = (RecyclerView) findViewById(R.id.list_message);
        listMessages.setLayoutManager(new LinearLayoutManager(this));
        List<MessageItem> messages = new ArrayList<>();
        DatabaseHelper database = new DatabaseHelper(this);
        messageGenerator = new MessageGenerator(this, database, editMessage, messages, listMessages);
        messageGenerator.refreshMessages();
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
                messageGenerator.addMessage(editMessage.getText().toString(), true);
                break;
        }
    }
}
