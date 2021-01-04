package com.example.bbb.boundaryLayer.ui;

import android.content.SharedPreferences;
import android.os.Build;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbb.R;
import com.example.bbb.entityLayer.data.POI;

import java.util.Locale;

public class POIFragment extends Fragment implements TextToSpeech.OnInitListener {
    private POI poi;
    private TextView title;
    private TextView description;
    private ImageButton ibTTS;
    private ImageButton ibBack;
    private POIListFragment poiListFragment;
    private FragmentManager fragmentManager;

    private VideoFragment videoFragment;
    private ImageFragment imageFragment;

    private Button buttonVideo;
    private boolean isVideo;

    private UIViewModel viewModel;

    private TextToSpeech tts;

    private SharedPreferences prefs;
    private String currentLang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);
        viewModel.setCurrentFragment(R.id.fragment_poi);

        this.title = (TextView) view.findViewById(R.id.TextViewTitle);
        this.description = (TextView) view.findViewById(R.id.textViewPOI);

        prefs = getActivity().getSharedPreferences("language", Context.MODE_PRIVATE);
        currentLang= prefs.getString("language", Locale.getDefault().getLanguage());//"No name defined" is the default value.

        isVideo = false;


        poi = viewModel.getSelectedPOI().getValue();

        ibBack = view.findViewById(R.id.imageButtonBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });
        title.setText(poi.POIName);

        switch (currentLang) {
            case "en":
                description.setText(poi.Description_en);
                break;
            case "fr":
                description.setText(poi.Description_fr);
                break;
            case "nl":
                description.setText(poi.Description_nl);
                break;
        }


        fragmentManager = getFragmentManager();
        setImageFragment();
        setVideoFragment();

        fragmentManager.beginTransaction().replace(R.id.detail_container, imageFragment).commit();

        buttonVideo = view.findViewById(R.id.buttonVideo);
        buttonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVideo) {
                    fragmentManager.beginTransaction().replace(R.id.detail_container, imageFragment).commit();
                    buttonVideo.setText(getResources().getString(R.string.show_video));
                } else {
                    fragmentManager.beginTransaction().replace(R.id.detail_container, videoFragment).commit();
                    buttonVideo.setText(getResources().getString(R.string.hide_video));
                }
                isVideo = !isVideo;
            }
        });

        tts = new TextToSpeech(getActivity().getApplicationContext(), this::onInit);

        ibTTS = view.findViewById(R.id.imageButtonTTS);
        ibTTS.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                switch (currentLang) {
                    case "en":
                        tts.speak(poi.Description_en, TextToSpeech.QUEUE_FLUSH, null, null);                        break;
                    case "fr":
                        tts.speak(poi.Description_fr, TextToSpeech.QUEUE_FLUSH, null, null);                        break;
                    case "nl":
                        tts.speak(poi.Description_nl, TextToSpeech.QUEUE_FLUSH, null, null);                        break;
                }
            }
        });
        return view;
    }

    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    public void setImageFragment() {
        if (fragmentManager.findFragmentById(R.id.fragment_image) == null) {
            imageFragment = new ImageFragment();
        } else {
            imageFragment = (ImageFragment) fragmentManager.findFragmentById(R.id.fragment_image);
        }

    }

    public void setVideoFragment() {
        if (fragmentManager.findFragmentById(R.id.fragment_video) == null) {
            videoFragment = new VideoFragment();
        } else {
            videoFragment = (VideoFragment) fragmentManager.findFragmentById(R.id.fragment_video);
        }

    }

    @Override
    public void onInit(int status) {
        SharedPreferences prefs = getActivity().getSharedPreferences("language", getActivity().getApplicationContext().MODE_PRIVATE);
        String currentLanguage = prefs.getString("language", "No name defined");

        Log.d("IN INIT", "in init of tts ##########################");
        if (status == TextToSpeech.SUCCESS) {
            switch (currentLanguage) {
                case "en":
                    tts.setLanguage(Locale.ENGLISH);
                    break;
                case "fr":
                    tts.setLanguage(Locale.FRANCE);
                    break;
                case "nl":
                    tts.setLanguage(Locale.ENGLISH);
                    break;
            }
        }

    }
}
