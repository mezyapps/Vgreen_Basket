package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.adapter.ProductListAdapter;
import com.mezyapps.vgreenbasket.api_common.ApiClient;
import com.mezyapps.vgreenbasket.api_common.ApiInterface;
import com.mezyapps.vgreenbasket.db.AppDatabase;
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.model.ProductListModel;
import com.mezyapps.vgreenbasket.model.SuccessModel;
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.ReferenceCardUiInterface;
import com.mezyapps.vgreenbasket.utils.ShowProgressDialog;
import com.mezyapps.vgreenbasket.view.fragment.CardFragment;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VegetableActivity extends AppCompatActivity implements ReferenceCardUiInterface {

    private ImageView iv_back, iv_close, iv_search, iv_back_search, iv_basket, iv_no_data_found;
    private RecyclerView recyclerView_Vegetable;
    public static ApiInterface apiInterface;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<ProductListModel> productListModelArrayList = new ArrayList<>();
    private ProductListAdapter productListAdapter;
    private RelativeLayout rr_toolbar, rr_toolbar_search;
    private EditText edit_search;
    private TextView textCardCnt, textTotalAmt, textTotalSavedAmt;
    private RelativeLayout rr_cart;
    private LinearLayout ll_price_bottom;
    private Button btn_checkout;
    private AppDatabase appDatabase;
    ArrayList<CardProductModel> cardProductModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable);

        find_View_IDS();
        events();
    }

    private void find_View_IDS() {
        appDatabase = AppDatabase.getInStatce(VegetableActivity.this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(VegetableActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_close = findViewById(R.id.iv_close);
        iv_search = findViewById(R.id.iv_search);
        edit_search = findViewById(R.id.edit_search);
        rr_toolbar = findViewById(R.id.rr_toolbar);
        iv_back_search = findViewById(R.id.iv_back_search);
        iv_basket = findViewById(R.id.iv_basket);
        rr_toolbar_search = findViewById(R.id.rr_toolbar_search);
        textCardCnt = findViewById(R.id.textCardCnt);
        rr_cart = findViewById(R.id.rr_cart);
        iv_no_data_found = findViewById(R.id.iv_no_data_found);
        recyclerView_Vegetable = findViewById(R.id.recyclerView_Vegetable);
        ll_price_bottom = findViewById(R.id.ll_price_bottom);
        textTotalAmt = findViewById(R.id.textTotalAmt);
        textTotalSavedAmt = findViewById(R.id.textTotalSavedAmt);
        textTotalSavedAmt = findViewById(R.id.textTotalSavedAmt);
        btn_checkout = findViewById(R.id.btn_checkout);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VegetableActivity.this);
        recyclerView_Vegetable.setLayoutManager(linearLayoutManager);

        if (NetworkUtils.isNetworkAvailable(VegetableActivity.this)) {
            callProductList();
        } else {
            NetworkUtils.isNetworkNotAvailable(VegetableActivity.this);
        }
    }

    private void events() {
        int size = cartCount();
        textCardCnt.setText(String.valueOf(size));

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_back_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_toolbar.setVisibility(View.GONE);
                rr_toolbar_search.setVisibility(View.VISIBLE);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_toolbar_search.setVisibility(View.GONE);
                rr_toolbar.setVisibility(View.VISIBLE);
                edit_search.setText("");
            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (productListModelArrayList.size() != 0) {
                    productListAdapter.getFilter().filter(edit_search.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rr_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VegetableActivity.this, CardActivity.class));
            }
        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VegetableActivity.this, PaymentDetialsActivity.class));
            }
        });

    }

    private void callProductList() {
        String prod_id = "2";
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.productList(prod_id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        productListModelArrayList.clear();
                        String message = null, code = null, folder;
                        if (successModule != null) {
                            code = successModule.getCode();
                            folder = successModule.getFolder();
                            if (code.equalsIgnoreCase("1")) {

                                productListModelArrayList = successModule.getProductListModelArrayList();
                                if (productListModelArrayList.size() != 0) {
                                    iv_no_data_found.setVisibility(View.GONE);
                                    productListAdapter = new ProductListAdapter(VegetableActivity.this, productListModelArrayList, folder, VegetableActivity.this);
                                    recyclerView_Vegetable.setAdapter(productListAdapter);
                                    productListAdapter.notifyDataSetChanged();
                                    if (cardProductModelArrayList.size() != 0) {
                                        ll_price_bottom.setVisibility(View.VISIBLE);
                                    } else {
                                        ll_price_bottom.setVisibility(View.GONE);
                                    }
                                } else {
                                    iv_no_data_found.setVisibility(View.VISIBLE);
                                    productListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                iv_no_data_found.setVisibility(View.VISIBLE);
                                productListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(VegetableActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t3) {
                showProgressDialog.dismissDialog();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callProductList();
        int size = cartCount();
        textCardCnt.setText(String.valueOf(size));
    }

    public int cartCount() {
        cardProductModelArrayList.clear();

        cardProductModelArrayList.addAll(appDatabase.getProductDAO().getAppProduct());

        if (cardProductModelArrayList.size() != 0) {
            ll_price_bottom.setVisibility(View.VISIBLE);
        } else {
            ll_price_bottom.setVisibility(View.GONE);
        }

        double total_rate = 0, total_saved = 0, total_saved_mrp = 0;
        for (int i = 0; i < cardProductModelArrayList.size(); i++) {
            double total_mrp = cardProductModelArrayList.get(i).getMrp_total();
            double total_price = cardProductModelArrayList.get(i).getPrice_total();
            total_rate = total_rate + total_price;
            total_saved = total_saved + total_mrp;
        }
        total_saved_mrp = total_saved - total_rate;
        textTotalAmt.setText("Rs "+String.format("%.2f",total_rate));
        textTotalSavedAmt.setText("Saved RS "+String.format("%.2f",total_saved_mrp));

        int size = cardProductModelArrayList.size();
        return size;
    }

    @Override
    public void reference() {
        int size = cartCount();
        textCardCnt.setText(String.valueOf(size));
    }

}
