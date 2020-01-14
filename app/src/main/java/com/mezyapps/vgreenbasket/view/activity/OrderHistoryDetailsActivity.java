package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.adapter.OrderHistoryDTAdapter;
import com.mezyapps.vgreenbasket.adapter.ProductListAdapter;
import com.mezyapps.vgreenbasket.api_common.ApiClient;
import com.mezyapps.vgreenbasket.api_common.ApiInterface;
import com.mezyapps.vgreenbasket.model.OrderHistoryDTModel;
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

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    private OrderHistoryModel orderHistoryModel;
    private String order_no, order_no_str, order_date, name, mobile_no, total_amt, order_status;
    private ImageView iv_back;
    private TextView textOrderNo, textOrderDate, textName, textMobileNumber, textTotalAmt, textOrderStatus, textTotalItem,
            textOrderNoTitle;
    private RecyclerView recyclerView_product_list;
    private ShowProgressDialog showProgressDialog;
    public static ApiInterface apiInterface;
    private ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList = new ArrayList<>();
    private OrderHistoryDTAdapter orderHistoryDTAdapter;
    private RelativeLayout rr_recycle_view;
    int arrayList_size;
    boolean is_visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(OrderHistoryDetailsActivity.this);
        iv_back = findViewById(R.id.iv_back);
        textOrderNo = findViewById(R.id.textOrderNo);
        textOrderDate = findViewById(R.id.textOrderDate);
        textName = findViewById(R.id.textName);
        textMobileNumber = findViewById(R.id.textMobileNumber);
        textTotalAmt = findViewById(R.id.textTotalAmt);
        textOrderStatus = findViewById(R.id.textOrderStatus);
        recyclerView_product_list = findViewById(R.id.recyclerView_product_list);
        rr_recycle_view = findViewById(R.id.rr_recycle_view);
        textTotalItem = findViewById(R.id.textTotalItem);
        textOrderNoTitle = findViewById(R.id.textOrderNoTitle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderHistoryDetailsActivity.this);
        recyclerView_product_list.setLayoutManager(linearLayoutManager);

        Bundle bundle = getIntent().getExtras();
        orderHistoryModel = bundle.getParcelable("ORDER_HD");
        order_no_str = "Order  No : " + orderHistoryModel.getOrder_id();
        order_no = orderHistoryModel.getOrder_id();
        order_date = orderHistoryModel.getDate();
        total_amt = "Total AMT : " + orderHistoryModel.getTotal_price();
        order_status = "Order Status : " + orderHistoryModel.getStatus();
        name = "Name : " + SharedLoginUtils.getUserName(OrderHistoryDetailsActivity.this);
        mobile_no = "Mobile No : " + SharedLoginUtils.getUserMobile(OrderHistoryDetailsActivity.this);

        textOrderNo.setText(order_no_str);
        textOrderDate.setText(order_date);
        textOrderNoTitle.setText(order_no);
        textName.setText(name);
        textMobileNumber.setText(mobile_no);
        textTotalAmt.setText(total_amt);
        textOrderStatus.setText(order_status);
    }

    private void events() {
        if (NetworkUtils.isNetworkAvailable(OrderHistoryDetailsActivity.this)) {
            callOrderHistoryDT();
        } else {
            NetworkUtils.isNetworkNotAvailable(OrderHistoryDetailsActivity.this);
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textTotalItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_visible) {
                    rr_recycle_view.setVisibility(View.GONE);
                    is_visible = false;
                } else {
                    rr_recycle_view.setVisibility(View.VISIBLE);
                    is_visible = true;
                }
            }
        });
    }

    private void callOrderHistoryDT() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.orderHistoryDT(order_no);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        orderHistoryDTModelArrayList.clear();
                        String message = null, code = null, folder;
                        if (successModule != null) {
                            code = successModule.getCode();
                            folder = successModule.getFolder();
                            if (code.equalsIgnoreCase("1")) {

                                orderHistoryDTModelArrayList = successModule.getOrderHistoryDTModelArrayList();
                                if (orderHistoryDTModelArrayList.size() != 0) {
                                    arrayList_size = orderHistoryDTModelArrayList.size();
                                    textTotalItem.setVisibility(View.VISIBLE);
                                    textTotalItem.setText(String.valueOf(arrayList_size)+" "+"items");
                                    Collections.reverse(orderHistoryDTModelArrayList);
                                    orderHistoryDTAdapter = new OrderHistoryDTAdapter(OrderHistoryDetailsActivity.this, orderHistoryDTModelArrayList, folder);
                                    recyclerView_product_list.setAdapter(orderHistoryDTAdapter);
                                    orderHistoryDTAdapter.notifyDataSetChanged();
                                } else {
                                    orderHistoryDTAdapter.notifyDataSetChanged();
                                }
                            } else {
                                orderHistoryDTAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(OrderHistoryDetailsActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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
}
