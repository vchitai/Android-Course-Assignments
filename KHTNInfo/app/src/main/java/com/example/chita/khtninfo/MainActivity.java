package com.example.chita.khtninfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Khoa> khoaList = new ArrayList<Khoa>();
        khoaList.add(new Khoa("CNTT", "HCMUS - KHTN", "LOL"));
        khoaList.add(new Khoa("Toan tin", "HCMUS - KHTN", "LOL"));
        khoaList.add(new Khoa("Sinh hoc", "HCMUS - KHTN", "LOL"));
        khoaList.add(new Khoa("Hoa hoc", "HCMUS - KHTN", "LOL"));
        khoaList.add(new Khoa("Vat ly", "HCMUS - KHTN", "LOL"));
        khoaList.add(new Khoa("Hai duong", "HCMUS - KHTN", "LOL"));
        khoaList.add(new Khoa("Moi truong", "HCMUS - KHTN", "LOL"));
        khoaList.add(new Khoa("Dia chat", "HCMUS - KHTN", "LOL"));

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new KhoaArrayAdapter(this,khoaList));
    }
}
