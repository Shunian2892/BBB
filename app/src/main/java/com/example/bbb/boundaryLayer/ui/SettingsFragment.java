package com.example.bbb.boundaryLayer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.launcher.MainActivity;
import com.example.bbb.boundaryLayer.ui.spinner.SpinnerAdapter;
import com.example.bbb.boundaryLayer.ui.spinner.SpinnerItem;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    private SpinnerItem spinnerItem;
    private SpinnerAdapter languageAdapter, sizeAdapter;
    private Spinner languageSpinner;
    private Spinner textSize;
    private ArrayList<SpinnerItem> languages;
    private ArrayList<SpinnerItem> sizes;
    private Locale locale;
    private Context fragmentContext;
    private boolean selected;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        selected = false;

        initSpinnerList();

        languageSpinner = view.findViewById(R.id.spinnerLanguage);
        languageAdapter = new SpinnerAdapter(fragmentContext, languages);

        languageSpinner.setAdapter(languageAdapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SpinnerItem clickedItem = (SpinnerItem) adapterView.getItemAtPosition(position);
                String clickedItemName = clickedItem.getName();
                System.out.println(clickedItemName);

                switch (clickedItemName) {
                    case "Dutch":
                        setLocale("nl");
                        break;
                    case "English":
                        setLocale("en");
                        break;
                    case "French":
                        setLocale("fr");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        textSize = view.findViewById(R.id.spinnerTextSize);
        sizeAdapter = new SpinnerAdapter(fragmentContext, sizes);
        textSize.setAdapter(sizeAdapter);
        return view;
    }

    public void initSpinnerList() {
        languages = new ArrayList<>();
        languages.add(new SpinnerItem("Dutch", R.drawable.dutch_flag));
        languages.add(new SpinnerItem("English", R.drawable.american_flag));
        languages.add(new SpinnerItem("French", R.drawable.french_flag));

        sizes = new ArrayList<>();
        sizes.add(new SpinnerItem("Small", R.drawable.text_size));
        sizes.add(new SpinnerItem("Medium", R.drawable.text_size));
        sizes.add(new SpinnerItem("Large", R.drawable.text_size));
    }

    public void setLocale(String language) {
        System.out.println(LocaleHelper.getLanguage(fragmentContext));
        System.out.println(language);

        if(!LocaleHelper.getLanguage(fragmentContext).equals(language)){
            LocaleHelper.setLocale(fragmentContext,language);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
        }
    }
}
