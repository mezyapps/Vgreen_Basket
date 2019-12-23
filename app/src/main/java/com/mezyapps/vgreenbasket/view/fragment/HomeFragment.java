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
    private RelativeLayout rr_VegetableView,rr_FruitView;
    private LinearLayout ll_potato,ll_tomato,ll_onion,ll_bell_paper,ll_apple,ll_orange,ll_watermelon,ll_banana;
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
        rr_VegetableView=view.findViewById(R.id.rr_VegetableView);
        rr_FruitView=view.findViewById(R.id.rr_FruitView);
        ll_potato=view.findViewById(R.id.ll_potato);
        ll_tomato=view.findViewById(R.id.ll_tomato);
        ll_onion=view.findViewById(R.id.ll_onion);
        ll_bell_paper=view.findViewById(R.id.ll_bell_paper);
        ll_apple=view.findViewById(R.id.ll_apple);
        ll_orange=view.findViewById(R.id.ll_orange);
        ll_watermelon=view.findViewById(R.id.ll_watermelon);
        ll_banana=view.findViewById(R.id.ll_banana);

        //ImageSlider
        CarouselView carouselView=view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

    }
    private void events() {
        rr_VegetableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VegetableActivity.class));
            }
        });
        ll_potato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VegetableActivity.class));
            }
        });
        ll_tomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VegetableActivity.class));
            }
        });
        ll_onion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VegetableActivity.class));
            }
        });
        ll_bell_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VegetableActivity.class));
            }
        });

        rr_FruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FruitActivity.class));
            }
        });

        ll_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FruitActivity.class));
            }
        });
        ll_orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FruitActivity.class));
            }
        });
        ll_watermelon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FruitActivity.class));
            }
        });
        ll_banana.setOnClickListener(new View.OnClickListener() {
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
