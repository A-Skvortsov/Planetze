package com.example.planetze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListData> {


    public ListAdapter(@NonNull Context context, @NonNull ArrayList<ListData> arrayList) {
        super(context,R.layout.item, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        ListData listData = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView descriptionview = view.findViewById(R.id.description);
        ImageView imageView = view.findViewById(R.id.imageView);


        // set the various ids to the provided
        descriptionview.setText(listData.description);
        imageView.setImageResource(listData.images);

        return view;

    }
}
