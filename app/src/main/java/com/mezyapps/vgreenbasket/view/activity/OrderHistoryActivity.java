package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.adapter.OrderHistoryAdapter;
import com.mezyapps.vgreenbasket.adapter.ProductListAdapter;
import com.mezyapps.vgreenbasket.api_common.ApiClient;
import com.mezyapps.vgreenbasket.api_common.ApiInterface;
import com.mezyapps.vgreenbasket.model.OrderHistoryModel;
import com.mezyapps.vgreenbasket.model.SuccessModel;
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;
import com.mezyapps.vgreenbasket.utils.ShowProgressDialog;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    private ImageView iv_back,iv_no_data_found;
    private RecyclerView recyclerView_order_history;
    private ArrayList<OrderHistoryModel> orderHistoryModelArrayList=new ArrayList<>();
    public static ApiInterface apiInterface;
    private ShowProgressDialog showProgressDialog;
    private String user_id;
    private OrderHistoryAdapter orderHistoryAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        find_View_IDS();
        events();
    }

    private void find_View_IDS() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog=new ShowProgressDialog(OrderHistoryActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_no_data_found = findViewById(R.id.iv_no_data_found);
        recyclerView_order_history = findViewById(R.id.recyclerView_order_history);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OrderHistoryActivity.this);
        recyclerView_order_history.setLayoutManager(linearLayoutManager);
        user_id= SharedLoginUtils.getUserId(OrderHistoryActivity.this);

        if (NetworkUtils.isNetworkAvailable(OrderHistoryActivity.this)) {
            callOrderHistory();
        } else {
            NetworkUtils.isNetworkNotAvailable(OrderHistoryActivity.this);
        }
    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void callOrderHistory() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.orderHistoryHD(user_id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        orderHistoryModelArrayList.clear();
                        String message = null, code = null;
                        if (successModule != null) {
                            code = successModule.getCode();
                            if (code.equalsIgnoreCase("1")) {
                                orderHistoryModelArrayList=successModule.getOrderHistoryModelArrayList();
                                if(orderHistoryModelArrayList.size()!=0) {
                                    Collections.reverse(orderHistoryModelArrayList);
                                    orderHistoryAdapter=new OrderHistoryAdapter(OrderHistoryActivity.this,orderHistoryModelArrayList);
                                    iv_no_data_found.setVisibility(View.GONE);
                                    recyclerView_order_history.setVisibility(View.VISIBLE);
                                    recyclerView_order_history.setAdapter(orderHistoryAdapter);
                                    orderHistoryAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    iv_no_data_found.setVisibility(View.VISIBLE);
                                    recyclerView_order_history.setVisibility(View.GONE);
                                    orderHistoryAdapter.notifyDataSetChanged();
                                }
                            } else {
                                iv_no_data_found.setVisibility(View.VISIBLE);
                                recyclerView_order_history.setVisibility(View.GONE);
                                orderHistoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(OrderHistoryActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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
        callOrderHistory();
    }
}
