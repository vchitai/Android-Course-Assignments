package com.example.chita.minesweeper;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCreateGame(View view)
    {
        Integer rows = getNumberOfRows();
        Integer columns = getNumberOfColumns();

        generateCells(rows, columns);
    }

    private void generateCells(Integer rows, Integer columns) {

        GridLayout gridCell = (GridLayout) findViewById(R.id.gridCells);
        gridCell.setColumnCount(columns);
        gridCell.setRowCount(rows);

        Button sample = (Button)findViewById(R.id.buttonPrototype);


        Button btn;
        for (int r= 0; r<rows; r++)
            for (int c  =0; c<columns; c++)
            {
                btn = new Button(this);
                btn.setLayoutParams(sample.getLayoutParams());

                gridCell.addView(btn);
            }
    }

    private Integer getNumberOfColumns() {
        String s = ((EditText) findViewById(R.id.editTextColumns)).getText().toString();
        return Integer.parseInt(s);
    }

    private Integer getNumberOfRows() {
        String s = ((EditText) findViewById(R.id.editTextRows)).getText().toString();
        return Integer.parseInt(s);

    }
}
