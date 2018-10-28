package com.example.chita.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    View.OnClickListener    onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            String s1 = ((EditText) findViewById(R.id.operand1)).getText().toString();
            String s2 = ((EditText) findViewById(R.id.operand2)).getText().toString();

            Integer a1, a2;
            a1 = Integer.parseInt(s1);
            a2 = Integer.parseInt(s2);

            Integer res = 0;

            if (v==findViewById(R.id.buttonAdd))
                res = a1+a2;
            else if (v==findViewById(R.id.buttonSubtract))
                res = a1-a2;
            else if (v==findViewById(R.id.buttonMultiply))
                res = a1 * a2;
            else if (v==findViewById(R.id.buttonDivide))
                if (a2 != 0)
                    res = a1 / a2;
                else res = 0;

            ((EditText)findViewById(R.id.result)).setText(res.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.buttonAdd)).setOnClickListener(onClickListener);
        ((Button)findViewById(R.id.buttonSubtract)).setOnClickListener(onClickListener);
        ((Button)findViewById(R.id.buttonMultiply)).setOnClickListener(onClickListener);
        ((Button)findViewById(R.id.buttonDivide)).setOnClickListener(onClickListener);
    }
}
