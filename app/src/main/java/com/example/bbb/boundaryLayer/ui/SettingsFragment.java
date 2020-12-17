package com.example.bbb.boundaryLayer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

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
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;
    private ViewGroup containerGlobal;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("language", MODE_PRIVATE).edit();
        SharedPreferences prefs = getActivity().getSharedPreferences("language", MODE_PRIVATE);


        String currentLang = prefs.getString("language", "No name defined");//"No name defined" is the default value.

        System.out.println("------------" + currentLang);

        containerGlobal = container;

        selected = false;

        initSpinnerList();

        languageSpinner = view.findViewById(R.id.spinnerLanguage);
        languageAdapter = new SpinnerAdapter(fragmentContext, languages);

        languageSpinner.setAdapter(languageAdapter);

        switch (currentLang) {
            case "nl":
                languageSpinner.setSelection(0);
                setLocale("nl");
                break;
            case "en":
                languageSpinner.setSelection(1);
                setLocale("en");
                break;
            case "fr":
                languageSpinner.setSelection(2);
                setLocale("fr");
                break;
        }




        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SpinnerItem clickedItem = (SpinnerItem) adapterView.getItemAtPosition(position);
                String clickedItemName = clickedItem.getName();
                System.out.println(clickedItemName);

                switch (clickedItemName) {
                    case "Dutch":
                        setLocale("nl");
                        editor.putString("language", "nl");
                        editor.apply();
                        editor.commit();

                        break;
                    case "English":
                        setLocale("en");
                        editor.putString("language", "en");
                        editor.apply();
                        editor.commit();
                        break;
                    case "French":
                        setLocale("fr");
                        editor.putString("language", "fr");
                        editor.apply();
                        editor.commit();
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


        if (!LocaleHelper.getLanguage(fragmentContext).equals(language)) {
            LocaleHelper.setLocale(fragmentContext, language);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
            //

            currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();


            // BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

            getActivity().setContentView(R.layout.activity_main);
            getActivity().onConfigurationChanged(getActivity().getResources().getConfiguration());


        }
    }

}

