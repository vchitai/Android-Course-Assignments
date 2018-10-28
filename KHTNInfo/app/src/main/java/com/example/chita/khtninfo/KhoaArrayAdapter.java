package com.example.chita.khtninfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chita on 03/03/2018.
 */

public class KhoaArrayAdapter extends ArrayAdapter<Khoa> {
    Context mContext;
    ArrayList<Khoa> khoaList;

    public KhoaArrayAdapter(Context context, ArrayList<Khoa> kl) {
        super(context, 0);
        mContext = context;
        khoaList = kl;
    }


    @Override
    public int getCount() {
        return khoaList.size();
    }

    @Override
    public Khoa getItem(int position) {
        return khoaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (position%2 == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout1, null);
                holder.icon = (ImageView)convertView.findViewById(R.id.iv1);
                holder.textview1 = (TextView) convertView.findViewById(R.id.tv1_1);
                holder.textview2 = (TextView) convertView.findViewById(R.id.tv1_2);
            }
            else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout2, null);
                holder.icon = (ImageView)convertView.findViewById(R.id.iv2);
                holder.textview1 = (TextView) convertView.findViewById(R.id.tv2_1);
                holder.textview2 = (TextView) convertView.findViewById(R.id.tv2_2);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Khoa k = khoaList.get(position);
        holder.textview1.setText(k.getTen());
        holder.textview2.setText(k.getMota());

        return convertView;
    }

    static class ViewHolder {
        ImageView icon;
        TextView textview1;
        TextView textview2;
    }
}
