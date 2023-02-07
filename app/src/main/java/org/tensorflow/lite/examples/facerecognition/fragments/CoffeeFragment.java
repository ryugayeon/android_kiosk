package org.tensorflow.lite.examples.facerecognition.fragments;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.tensorflow.lite.examples.facerecognition.R;

public class CoffeeFragment extends Fragment {

    private TextToSpeech tts;
    private Button button3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_coffee, container, false);

        return rootView;
    }



}
