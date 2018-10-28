package com.example.chita.allinone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText contentView = (EditText)findViewById(R.id.content);
        Button callButton = (Button) findViewById(R.id.call);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallTheNumber(contentView.getText().toString());
            }
        });

        Button smsButton = (Button) findViewById(R.id.sms);
        final LinearLayout temp2 = (LinearLayout) findViewById(R.id.temp_frame_2);
        final LinearLayout temp3 = (LinearLayout) findViewById(R.id.temp_frame_3);

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp3.setVisibility(View.GONE);
                temp2.setVisibility(View.VISIBLE);
            }
        });

        Button accessButton = (Button) findViewById(R.id.access);

        accessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessWebsite(contentView.getText().toString());
            }
        });

        Button smsSendButton = (Button) findViewById(R.id.smssend);

        smsSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendSmsToNumber(contentView.getText().toString(), ((EditText)findViewById(R.id.smscontent)).getText().toString());
            }
        });

        Button compareButton = (Button) findViewById(R.id.compare);

        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp2.setVisibility(View.GONE);
                temp3.setVisibility(View.VISIBLE);
            }
        });


        Button compareCmdButton = (Button) findViewById(R.id.compareCmd);

        compareCmdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nextValue = (EditText) findViewById(R.id.compareValue);

                CompareUsingActivity(contentView.getText().toString(), nextValue.getText().toString());
            }
        });
    }

    private void CompareUsingActivity(String s, String s1) {
        if (s != null && s1!=null && !s.equals("") && !s1.equals("")) {
            Intent intent = new Intent(getApplicationContext(), CompareActivity.class);
            intent.putExtra("num1", Integer.parseInt(s));
            intent.putExtra("num2", Integer.parseInt(s1));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private void SendSmsToNumber(String number, String smsContent) {
        if (number != null) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"+number));  // This ensures only SMS apps respond
            intent.putExtra("sms_body", smsContent);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private void AccessWebsite(String s) {
        if (s != null) {
            Uri webpage = Uri.parse("http:"+s);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private void CallTheNumber(String number) {
        if (number != null) {
            String command = "tel:" + number;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(command));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
