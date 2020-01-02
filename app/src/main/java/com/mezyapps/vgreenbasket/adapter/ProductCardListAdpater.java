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
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;


import java.util.ArrayList;

public class ProductCardListAdpater  extends  RecyclerView.Adapter<ProductCardListAdpater.MyViewHolder> {

    private Context mContext;
    private ArrayList<CardProductModel> cardProductModelArrayList;

    public ProductCardListAdpater(Context mContext, ArrayList<CardProductModel> cardProductModelArrayList) {
        this.mContext = mContext;
        this.cardProductModelArrayList = cardProductModelArrayList;
    }

    @NonNull
    @Override
    public ProductCardListAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_product_adapter,parent,false);
        return new ProductCardListAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardListAdpater.MyViewHolder holder, int position) {
        final CardProductModel cardProductModel=cardProductModelArrayList.get(position);
        String unit=cardProductModel.getUnit()+" "+cardProductModel.getWeight()+" - ";
        String rate="Rs "+cardProductModel.getPrice_total();
        String mrp="Rs "+cardProductModel.getMrp_total()+" ";
        holder.textUnitName.setText(unit);
        holder.textMrp.setText(mrp);
        holder.textProductName.setText(cardProductModel.getProduct_name());
        holder.textMrp.setPaintFlags(holder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.textPrice.setText(rate);
    }


    @Override
    public int getItemCount() {
        return cardProductModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_product_image;
        private TextView textProductName, textUnitName, textPrice, textMrp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            textProductName = itemView.findViewById(R.id.textProductName);
            textUnitName = itemView.findViewById(R.id.textUnitName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textMrp = itemView.findViewById(R.id.textMrp);
        }
    }
}

