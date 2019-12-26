package com.mezyapps.vgreenbasket.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.adapter.NotificationListAdapter;
import com.mezyapps.vgreenbasket.model.NotificationModel;

import java.util.ArrayList;

public class NotificationListFragment extends Fragment {
    private Context mContext;
    private RecyclerView recyclerView_Notification;
    private ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    private NotificationListAdapter notificationListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_notification_list, container, false);
        mContext=getActivity();
        find_View_IDs(view);
        events();
        return  view;
    }

    private void find_View_IDs(View view) {
        recyclerView_Notification=view.findViewById(R.id.recyclerView_Notification);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView_Notification.setLayoutManager(linearLayoutManager);

     /*   for (int i=0;i<=10;i++)
        {
            NotificationModel notificationModel=new NotificationModel();
            notificationModel.setTitle("Title");
            notificationModel.setDescription("Not_Description");
            notificationModelArrayList.add(notificationModel);
        }
        notificationListAdapter=new NotificationListAdapter(mContext,notificationModelArrayList);
        recyclerView_Notification.setAdapter(notificationListAdapter);
        notificationListAdapter.notifyDataSetChanged();*/
    }

    private void events() {

    }
}
