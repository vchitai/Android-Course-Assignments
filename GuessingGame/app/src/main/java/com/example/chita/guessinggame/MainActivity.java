package com.example.chita.guessinggame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int[] mImgIds = new int[] {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4, R.drawable.img_5};
    private String[] mAns = new String[] {"hop dong", "thu cong", "la ban", "tai hoa", "cau cu"};
    private int curQuestionId = 0;
    private int curScore = 0;

    ImageView imageView;
    TextView tvScore;
    CheckBox checkBox;
    EditText edtBetScore, edtAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        displayNextQuestion(0);
    }

    private void displayNextQuestion(int id) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), mImgIds[id]);
        imageView.setImageBitmap(bmp);
        checkBox.setChecked(false);
        edtBetScore.setText("");
        edtAns.setText("");
    }

    private void initComponents() {
        imageView = (ImageView) findViewById(R.id.imageView);
        tvScore = (TextView) findViewById(R.id.tvScore);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        edtBetScore = (EditText) findViewById(R.id.edtBetScore);
        edtAns = (EditText) findViewById(R.id.edtAns);
    }


    public void onClick_btnAns(View view) {
        boolean betFlage = false;
        int betScore = 0;

        if (checkBox.isChecked()) {
            betFlage = true;
            try {
                betScore = Integer.parseInt(edtBetScore.getText().toString());
                if (betScore < 0 || betScore > curScore) {
                    Toast.makeText(this, "Điểm cược phải không âm và nhỏ hơn số điểm hiện có", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Số điểm cược không hợp lệ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
        }

        String ans = edtAns.getText().toString();
        if (ans.length() == 0) {
            Toast.makeText(this, "Chưa nhập câu trả lời", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ans.toLowerCase().equals(mAns[curQuestionId])) {
            Toast.makeText(this, "Xin chúc mừng câu trả lời đúng", Toast.LENGTH_SHORT).show();
            curScore += 50;

            if (betFlage) curScore += betScore;
            curQuestionId++;
        }
        else {
            Toast.makeText(this, "Câu trả lời chưa chính xác", Toast.LENGTH_SHORT).show();
            curScore -= 50;

            if (betFlage) curScore -= betScore;
            curScore = curScore <= 0? 0: curScore;
        }

        tvScore.setText("Điểm: " + String.valueOf(curScore));
        displayNextQuestion(curQuestionId);
    }
}
