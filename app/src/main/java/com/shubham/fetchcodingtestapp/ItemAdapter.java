package com.shubham.fetchcodingtestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    public ItemAdapter(Context context, int resource, List<Item> itemList){
        super(context,resource,itemList);
    }


    public View getView(int position, View convertView, ViewGroup parent){
        Item item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }

        TextView listID = (TextView) convertView.findViewById(R.id.listID);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        listID.setText(Integer.toString(item.listId));
        id.setText(Integer.toString(item.id));
        name.setText(item.name);

        return convertView;
    }
}
