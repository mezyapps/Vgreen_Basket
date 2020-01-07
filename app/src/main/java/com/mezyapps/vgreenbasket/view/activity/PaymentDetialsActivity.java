package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.db.AppDatabase;
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;

import java.util.ArrayList;

public class PaymentDetialsActivity extends AppCompatActivity {
    private ImageView iv_back;
    private ArrayList<CardProductModel> cardProductModelArrayList = new ArrayList<>();
    private AppDatabase appDatabase;
    private TextView textTotalAmt, textTotalSavedAmt, textName, textMobileNumber, textTotalMrp;
    private String name, mobile_no, user_id;
    private LinearLayout ll_login_sign_up, ll_user_details, ll_login, ll_sign_up;
    private Button btn_place_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detials);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "VgreenDB").allowMainThreadQueries().build();
        iv_back = findViewById(R.id.iv_back);
        textTotalAmt = findViewById(R.id.textTotalAmt);
        textTotalSavedAmt = findViewById(R.id.textTotalSavedAmt);
        textName = findViewById(R.id.textName);
        textMobileNumber = findViewById(R.id.textMobileNumber);
        ll_login_sign_up = findViewById(R.id.ll_login_sign_up);
        ll_user_details = findViewById(R.id.ll_user_details);
        ll_login = findViewById(R.id.ll_login);
        ll_sign_up = findViewById(R.id.ll_sign_up);
        btn_place_order = findViewById(R.id.btn_place_order);
        textTotalMrp = findViewById(R.id.textTotalMrp);

        cardProductModelArrayList.clear();
        cardProductModelArrayList.addAll(appDatabase.getProductDAO().getAppProduct());

        long total_rate = 0, total_saved = 0, total_saved_mrp = 0;

        for (int i = 0; i < cardProductModelArrayList.size(); i++) {
            long total_mrp = cardProductModelArrayList.get(i).getMrp_total();
            long total_price = cardProductModelArrayList.get(i).getPrice_total();
            total_rate = total_rate + total_price;
            total_saved = total_saved + total_mrp;
        }
        total_saved_mrp = total_saved - total_rate;
        String rate = "Amt To Pay Rs : " + total_rate;
        String total_save = "Saved Rs : " + total_saved_mrp;
        String total_mrp = "Total MRP : " + total_saved;
        textTotalMrp.setText(total_mrp);
        textTotalSavedAmt.setText(total_save);
        textTotalAmt.setText(rate);


        name = SharedLoginUtils.getUserName(PaymentDetialsActivity.this);
        mobile_no = SharedLoginUtils.getUserMobile(PaymentDetialsActivity.this);
        user_id = SharedLoginUtils.getUserId(PaymentDetialsActivity.this);

        if (!name.equalsIgnoreCase("") && !mobile_no.equalsIgnoreCase("")) {
            String name1 = "Name : " + name, mobile_no1 = "Mobile No : " + mobile_no;
            textName.setText(name1);
            textMobileNumber.setText(mobile_no1);
            ll_login_sign_up.setVisibility(View.GONE);
        } else {
            ll_login_sign_up.setVisibility(View.VISIBLE);
            ll_user_details.setVisibility(View.GONE);
        }
    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentDetialsActivity.this, LoginActivity.class));
            }
        });
        ll_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentDetialsActivity.this, SignUpActivity.class));
            }
        });
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentDetialsActivity.this, "Wait", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
