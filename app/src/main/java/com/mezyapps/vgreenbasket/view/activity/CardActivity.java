package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.adapter.ProductCardListAdpater;
import com.mezyapps.vgreenbasket.db.AppDatabase;
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.utils.ReferenceCardUiInterface;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity implements ReferenceCardUiInterface {
    private ImageView iv_back;
    private RecyclerView recyclerView_Card;
    private AppDatabase appDatabase;
    private ArrayList<CardProductModel> cardProductModelArrayList = new ArrayList<>();
    private ProductCardListAdpater productCardListAdpater;
    private TextView textTotalAmt,textTotalSavedAmt;
    private Button btn_checkout;
    private LinearLayout ll_cart_bottom,ll_recyclerView_Card;
    private ImageView iv_no_data_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        find_View_ID();
        events();
    }

    private void find_View_ID() {
        appDatabase= AppDatabase.getInStatce(CardActivity.this);
        iv_back=findViewById(R.id.iv_back);
        recyclerView_Card=findViewById(R.id.recyclerView_Card);
        textTotalAmt=findViewById(R.id.textTotalAmt);
        btn_checkout=findViewById(R.id.btn_checkout);
        textTotalSavedAmt=findViewById(R.id.textTotalSavedAmt);
        ll_cart_bottom=findViewById(R.id.ll_cart_bottom);
        iv_no_data_found=findViewById(R.id.iv_no_data_found);
        ll_recyclerView_Card=findViewById(R.id.ll_recyclerView_Card);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CardActivity.this);
        recyclerView_Card.setLayoutManager(linearLayoutManager);
        cardProductModelArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        productCardListAdpater=new ProductCardListAdpater(CardActivity.this,cardProductModelArrayList,this);
        recyclerView_Card.setAdapter(productCardListAdpater);
        productCardListAdpater.notifyDataSetChanged();
        double total_rate = 0,total_saved=0,total_saved_mrp=0;

        if (cardProductModelArrayList.size()==0)
        {
            ll_cart_bottom.setVisibility(View.GONE);
            iv_no_data_found.setVisibility(View.VISIBLE);
            ll_recyclerView_Card.setVisibility(View.GONE);
        }

        for (int i=0;i<cardProductModelArrayList.size();i++)
        {
            double total_mrp=cardProductModelArrayList.get(i).getMrp_total();
            double total_price=cardProductModelArrayList.get(i).getPrice_total();
            total_rate=total_rate+total_price;
            total_saved=total_saved+total_mrp;
        }
        total_saved_mrp=total_saved-total_rate;
        textTotalAmt.setText("Rs "+String.format("%.2f",total_rate));
        textTotalSavedAmt.setText("Saved RS "+String.format("%.2f",total_saved_mrp));
    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardActivity.this,PaymentDetialsActivity.class));
            }
        });
    }


    @Override
    public void reference() {
        cardProductModelArrayList.clear();
        cardProductModelArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        double total_rate = 0,total_saved=0,total_saved_mrp=0;

        if (cardProductModelArrayList.size()==0)
        {
            ll_cart_bottom.setVisibility(View.GONE);
            iv_no_data_found.setVisibility(View.VISIBLE);
            ll_recyclerView_Card.setVisibility(View.GONE);
        }

        for (int i=0;i<cardProductModelArrayList.size();i++)
        {
            double total_mrp=cardProductModelArrayList.get(i).getMrp_total();
            double total_price=cardProductModelArrayList.get(i).getPrice_total();
            total_rate=total_rate+total_price;
            total_saved=total_saved+total_mrp;
        }
        total_saved_mrp=total_saved-total_rate;
        textTotalAmt.setText("Rs "+total_rate);
        textTotalSavedAmt.setText("Saved RS "+total_saved_mrp);
    }
}
