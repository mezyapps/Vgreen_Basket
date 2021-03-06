package com.mezyapps.vgreenbasket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.model.OrderHistoryModel;
import com.mezyapps.vgreenbasket.model.ProductListModel;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;
import com.mezyapps.vgreenbasket.view.activity.OrderHistoryDetailsActivity;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<OrderHistoryModel> orderHistoryModelArrayList;
    private ArrayList<OrderHistoryModel> arrayListFiltered;

    public OrderHistoryAdapter(Context mContext, ArrayList<OrderHistoryModel> orderHistoryModelArrayList) {
        this.mContext = mContext;
        this.orderHistoryModelArrayList = orderHistoryModelArrayList;
        this.arrayListFiltered = orderHistoryModelArrayList;
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

            if(status.equalsIgnoreCase("cancelled"))
            {
                holder.textStatus.setTextColor(mContext.getResources().getColor(R.color.red));
            }else if(status.equalsIgnoreCase("delivered"))
            {
                holder.textStatus.setTextColor(mContext.getResources().getColor(R.color.green));
            }
            else{
                holder.textStatus.setTextColor(mContext.getResources().getColor(R.color.blue));
            }

            holder.textStatus.setText(status);
            String party_name= SharedLoginUtils.getUserName(mContext);
            holder.textPartyName.setText(party_name);

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
        private TextView textOrderNo,textOrderDate,textTotalPrice,textStatus,textPartyName;
        private CardView cardView_order_history;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderNo=itemView.findViewById(R.id.textOrderNo);
            textOrderDate=itemView.findViewById(R.id.textOrderDate);
            textTotalPrice=itemView.findViewById(R.id.textTotalPrice);
            textStatus=itemView.findViewById(R.id.textStatus);
            cardView_order_history=itemView.findViewById(R.id.cardView_order_history);
            textPartyName=itemView.findViewById(R.id.textPartyName);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().replaceAll("\\s", "").toLowerCase().trim();
                if (charString.isEmpty() || charSequence.equals("")) {
                    orderHistoryModelArrayList = arrayListFiltered;
                } else {
                    ArrayList<OrderHistoryModel> filteredList = new ArrayList<>();
                    for (int i = 0; i < orderHistoryModelArrayList.size(); i++) {
                        String order_id = orderHistoryModelArrayList.get(i).getOrder_id().replaceAll("\\s", "").toLowerCase().trim();
                        if ((order_id.contains(charString))) {
                            filteredList.add(orderHistoryModelArrayList.get(i));
                        }
                    }
                    if (filteredList.size() > 0) {
                        orderHistoryModelArrayList = filteredList;
                    } else {
                        orderHistoryModelArrayList = arrayListFiltered;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = orderHistoryModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                orderHistoryModelArrayList = (ArrayList<OrderHistoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
