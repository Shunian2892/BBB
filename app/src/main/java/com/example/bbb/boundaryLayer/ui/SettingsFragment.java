package com.example.bbb.boundaryLayer.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.spinner.SpinnerAdapter;
import com.example.bbb.boundaryLayer.ui.spinner.SpinnerItem;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;

public class SettingsFragment extends Fragment {
    private SpinnerItem spinnerItem;
    private SpinnerAdapter languageAdapter, sizeAdapter;
    private Spinner languageSpinner;
    private Spinner textSizeSpinner;
    private ArrayList<SpinnerItem> languages;
    private ArrayList<SpinnerItem> sizes;
    private Locale locale;
    private Context fragmentContext;
    private boolean selected;
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;
    private ViewGroup containerGlobal;
    private String currentFontSize;
    private Boolean textSizeStarted = false;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Sharedprefs Language pref
        SharedPreferences.Editor languageEditor = getActivity().getSharedPreferences("language", MODE_PRIVATE).edit();
        SharedPreferences languagePrefs = getActivity().getSharedPreferences("language", MODE_PRIVATE);
        String currentLang = languagePrefs.getString("language", "No name defined");//"No name defined" is the default value.

        //Sharedprefs Fontsize pref
        SharedPreferences.Editor fontSizeEditor = getActivity().getSharedPreferences("fontsize", MODE_PRIVATE).edit();
        SharedPreferences fontSizePrefs = getActivity().getSharedPreferences("fontsize", MODE_PRIVATE);
        currentFontSize = (fontSizePrefs.getString("fontsize", "1"));//"No name defined" is the default value.
        float convertedFontSize = Float.parseFloat(currentFontSize);


        System.out.println(convertedFontSize);


        containerGlobal = container;
        selected = false;
        initSpinnerList();


        //Creating language spinner
        languageSpinner = view.findViewById(R.id.spinnerLanguage);
        languageAdapter = new SpinnerAdapter(fragmentContext, languages);
        languageSpinner.setAdapter(languageAdapter);

        //Creating textSize spinner
        textSizeSpinner = view.findViewById(R.id.spinnerTextSize);
        sizeAdapter = new SpinnerAdapter(fragmentContext, sizes);
        textSizeSpinner.setAdapter(sizeAdapter);


        //Base scale
        adjustFontScale(getActivity().getResources().getConfiguration(), convertedFontSize);


        //When starting the app we select the previously remembered
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

        switch (currentFontSize) {
            case "0.75":
                textSizeSpinner.setSelection(0);
                adjustFontScale(getActivity().getResources().getConfiguration(), (float) 0.75);

                break;
            case "1":
                textSizeSpinner.setSelection(1);
                adjustFontScale(getActivity().getResources().getConfiguration(), (float) 1);
                break;

            case "1.25":
                textSizeSpinner.setSelection(2);
                adjustFontScale(getActivity().getResources().getConfiguration(), (float) 1.25);
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
                        languageEditor.putString("language", "nl");
                        languageEditor.apply();
                        languageEditor.commit();

                        break;
                    case "English":
                        setLocale("en");
                        languageEditor.putString("language", "en");
                        languageEditor.apply();
                        languageEditor.commit();
                        break;
                    case "French":
                        setLocale("fr");
                        languageEditor.putString("language", "fr");
                        languageEditor.apply();
                        languageEditor.commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });


        textSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SpinnerItem clickedItem = (SpinnerItem) adapterView.getItemAtPosition(position);
                String clickedItemName = clickedItem.getName();
                System.out.println(clickedItemName);

                switch (clickedItemName) {
                    case "Small":
                        fontSizeEditor.putString("fontsize", "0.75");
                        fontSizeEditor.apply();
                        fontSizeEditor.commit();
                        adjustFontScale(getActivity().getResources().getConfiguration(), (float) 0.75);
                        break;

                    case "Medium":
                        fontSizeEditor.putString("fontsize", "1");
                        fontSizeEditor.apply();
                        fontSizeEditor.commit();
                        adjustFontScale(getActivity().getResources().getConfiguration(), (float) 1);
                        break;

                    case "Large":
                        fontSizeEditor.putString("fontsize", "1.25");
                        fontSizeEditor.apply();
                        fontSizeEditor.commit();
                        adjustFontScale(getActivity().getResources().getConfiguration(), (float) 1.25);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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

            refreshScreen();


        }
    }


    public void adjustFontScale(Configuration configuration, float scale) {
        float convFont = Float.parseFloat(currentFontSize);


        System.out.println(convFont + " ----  " + scale);
        System.out.println(textSizeStarted);

        if (convFont != scale) {
            configuration.fontScale = scale;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);
            refreshScreen();
        }


        if (textSizeStarted == false) {
            textSizeStarted = true;
            configuration.fontScale = scale;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);
            refreshScreen();
        }


    }

    private void refreshScreen() {
        System.out.println(1);
        currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragmentTransaction = getFragmentManager().beginTransaction();
        System.out.println(2);
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        System.out.println(3);
        fragmentTransaction.commit();
        System.out.println(4);
        getActivity().setContentView(R.layout.activity_main);
        getActivity().onConfigurationChanged(getActivity().getResources().getConfiguration());
    }

}

