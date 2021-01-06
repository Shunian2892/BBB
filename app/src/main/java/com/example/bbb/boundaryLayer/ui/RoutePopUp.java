package com.example.bbb.boundaryLayer.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbb.R;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;

import java.util.List;
import java.util.Locale;

public class RoutePopUp extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pop_up_route, null);
        builder.setView(view);

        UIViewModel viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonOk = view.findViewById(R.id.buttonRouteOk);
        buttonOk.setOnClickListener(view1 -> alertDialog.dismiss());

        Button buttonStopRoute = view.findViewById(R.id.buttonRouteStop);
        buttonStopRoute.setOnClickListener(view2 -> {

            //


            viewModel.getIMapChanged().onMapChange();
            alertDialog.dismiss();
        });

        TextView textViewRouteName = view.findViewById(R.id.textViewRouteName);
        TextView textViewNextPOI = view.findViewById(R.id.textViewNextPOI);
        TextView textViewLastPOI = view.findViewById(R.id.textViewLastPOI);
        TextView textViewProgress = view.findViewById(R.id.textViewProgress);

        Route selectedRoute = viewModel.getRoutePopUpSelectedRoute().getValue();
        List<POI> poiList = DatabaseManager.getInstance().getPOIsFromRoute(selectedRoute.ID);

        SharedPreferences prefs = getActivity().getSharedPreferences("language", Context.MODE_PRIVATE);
        String currentLang = prefs.getString("language", Locale.getDefault().getLanguage());//"No name defined" is the default value.
        switch (currentLang) {
            case "en":
                textViewRouteName.setText(getResources().getString(R.string.route) + selectedRoute.RouteName_en);
                break;
            case "fr":
                textViewRouteName.setText(getResources().getString(R.string.route) + selectedRoute.RouteName_fr);
                break;
            case "nl":
                textViewRouteName.setText(getResources().getString(R.string.route) + selectedRoute.RouteName_nl);
                break;
        }

        for (int i = 0; i < poiList.size(); i++) {
            POI poi = poiList.get(i);
            if (!poi.IsVisited) {
                textViewNextPOI.setText(getResources().getString(R.string.heading_to)+ poi.POIName);

                if (i != 0) {
                    textViewLastPOI.setText(getResources().getString(R.string.last_poi) + poiList.get(i - 1).POIName);
                } else {
                    textViewLastPOI.setText(getResources().getString(R.string.last_poi) + "-");
                }
            }
        }

        textViewProgress.setText(getResources().getString(R.string.progress) + "???");

        return alertDialog;
    }
}
