package org.tensorflow.lite.examples.facerecognition;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.LatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.SmoothieFragment;


public class Myadapter extends FragmentStateAdapter {

    public int mCount;

    public Myadapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new CoffeeFragment();
        else if(index==1) return new LatteFragment();
        else return new SmoothieFragment();
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) { return position % mCount; }


}
