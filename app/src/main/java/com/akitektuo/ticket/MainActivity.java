package com.akitektuo.ticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                Toast.makeText(getApplicationContext(), "This will send the text \"" + editMessage.getText().toString() + "\".", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
