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
import android.widget.EditText;
import android.widget.ImageView;
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

public class FruitActivity extends AppCompatActivity  implements ReferenceCardUiInterface {
    private ImageView  iv_back,iv_close,iv_search,iv_back_search,iv_basket;
    private RecyclerView recyclerView_Fruit;
    public static ApiInterface apiInterface;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<ProductListModel> productListModelArrayList=new ArrayList<>();
    private ProductListAdapter productListAdapter;
    private RelativeLayout rr_toolbar,rr_toolbar_search;
    private EditText edit_search;
    private TextView textCardCnt;
    private RelativeLayout rr_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        find_View_IDS();
        events();
    }

    private void find_View_IDS() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(FruitActivity.this);
        iv_back=findViewById(R.id.iv_back);
        iv_close=findViewById(R.id.iv_close);
        iv_search=findViewById(R.id.iv_search);
        edit_search=findViewById(R.id.edit_search);
        rr_toolbar = findViewById(R.id.rr_toolbar);
        iv_back_search = findViewById(R.id.iv_back_search);
        rr_toolbar_search = findViewById(R.id.rr_toolbar_search);
        recyclerView_Fruit=findViewById(R.id.recyclerView_Fruit);
        iv_basket=findViewById(R.id.iv_basket);
        textCardCnt=findViewById(R.id.textCardCnt);
        rr_cart = findViewById(R.id.rr_cart);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(FruitActivity.this);
        recyclerView_Fruit.setLayoutManager(linearLayoutManager);

        if (NetworkUtils.isNetworkAvailable(FruitActivity.this)) {
            callProductList();
        } else {
            NetworkUtils.isNetworkNotAvailable(FruitActivity.this);
        }

    }

    private void events() {
        int size=cartCount();
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
                if(productListModelArrayList.size()!=0) {
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
                startActivity(new Intent(FruitActivity.this,CardActivity.class));
            }
        });
    }

    private void callProductList() {
        String prod_id="1";
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
                        String message = null, code = null,folder=null;
                        if (successModule != null) {
                            code = successModule.getCode();
                            folder=successModule.getFolder();
                            if (code.equalsIgnoreCase("1")) {

                                productListModelArrayList=successModule.getProductListModelArrayList();
                                if(productListModelArrayList.size()!=0) {
                                    Collections.reverse(productListModelArrayList);
                                    productListAdapter=new ProductListAdapter(FruitActivity.this,productListModelArrayList,folder,FruitActivity.this);
                                    recyclerView_Fruit.setAdapter(productListAdapter);
                                    productListAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    // text_view_empty.setVisibility(View.VISIBLE);
                                    productListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                // text_view_empty.setVisibility(View.VISIBLE);
                                productListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(FruitActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                showProgressDialog.dismissDialog();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callProductList();

        int size=cartCount();
        textCardCnt.setText(String.valueOf(size));
    }
    public int cartCount() {
        ArrayList<CardProductModel> cardProductModelArrayList=new ArrayList<>();
        AppDatabase appDatabase;
        appDatabase= AppDatabase.getInStatce(FruitActivity.this);
        cardProductModelArrayList.clear();
        cardProductModelArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        int size=cardProductModelArrayList.size();
        return size;
    }

    @Override
    public void reference() {
        int size=cartCount();
        textCardCnt.setText(String.valueOf(size));
    }
}
