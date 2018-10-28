package com.example.chita.mysender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = ((EditText)findViewById(R.id.msg_sender)).getText().toString();

                Intent intent = new Intent("com.company.package.RECEIVER");
                intent.putExtra("message", msg);
                startActivity(intent);
            }
        });
    }
}
