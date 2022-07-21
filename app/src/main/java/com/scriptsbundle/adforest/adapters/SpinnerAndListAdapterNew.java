package com.scriptsbundle.adforest.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.modelsList.subcatDiloglist;

import java.util.ArrayList;

public class SpinnerAndListAdapterNew extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private ArrayList<subcatDiloglist> itemList;
    private boolean active = false;
    private Activity activity;

    public SpinnerAndListAdapterNew(Activity activity, ArrayList<subcatDiloglist> list) {
        itemList = list;
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public SpinnerAndListAdapterNew(Activity activity, ArrayList<subcatDiloglist> list, boolean s) {
        itemList = list;
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        active = s;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final subcatDiloglist item = itemList.get(position);
        View vi = convertView;
        if (convertView == null) {
            if (active)
                vi = inflater.inflate(R.layout.spinner_item_medium_new, null);
            else
                vi = inflater.inflate(R.layout.spinner_itemlarge, null);
        }


        vi.setTag(item);
        TextView name = vi.findViewById(R.id.text_view_name);
        if (active) {
            ImageView ivLogo = vi.findViewById(R.id.ivLogo);
            Glide.with(activity).load(item.getImage_url()).placeholder(R.drawable.placeholder).into(ivLogo);
        }
        name.setText(item.getName());

        return vi;
    }


}