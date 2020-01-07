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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        find_View_ID();
        events();
    }

    private void find_View_ID() {
        appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"VgreenDB").allowMainThreadQueries().build();
        iv_back=findViewById(R.id.iv_back);
        recyclerView_Card=findViewById(R.id.recyclerView_Card);
        textTotalAmt=findViewById(R.id.textTotalAmt);
        btn_checkout=findViewById(R.id.btn_checkout);
        textTotalSavedAmt=findViewById(R.id.textTotalSavedAmt);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CardActivity.this);
        recyclerView_Card.setLayoutManager(linearLayoutManager);
        cardProductModelArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        productCardListAdpater=new ProductCardListAdpater(CardActivity.this,cardProductModelArrayList,this);
        recyclerView_Card.setAdapter(productCardListAdpater);
        productCardListAdpater.notifyDataSetChanged();
        long total_rate = 0,total_saved=0,total_saved_mrp=0;

        for (int i=0;i<cardProductModelArrayList.size();i++)
        {
            long total_mrp=cardProductModelArrayList.get(i).getMrp_total();
            long total_price=cardProductModelArrayList.get(i).getPrice_total();
            total_rate=total_rate+total_price;
            total_saved=total_saved+total_mrp;
        }
        total_saved_mrp=total_saved-total_rate;
        textTotalAmt.setText("Rs "+total_rate);
        textTotalSavedAmt.setText("Saved RS "+total_saved_mrp);
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
        long total_rate = 0,total_saved=0,total_saved_mrp=0;

        for (int i=0;i<cardProductModelArrayList.size();i++)
        {
            long total_mrp=cardProductModelArrayList.get(i).getMrp_total();
            long total_price=cardProductModelArrayList.get(i).getPrice_total();
            total_rate=total_rate+total_price;
            total_saved=total_saved+total_mrp;
        }
        total_saved_mrp=total_saved-total_rate;
        textTotalAmt.setText("Rs "+total_rate);
        textTotalSavedAmt.setText("Saved RS "+total_saved_mrp);
    }
}
