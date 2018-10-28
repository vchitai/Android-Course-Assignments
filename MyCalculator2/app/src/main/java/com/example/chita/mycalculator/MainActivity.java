package com.example.chita.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button button0;
    Button buttonClear;
    Button buttonCal;
    Button buttonAdd;
    Button buttonSub;
    Button buttonMul;
    Button buttonDiv;
    TextView textViewResult;

    String result;
    String tmp;
    String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
        initControlListener();
    }

    private void initControlListener() {
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("0");
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("1");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("2");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("3");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("4");
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("5");
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("6");
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("7");
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("8");
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClicked("9");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClicked();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorButtonClicked("+");
            }
        });
        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorButtonClicked("-");
            }
        });
        buttonMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorButtonClicked("X");
            }
        });
        buttonDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorButtonClicked("/");
            }
        });

        buttonCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCalButtonClicked();
            }
        });
    }

    private void onCalButtonClicked() {
        try {
            int res = 0;
            int number1 = Integer.valueOf(tmp);
            int number2 = Integer.valueOf(textViewResult.getText().toString());
            switch (operator) {
                case "+":
                    res = number1 + number2;
                    break;
                case "-":
                    res = number1 - number2;
                    break;
                case "X":
                    res = number1 * number2;
                    break;
                case "/":
                    res = number1 / number2;
                    break;
                default:
                    break;
            }
            result = String.valueOf(res);
            textViewResult.setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onOperatorButtonClicked(String operator) {
        tmp = textViewResult.getText().toString();
        onClearButtonClicked();
        this.operator = operator;
    }

    private void onClearButtonClicked() {
        result = "";
        textViewResult.setText("");
    }

    private void onNumberButtonClicked(String pos) {
        result = textViewResult.getText().toString();
        result = result + pos;
        textViewResult.setText(result);
    }

    private void initControls() {
        button1 = (Button)findViewById(R.id.button_1);
        button2 = (Button)findViewById(R.id.button_2);
        button3 = (Button)findViewById(R.id.button_3);
        button4 = (Button)findViewById(R.id.button_4);
        button5 = (Button)findViewById(R.id.button_5);
        button6 = (Button)findViewById(R.id.button_6);
        button7 = (Button)findViewById(R.id.button_7);
        button8 = (Button)findViewById(R.id.button_8);
        button9 = (Button)findViewById(R.id.button_9);
        button0 = (Button)findViewById(R.id.button_0);

        buttonAdd = (Button)findViewById(R.id.button_add);
        buttonSub = (Button)findViewById(R.id.button_sub);
        buttonMul = (Button)findViewById(R.id.button_mul);
        buttonDiv = (Button)findViewById(R.id.button_div);

        buttonClear = (Button)findViewById(R.id.button_clear);
        buttonCal = (Button)findViewById(R.id.button_cal);

        textViewResult = (TextView)findViewById(R.id.text_view_result);
    }
}
