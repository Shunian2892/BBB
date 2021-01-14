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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbb.R;
import com.example.bbb.entityLayer.data.POI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

/**
 * This class is the detail view of a specific point of interest. The text on the screen and the text to speech changes based on the set language from the settings fragment.
 */
public class POIFragment extends Fragment implements TextToSpeech.OnInitListener {
    private POI poi;
    private TextView title;
    private TextView description;
    private ImageButton ibTTS;
    private ImageButton ibBack;
    private FragmentManager fragmentManager;

    private UIViewModel viewModel;

    private VideoFragment videoFragment;
    private ImageFragment imageFragment;
    private MapFragment mapFragment;

    private Button buttonVideo;
    private Button buttonMap;
    private boolean isVideo;

    private TextToSpeech tts;

    private SharedPreferences prefs;
    private String currentLang;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);
        viewModel.setCurrentFragment(R.id.fragment_poi);

        this.title = view.findViewById(R.id.TextViewTitle);
        this.description = view.findViewById(R.id.textViewPOI);

        //Get the manually set language from shared preferences
        prefs = getActivity().getSharedPreferences("language", Context.MODE_PRIVATE);
        currentLang= prefs.getString("language", Locale.getDefault().getLanguage());


        //Get the correct POI and it's description
        poi = viewModel.getSelectedPOI().getValue();
        fragmentManager = getParentFragmentManager();


        ibBack = view.findViewById(R.id.imageButtonBack);
        ibBack.setOnClickListener(view12 -> {
            isVideo = false;
            viewModel.setIsVideoState(isVideo);
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }
        });
        title.setText(poi.POIName);

        //Set the description of the POI in the language based on the language saved in the shared preferences
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

        setImageFragment();
        setVideoFragment();
        setMapFragment();

        fragmentManager.beginTransaction().replace(R.id.detail_container, imageFragment).commit();

        isVideo = viewModel.getIsVideoState().getValue();
        buttonVideo = view.findViewById(R.id.buttonVideo);
        if (!isVideo) {
            fragmentManager.beginTransaction().replace(R.id.detail_container, imageFragment).commit();
            buttonVideo.setText(getResources().getString(R.string.show_video));
        } else {
            fragmentManager.beginTransaction().replace(R.id.detail_container, videoFragment).commit();
            buttonVideo.setText(getResources().getString(R.string.hide_video));
        }
        viewModel.setIsVideoState(isVideo);
        isVideo = !isVideo;

        buttonVideo.setOnClickListener(v -> {

            if (!isVideo) {
                fragmentManager.beginTransaction().replace(R.id.detail_container, imageFragment).commit();
                buttonVideo.setText(getResources().getString(R.string.show_video));
            } else {
                fragmentManager.beginTransaction().replace(R.id.detail_container, videoFragment).commit();
                buttonVideo.setText(getResources().getString(R.string.hide_video));
            }
            viewModel.setIsVideoState(isVideo);
            isVideo = !isVideo;

        });

        buttonMap = view.findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(view1 -> {
            viewModel.setVisiblePOI(poi);
            viewModel.setCenterOnUser(false);
            BottomNavigationView bottom = getActivity().findViewById(R.id.bottomNavigationView);
            bottom.setSelectedItemId(R.id.menu_map);
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFragment).addToBackStack(null).commit();
        });

        //Set text to speech in this activity and the onClick for the tts button. TTS speaks out the text from the description box only
        tts = new TextToSpeech(getActivity().getApplicationContext(), this::onInit);

        ibTTS = view.findViewById(R.id.imageButtonTTS);
        ibTTS.setOnClickListener(v -> {
            switch (currentLang) {
                case "en":
                    tts.speak(poi.Description_en, TextToSpeech.QUEUE_FLUSH, null, null);
                    break;
                case "fr":
                    tts.speak(poi.Description_fr, TextToSpeech.QUEUE_FLUSH, null, null);
                    break;
                case "nl":
                    tts.speak(poi.Description_nl, TextToSpeech.QUEUE_FLUSH, null, null);
                    break;
            }
        });
        return view;
    }

    /**
     * Stop the text to speech function
     */
    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();

    }

    /**
     * Set the image fragment in the POI detail view
     */
    public void setImageFragment() {
        if (fragmentManager.findFragmentById(R.id.fragment_image) == null) {
            imageFragment = new ImageFragment();
        } else {
            imageFragment = (ImageFragment) fragmentManager.findFragmentById(R.id.fragment_image);
        }

    }

    /**
     * Set the video fragment in the POI detail view
     */
    public void setVideoFragment() {
        if (fragmentManager.findFragmentById(R.id.fragment_video) == null) {
            videoFragment = new VideoFragment();
        } else {
            videoFragment = (VideoFragment) fragmentManager.findFragmentById(R.id.fragment_video);
        }

    }

    public void setMapFragment(){
        if(fragmentManager.findFragmentById(R.id.map_fragment) == null){
            mapFragment = new MapFragment();
        } else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        }
    }

    /**
     * Set the language of text to speech. The Dutch language is not supported, in this case the text will be spoken with an English accent
     * @param status
     */
    @Override
    public void onInit(int status) {
        SharedPreferences prefs = getActivity().getSharedPreferences("language", getActivity().getApplicationContext().MODE_PRIVATE);
        String currentLanguage = prefs.getString("language", "No name defined");

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
