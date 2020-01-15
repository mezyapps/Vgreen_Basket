package com.mezyapps.vgreenbasket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.model.OrderHistoryModel;
import com.mezyapps.vgreenbasket.view.activity.OrderHistoryDetailsActivity;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<OrderHistoryModel> orderHistoryModelArrayList;

    public OrderHistoryAdapter(Context mContext, ArrayList<OrderHistoryModel> orderHistoryModelArrayList) {
        this.mContext = mContext;
        this.orderHistoryModelArrayList = orderHistoryModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final OrderHistoryModel orderHistoryModel=orderHistoryModelArrayList.get(position);

        String order_no=orderHistoryModel.getOrder_id();
        String order_date=orderHistoryModel.getDate();
        String total_price=orderHistoryModel.getTotal_price();
        String status=orderHistoryModel.getStatus();
        holder.textOrderNo.setText(order_no);
        holder.textOrderDate.setText(order_date);
        holder.textTotalPrice.setText(total_price);
        holder.textStatus.setText(status);

        holder.cardView_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,OrderHistoryDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ORDER_HD", (Parcelable) orderHistoryModelArrayList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderHistoryModelArrayList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        private TextView textOrderNo,textOrderDate,textTotalPrice,textStatus;
        private CardView cardView_order_history;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderNo=itemView.findViewById(R.id.textOrderNo);
            textOrderDate=itemView.findViewById(R.id.textOrderDate);
            textTotalPrice=itemView.findViewById(R.id.textTotalPrice);
            textStatus=itemView.findViewById(R.id.textStatus);
            cardView_order_history=itemView.findViewById(R.id.cardView_order_history);

        }
    }
}
