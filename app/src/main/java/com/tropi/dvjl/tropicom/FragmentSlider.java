package com.tropi.dvjl.tropicom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSlider#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSlider extends Fragment {

    private static final String ARG_PARAM = "params";
    int imageUrl;

    public FragmentSlider() {
        // Required empty public constructor
    }


    public static FragmentSlider newInstance(int param) {
        FragmentSlider fragment = new FragmentSlider();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, String.valueOf(param));
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_fragment_slider, container, false);
        ImageView img=v.findViewById(R.id.img);
        Glide.with(getActivity())
                .load(imageUrl)
                .placeholder(R.drawable.miniature)
                .into(img);
        return v;
    }

}
