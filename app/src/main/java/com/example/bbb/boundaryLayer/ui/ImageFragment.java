package com.example.bbb.boundaryLayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.bbb.R;
import com.example.bbb.entityLayer.data.POI;

public class ImageFragment extends Fragment {

    private ImageView imageView;
    private UIViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_image, container, false);

        viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);
        POI poi = viewModel.getSelectedPOI().getValue();

        imageView = view.findViewById(R.id.imageViewPOI);
        Glide.with(getContext()).load(poi.imageURL).into(imageView);
        return view;
    }
}
