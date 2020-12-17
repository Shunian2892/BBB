package com.example.bbb.boundaryLayer.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bbb.R;

import org.osmdroid.views.MapView;

public class RoutePopUp extends DialogFragment {
private IMapChanged mapChanged;

    public RoutePopUp(IMapChanged mapChanged) {
        this.mapChanged = mapChanged;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View myview = inflater.inflate(R.layout.pop_up_route, null);
        builder.setView(myview);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonOk = myview.findViewById(R.id.buttonRouteOk);
        buttonOk.setOnClickListener(view -> alertDialog.dismiss());

        Button buttonStopRoute = myview.findViewById(R.id.buttonRouteStop);
        buttonStopRoute.setOnClickListener(view ->{
            mapChanged.onMapChange();
            alertDialog.dismiss();
        });

        return alertDialog;
    }
}
