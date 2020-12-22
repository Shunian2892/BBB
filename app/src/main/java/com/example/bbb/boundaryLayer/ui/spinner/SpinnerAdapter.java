package com.example.bbb.boundaryLayer.ui.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bbb.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    public SpinnerAdapter(Context context, ArrayList<SpinnerItem> itemList){
        super(context, 0, itemList);
    }

    //getView and getDropDownView both need to return the same view. Therefor a new method "initView" which returns the desired view such that the getView and getDropDownView only need to cal initView.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        //Check if convertView is null, if so create and inflate layout with the layout of the spinner.
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item, parent, false
            );
        }

        ImageView methodImage = convertView.findViewById(R.id.spinner_image);
        TextView methodText = convertView.findViewById(R.id.spinner_name);

        SpinnerItem currentItem = getItem(position);

        if(currentItem != null){
            methodImage.setImageResource(currentItem.getImage());
            methodText.setText(currentItem.getName());
        }

        return convertView;
    }
}
