package com.example.chita.allinone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CompareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        int num1 = getIntent().getIntExtra("num1", 0);
        int num2 = getIntent().getIntExtra("num2", 0);
        int max = 0;
        if (num1 > num2)
            max = num1;
        else
            max = num2;

        TextView maximumValue = (TextView)findViewById(R.id.lol);
        maximumValue.setText(String.valueOf(max));

    }
}
