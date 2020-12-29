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
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bbb.R;
import com.example.bbb.boundaryLayer.ui.spinner.SpinnerAdapter;
import com.example.bbb.boundaryLayer.ui.spinner.SpinnerItem;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;

public class SettingsFragment extends Fragment {
    private SpinnerAdapter languageAdapter, sizeAdapter;
    private Spinner languageSpinner;
    private Spinner textSizeSpinner;
    private ArrayList<SpinnerItem> languages;
    private ArrayList<SpinnerItem> sizes;
    private Context fragmentContext;
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;
    private String currentFontSize;
    private Boolean textSizeStarted = false;
    private SwitchCompat themeSwitch;
    private String currentLang;
    private Boolean currentColorMode;

    //sharedprefs
    private SharedPreferences.Editor fontSizeEditor;
    private SharedPreferences.Editor languageEditor;
    private SharedPreferences.Editor colorBlindEditor;

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
        languageEditor = getActivity().getSharedPreferences("language", MODE_PRIVATE).edit();
        SharedPreferences languagePrefs = getActivity().getSharedPreferences("language", MODE_PRIVATE);
        currentLang = languagePrefs.getString("language", "No name defined");//"No name defined" is the default value.

        //Sharedprefs Fontsize pref
        fontSizeEditor = getActivity().getSharedPreferences("fontsize", MODE_PRIVATE).edit();
        SharedPreferences fontSizePrefs = getActivity().getSharedPreferences("fontsize", MODE_PRIVATE);
        currentFontSize = (fontSizePrefs.getString("fontsize", "1"));//"No name defined" is the default value.
        float convertedFontSize = Float.parseFloat(currentFontSize);

        //Sharedprefs colorblindmode mode pref
        colorBlindEditor = getActivity().getSharedPreferences("colorblind", MODE_PRIVATE).edit();
        SharedPreferences colorBlindPrefs = getActivity().getSharedPreferences("colorblind", MODE_PRIVATE);
        currentColorMode = colorBlindPrefs.getBoolean("colorblind", false);//"No name defined" is the default value.

        initSpinnerList();

        //Creating language spinner
        languageSpinner = view.findViewById(R.id.spinnerLanguage);
        languageAdapter = new SpinnerAdapter(fragmentContext, languages);
        languageSpinner.setAdapter(languageAdapter);

        //Creating textSize spinner
        textSizeSpinner = view.findViewById(R.id.spinnerTextSize);
        sizeAdapter = new SpinnerAdapter(fragmentContext, sizes);
        textSizeSpinner.setAdapter(sizeAdapter);

        //Creating themeswitch switch
        themeSwitch = view.findViewById(R.id.switchColorBlindMode);

        //Base scale
        adjustFontScale(getActivity().getResources().getConfiguration(), convertedFontSize);

        setPreviousSettings();
        setOnClicks();

        return view;
    }

    private void setPreviousSettings() {
        //When starting the app we select the previously  language
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


        //When starting the app we select the previously remembered font size
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

        if (currentColorMode.equals(false)) {
            themeSwitch.setChecked(false);
            getActivity().setTheme(R.style.Theme_BBB);

        }

        if (currentColorMode.equals(true)) {
            themeSwitch.setChecked(true);
            getActivity().setTheme(R.style.ThemeOverlay_MaterialComponents_Dark);

        }
    }

    private void setOnClicks() {

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SpinnerItem clickedItem = (SpinnerItem) adapterView.getItemAtPosition(position);
                String clickedItemName = clickedItem.getName();
                System.out.println(clickedItemName);

                switch (clickedItemName) {
                    case "Nederlands":
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
                    case "Français":
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

                if (clickedItemName.equals(getActivity().getResources().getString(R.string.small))){
                    fontSizeEditor.putString("fontsize", "0.75");
                    fontSizeEditor.apply();
                    fontSizeEditor.commit();
                    adjustFontScale(getActivity().getResources().getConfiguration(), (float) 0.75);
                } else if (clickedItemName.equals(getActivity().getResources().getString(R.string.medium))){
                    fontSizeEditor.putString("fontsize", "1");
                    fontSizeEditor.apply();
                    fontSizeEditor.commit();
                    adjustFontScale(getActivity().getResources().getConfiguration(), (float) 1);
                } else if (clickedItemName.equals(getActivity().getResources().getString(R.string.large))){
                    fontSizeEditor.putString("fontsize", "1.25");
                    fontSizeEditor.apply();
                    fontSizeEditor.commit();
                    adjustFontScale(getActivity().getResources().getConfiguration(), (float) 1.25);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                System.out.println(getActivity().getApplicationInfo().theme);

                if (b) {
                    // Call setTheme before creation of any(!) View.
                    getActivity().setTheme(R.style.ThemeOverlay_MaterialComponents_Dark);
                    colorBlindEditor.putBoolean("colorblind", b);
                    colorBlindEditor.apply();
                    colorBlindEditor.commit();
                    refreshScreen();

                }

                if (!b) {
                    getActivity().setTheme(R.style.Theme_BBB);
                    colorBlindEditor.putBoolean("colorblind", b);
                    colorBlindEditor.apply();
                    colorBlindEditor.commit();
                    refreshScreen();
                }
            }
        });

    }

    public void initSpinnerList() {
        languages = new ArrayList<>();
        languages.add(new SpinnerItem(getResources().getString(R.string.dutch), R.drawable.dutch_flag));
        languages.add(new SpinnerItem(getResources().getString(R.string.english), R.drawable.american_flag));
        languages.add(new SpinnerItem(getResources().getString(R.string.french), R.drawable.french_flag));

        sizes = new ArrayList<>();
        sizes.add(new SpinnerItem(getResources().getString(R.string.small), R.drawable.text_size));
        sizes.add(new SpinnerItem(getResources().getString(R.string.medium), R.drawable.text_size));
        sizes.add(new SpinnerItem(getResources().getString(R.string.large), R.drawable.text_size));
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
        currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
        getActivity().setContentView(R.layout.activity_main);
        getActivity().onConfigurationChanged(getActivity().getResources().getConfiguration());
    }

}

