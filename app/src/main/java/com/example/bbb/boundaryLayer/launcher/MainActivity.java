package com.example.bbb.boundaryLayer.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.HelpPopUp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            DialogFragment dialogFragment = new HelpPopUp();
            dialogFragment.show(getSupportFragmentManager(), "JOEJOE");
        });
    }

}