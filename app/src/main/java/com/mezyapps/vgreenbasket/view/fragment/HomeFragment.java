package com.mezyapps.vgreenbasket.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.view.activity.FruitActivity;
import com.mezyapps.vgreenbasket.view.activity.VegetableActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends Fragment {
    private  Context mContext;
    private RelativeLayout  rr_vegetable,rr_fruit;
    //Image Slider Image Array;
    private int[] sampleImages = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4, R.drawable.slider5};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        mContext=getActivity();
        find_View_IdS(view);
        events();
        return view;
    }


    private void find_View_IdS(View view) {
        rr_vegetable=view.findViewById(R.id.rr_vegetable);
        rr_fruit=view.findViewById(R.id.rr_fruit);


        //ImageSlider
        CarouselView carouselView=view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

    }
    private void events() {
        rr_vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VegetableActivity.class));
            }
        });
        rr_fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FruitActivity.class));
            }
        });
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
}
