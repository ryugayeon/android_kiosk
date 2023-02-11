package org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tensorflow.lite.examples.facerecognition.R;

public class DolceLatteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dolce_latte, container, false);
    }
}