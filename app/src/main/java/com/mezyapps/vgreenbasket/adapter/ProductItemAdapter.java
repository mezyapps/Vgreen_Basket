package com.mezyapps.vgreenbasket.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.model.NotificationModel;
import com.mezyapps.vgreenbasket.model.ProductUnitModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductItemAdapter extends  RecyclerView.Adapter<ProductItemAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ProductUnitModel> productUnitModelArrayList;

    public ProductItemAdapter(Context mContext, ArrayList<ProductUnitModel> productUnitModelArrayList) {
        this.mContext = mContext;
        this.productUnitModelArrayList = productUnitModelArrayList;
    }

    @NonNull
    @Override
    public ProductItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product_unit_adapter,parent,false);
        return new ProductItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemAdapter.MyViewHolder holder, int position) {
        final ProductUnitModel productUnitModel=productUnitModelArrayList.get(position);
        String unit=productUnitModel.getProd_unit()+" "+productUnitModel.getProd_weight()+" - ";
        String rate="Rs "+productUnitModel.getProd_rate();
        String mrp="Rs "+productUnitModel.getProd_mrp()+" ";
        holder.textUnit.setText(unit);
        holder.textMRP.setText(mrp);
        holder.textMRP.setPaintFlags(holder.textMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.textPrice.setText(rate);
    }


    @Override
    public int getItemCount() {
        return productUnitModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textUnit,textMRP,textPrice;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textUnit=itemView.findViewById(R.id.textUnit);
            textMRP=itemView.findViewById(R.id.textMRP);
            textPrice=itemView.findViewById(R.id.textPrice);
        }
    }
}
