package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.adapter.ProductCardListAdpater;
import com.mezyapps.vgreenbasket.db.AppDatabase;
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity {
    private ImageView iv_back;
    private RecyclerView recyclerView_Card;
    private AppDatabase appDatabase;
    private ArrayList<CardProductModel> cardProductModelArrayList = new ArrayList<>();
    private ProductCardListAdpater productCardListAdpater;

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
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CardActivity.this);
        recyclerView_Card.setLayoutManager(linearLayoutManager);
        cardProductModelArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        productCardListAdpater=new ProductCardListAdpater(CardActivity.this,cardProductModelArrayList);
        recyclerView_Card.setAdapter(productCardListAdpater);
        productCardListAdpater.notifyDataSetChanged();

    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
