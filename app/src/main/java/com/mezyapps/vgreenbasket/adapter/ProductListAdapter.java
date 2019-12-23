package com.mezyapps.vgreenbasket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.api_common.BaseApi;
import com.mezyapps.vgreenbasket.model.ProductListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<ProductListModel> productListDashboardModelArrayList;
    private  ArrayList<ProductListModel> arrayListFiltered;

    public ProductListAdapter(Context mContext, ArrayList<ProductListModel> productListDashboardModelArrayList) {
        this.mContext = mContext;
        this.productListDashboardModelArrayList = productListDashboardModelArrayList;
        this.arrayListFiltered = productListDashboardModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dashboard_adapter,parent,false);
     return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ProductListModel productListModel=productListDashboardModelArrayList.get(position);
        String imagePath= BaseApi.IMAGE_PATH+productListModel.getProd_image();
        Picasso.with(mContext).load(imagePath).into(holder.iv_profile_image);
        holder.textProductName.setText(productListModel.getProd_name());
        holder.textUnitName.setText(productListModel.getUnit_name());
        String rate="RS "+productListModel.getProd_rate();
        holder.textPrice.setText(rate);
    }


    @Override
    public int getItemCount() {
        return productListDashboardModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_profile_image;
        private TextView  textProductName,textUnitName,textPrice;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_profile_image=itemView.findViewById(R.id.iv_profile_image);
            textProductName=itemView.findViewById(R.id.textProductName);
            textUnitName=itemView.findViewById(R.id.textUnitName);
            textPrice=itemView.findViewById(R.id.textPrice);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().replaceAll("\\s","").toLowerCase().trim();
                if (charString.isEmpty() || charSequence.equals("")) {
                    productListDashboardModelArrayList = arrayListFiltered;
                } else {
                    ArrayList<ProductListModel> filteredList = new ArrayList<>();
                    for (int i = 0; i < productListDashboardModelArrayList.size(); i++) {
                        String name=productListDashboardModelArrayList.get(i).getProd_name().replaceAll("\\s","").toLowerCase().trim();
                        if ((name.contains(charString))) {
                            filteredList.add(productListDashboardModelArrayList.get(i));
                        }
                    }
                    if (filteredList.size() > 0) {
                        productListDashboardModelArrayList = filteredList;
                    } else {
                        productListDashboardModelArrayList = arrayListFiltered;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListDashboardModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListDashboardModelArrayList = (ArrayList<ProductListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
