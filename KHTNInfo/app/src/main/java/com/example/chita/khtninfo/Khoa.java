package com.example.chita.khtninfo;

import java.io.Serializable;

/**
 * Created by chita on 03/03/2018.
 */

public class Khoa implements Serializable {
    private String ten;
    private String mota;
    private String icon;

    Khoa(String t, String m, String i) {
        ten = t;
        mota = m;
        icon = i;
    }

    public String getTen() {
        return ten;
    }

    public String getMota() {
        return mota;
    }

    public String getIcon() {
        return icon;
    }
}
