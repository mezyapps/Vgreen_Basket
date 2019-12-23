package com.mezyapps.vgreenbasket.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mezyapps.vgreenbasket.R;

public class CardFragment extends Fragment {
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_card, container, false);
        mContext=getActivity();

        find_View_IdS(view);
        events();
        return view;
    }

    private void find_View_IdS(View view) {

    }

    private void events() {

    }
}
