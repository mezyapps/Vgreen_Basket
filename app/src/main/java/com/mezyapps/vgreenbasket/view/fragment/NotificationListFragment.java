package com.mezyapps.vgreenbasket.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.adapter.NotificationListAdapter;
import com.mezyapps.vgreenbasket.adapter.ProductListAdapter;
import com.mezyapps.vgreenbasket.api_common.ApiClient;
import com.mezyapps.vgreenbasket.api_common.ApiInterface;
import com.mezyapps.vgreenbasket.model.NotificationModel;
import com.mezyapps.vgreenbasket.model.SuccessModel;
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.ShowProgressDialog;
import com.mezyapps.vgreenbasket.view.activity.FruitActivity;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListFragment extends Fragment {
    private Context mContext;
    private RecyclerView recyclerView_Notification;
    private ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    private NotificationListAdapter notificationListAdapter;
    public static ApiInterface apiInterface;
    private ShowProgressDialog showProgressDialog;

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
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(mContext);
        recyclerView_Notification=view.findViewById(R.id.recyclerView_Notification);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView_Notification.setLayoutManager(linearLayoutManager);
    }

    private void events() {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            callNotificationList();
        } else {
            NetworkUtils.isNetworkNotAvailable(mContext);
        }
    }

    private void callNotificationList() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.notificationList();
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        notificationModelArrayList.clear();
                        String message = null, code = null,folder=null;
                        if (successModule != null) {
                            code = successModule.getCode();
                            folder=successModule.getFolder();
                            if (code.equalsIgnoreCase("1")) {

                                notificationModelArrayList=successModule.getNotificationModelArrayList();
                                if(notificationModelArrayList.size()!=0) {
                                    Collections.reverse(notificationModelArrayList);
                                    notificationListAdapter=new NotificationListAdapter(mContext,notificationModelArrayList,folder);
                                    recyclerView_Notification.setAdapter(notificationListAdapter);
                                    notificationListAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    // text_view_empty.setVisibility(View.VISIBLE);
                                    notificationListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                // text_view_empty.setVisibility(View.VISIBLE);
                                notificationListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(mContext, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                showProgressDialog.dismissDialog();
            }
        });

    }
}
