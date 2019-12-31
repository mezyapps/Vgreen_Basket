package com.mezyapps.vgreenbasket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.api_common.BaseApi;
import com.mezyapps.vgreenbasket.model.NotificationModel;
import com.mezyapps.vgreenbasket.model.ProductListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<NotificationModel> notificationModelArrayList;
    private String folder;
    public NotificationListAdapter(Context mContext, ArrayList<NotificationModel> notificationModelArrayList,String folder) {
        this.mContext = mContext;
        this.notificationModelArrayList = notificationModelArrayList;
        this.folder=folder;
    }

    @NonNull
    @Override
    public NotificationListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notification_adapter,parent,false);
        return new NotificationListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.MyViewHolder holder, int position) {
        final NotificationModel notificationModel=notificationModelArrayList.get(position);

        String imagePath = BaseApi.BASE_URL + folder + notificationModel.getImage();
        Picasso.with(mContext).load(imagePath).into(holder.iv_notification_image);
        holder.textTitle.setText(notificationModel.getTitle());
        holder.textDescription.setText(notificationModel.getDescription());
    }


    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_notification_image;
        private TextView textDescription,textTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescription=itemView.findViewById(R.id.textDescription);
            textTitle=itemView.findViewById(R.id.textTitle);
            iv_notification_image=itemView.findViewById(R.id.iv_notification_image);
        }
    }
}


