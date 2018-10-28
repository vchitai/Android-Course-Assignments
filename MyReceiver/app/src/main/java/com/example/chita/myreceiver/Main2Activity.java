package com.example.chita.myreceiver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String msg = getIntent().getStringExtra("message");
        TextView tv = (TextView) findViewById(R.id.msg_show);
        tv.setText(msg);
    }
}
