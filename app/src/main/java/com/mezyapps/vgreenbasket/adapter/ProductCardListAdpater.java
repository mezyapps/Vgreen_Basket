package com.mezyapps.vgreenbasket.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.api_common.BaseApi;
import com.mezyapps.vgreenbasket.db.AppDatabase;
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.utils.ReferenceCardUiInterface;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class ProductCardListAdpater extends RecyclerView.Adapter<ProductCardListAdpater.MyViewHolder> {

    private Context mContext;
    private ArrayList<CardProductModel> cardProductModelArrayList;
    final AppDatabase appDatabase;
    private ReferenceCardUiInterface referenceCardUiInterface;

    public ProductCardListAdpater(Context mContext, ArrayList<CardProductModel> cardProductModelArrayList,ReferenceCardUiInterface referenceCardUiInterface) {
        this.mContext = mContext;
        this.cardProductModelArrayList = cardProductModelArrayList;
        appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "VgreenDB").allowMainThreadQueries().build();
        this.referenceCardUiInterface=referenceCardUiInterface;
    }

    @NonNull
    @Override
    public ProductCardListAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_product_adapter, parent, false);
        return new ProductCardListAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductCardListAdpater.MyViewHolder holder, final int position) {
        final CardProductModel cardProductModel = cardProductModelArrayList.get(position);
        final long product_id = cardProductModel.getProduct_id();
        String imagePath = BaseApi.BASE_URL+cardProductModel.getImagepath();
        Picasso.with(mContext).load(imagePath).into(holder.iv_product_image);

        final long qty = cardProductModel.getQty();
        String unit = cardProductModel.getUnit() + " " + cardProductModel.getWeight() + " - ";
        long mrpTotal = qty * cardProductModel.getMrp_total();
        long totalRate = qty * cardProductModel.getPrice_total();
        String rate = "Rs " + cardProductModel.getPrice_total();
        String mrp = "Rs " + cardProductModel.getMrp_total() + " ";
        holder.textUnitName.setText(unit);
        holder.textMrp.setText(mrp);
        holder.textProductName.setText(cardProductModel.getProduct_name());
        holder.textMrp.setPaintFlags(holder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.textPrice.setText(rate);
        holder.textQty.setText(String.valueOf(cardProductModel.getQty()));

        holder.ll_add_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long qtyVal = qty + 1;
                cardProductModel.setProduct_id(product_id);
                cardProductModel.setProduct_name(cardProductModel.getProduct_name());
                cardProductModel.setUnit(cardProductModel.getUnit());
                cardProductModel.setWeight(cardProductModel.getWeight());
                cardProductModel.setPrice(cardProductModel.getPrice());
                cardProductModel.setMrp(cardProductModel.getMrp());
                long mrpTotal = qtyVal * cardProductModel.getMrp();
                long totalRate = qtyVal * cardProductModel.getPrice();
                cardProductModel.setMrp_total(mrpTotal);
                cardProductModel.setPrice_total(totalRate);
                cardProductModel.setQty(qtyVal);

                long idVal = appDatabase.getProductDAO().getProductDataUpdate(qtyVal, product_id, totalRate, mrpTotal);
               /* Toast.makeText(mContext, String.valueOf(idVal), Toast.LENGTH_SHORT).show();*/
                cardProductModelArrayList.set(position, cardProductModel);
                notifyDataSetChanged();
                referenceCardUiInterface.reference();
            }
        });
        holder.ll_remove_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long qtyVal = qty - 1;
                if (qtyVal == 0) {
                    Toast.makeText(mContext, "Less Then 1 Qty Not Allow", Toast.LENGTH_SHORT).show();
                } else {
                    cardProductModel.setProduct_id(product_id);
                    cardProductModel.setProduct_name(cardProductModel.getProduct_name());
                    cardProductModel.setUnit(cardProductModel.getUnit());
                    cardProductModel.setWeight(cardProductModel.getWeight());
                    cardProductModel.setPrice(cardProductModel.getPrice());
                    cardProductModel.setMrp(cardProductModel.getMrp());
                    long mrpTotal = qtyVal * cardProductModel.getMrp();
                    long totalRate = qtyVal * cardProductModel.getPrice();
                    cardProductModel.setMrp_total(mrpTotal);
                    cardProductModel.setPrice_total(totalRate);
                    cardProductModel.setQty(qtyVal);

                    long idVal = appDatabase.getProductDAO().getProductDataUpdate(qtyVal, product_id, totalRate, mrpTotal);
                  /*  Toast.makeText(mContext, String.valueOf(idVal), Toast.LENGTH_SHORT).show();*/
                    cardProductModelArrayList.set(position, cardProductModel);
                    notifyDataSetChanged();
                    referenceCardUiInterface.reference();
                }
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardProductModelArrayList.remove(position);
                appDatabase.getProductDAO().deleteProduct(cardProductModel);
                notifyDataSetChanged();
                referenceCardUiInterface.reference();
            }
        });
    }
    @Override
    public int getItemCount() {
        return cardProductModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_product_image, iv_delete;
        private TextView textProductName, textUnitName, textPrice, textMrp, textQty;
        private LinearLayout ll_remove_qty, ll_add_qty;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            textProductName = itemView.findViewById(R.id.textProductName);
            textUnitName = itemView.findViewById(R.id.textUnitName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textMrp = itemView.findViewById(R.id.textMrp);
            ll_remove_qty = itemView.findViewById(R.id.ll_remove_qty);
            ll_add_qty = itemView.findViewById(R.id.ll_add_qty);
            textQty = itemView.findViewById(R.id.textQty);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}

