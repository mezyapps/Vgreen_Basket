package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    private OrderHistoryModel orderHistoryModel;
    private String order_no, user_id, order_date, name, total_amt, order_status, address, total_mrp,reason_order_cancel;
    private ImageView iv_back;
    private TextView textOrderDate, textName, textMobileNumber, textOrderStatus, textTotalItem,
            textOrderNoTitle, textDeliveryAddress, textOrderValue, textTotalAmtTopay, textTotalSavedAmt;
    private RecyclerView recyclerView_product_list;
    private ShowProgressDialog showProgressDialog;
    public static ApiInterface apiInterface;
    private ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList = new ArrayList<>();
    private OrderHistoryDTAdapter orderHistoryDTAdapter;
    private RelativeLayout rr_recycle_view;
    int arrayList_size;
    boolean is_visible = false;
    private Button btn_cancel_order;

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
        textOrderDate = findViewById(R.id.textOrderDate);
        textName = findViewById(R.id.textName);
        textOrderStatus = findViewById(R.id.textOrderStatus);
        recyclerView_product_list = findViewById(R.id.recyclerView_product_list);
        rr_recycle_view = findViewById(R.id.rr_recycle_view);
        textTotalItem = findViewById(R.id.textTotalItem);
        textOrderNoTitle = findViewById(R.id.textOrderNoTitle);
        textDeliveryAddress = findViewById(R.id.textDeliveryAddress);
        textOrderValue = findViewById(R.id.textOrderValue);
        textTotalAmtTopay = findViewById(R.id.textTotalAmtTopay);
        textTotalSavedAmt = findViewById(R.id.textTotalSavedAmt);
        btn_cancel_order = findViewById(R.id.btn_cancel_order);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderHistoryDetailsActivity.this);
        recyclerView_product_list.setLayoutManager(linearLayoutManager);

        Bundle bundle = getIntent().getExtras();
        orderHistoryModel = bundle.getParcelable("ORDER_HD");
        order_no = orderHistoryModel.getOrder_id();
        order_date = orderHistoryModel.getDate();
        total_amt = orderHistoryModel.getTotal_price();
        order_status = orderHistoryModel.getStatus();
        total_mrp = orderHistoryModel.getTotal_mrp();
        name = SharedLoginUtils.getUserName(OrderHistoryDetailsActivity.this);
        address = SharedLoginUtils.getUserAddress(OrderHistoryDetailsActivity.this);
        user_id = SharedLoginUtils.getUserId(OrderHistoryDetailsActivity.this);

        if(order_status.equalsIgnoreCase("cancelled") || order_status.equalsIgnoreCase("delivered"))
        {
            btn_cancel_order.setVisibility(View.GONE);
        }

  /*      SimpleDateFormat formatter1=new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date1=formatter1.parse(order_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
*/
        textOrderDate.setText(order_date);
        textOrderNoTitle.setText(order_no);
        textName.setText(name);
        textDeliveryAddress.setText(address);
        textOrderValue.setText(total_mrp);
        textTotalAmtTopay.setText(total_amt);
        int savedAmt = Integer.parseInt(total_mrp) - Integer.parseInt(total_amt);
        textTotalSavedAmt.setText(String.valueOf(savedAmt));
        //textTotalAmt.setText(total_amt);
        //textOrderStatus.setText(order_status);
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
        btn_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });
    }

    public void cancelDialog() {
        final Dialog cancel_dialog = new Dialog(OrderHistoryDetailsActivity.this);
        cancel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancel_dialog.setContentView(R.layout.dialog_cancel_order);
        final EditText edt_resign=cancel_dialog.findViewById(R.id.edt_resign);
        Button btn_yes=cancel_dialog.findViewById(R.id.btn_yes);
        Button btn_no=cancel_dialog.findViewById(R.id.btn_no);
        cancel_dialog.setCancelable(false);
        cancel_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        cancel_dialog.show();

        Window window = cancel_dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason_order_cancel=edt_resign.getText().toString();
                if(reason_order_cancel.equalsIgnoreCase(""))
                {
                    edt_resign.setError("Enter Reason Cancel Order");
                    edt_resign.requestFocus();
                }
                else {
                    cancel_dialog.dismiss();
                    edt_resign.clearFocus();
                    if (NetworkUtils.isNetworkAvailable(OrderHistoryDetailsActivity.this)) {
                        callCancelOrder();
                    } else {
                        NetworkUtils.isNetworkNotAvailable(OrderHistoryDetailsActivity.this);
                    }
                }
            }
        });
    }

    private void callCancelOrder() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.cancelOrder(order_no, user_id,reason_order_cancel);
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
                        String message = null, code = null;
                        if (successModule != null) {
                            code = successModule.getCode();
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(OrderHistoryDetailsActivity.this, "Your Order Canceled", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OrderHistoryDetailsActivity.this, OrderHistoryActivity.class));
                                finish();
                            } else {
                                Toast.makeText(OrderHistoryDetailsActivity.this, "Your Order Not Cancel", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(OrderHistoryDetailsActivity.this, "Your Order Not Cancel", Toast.LENGTH_SHORT).show();
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
                                    textTotalItem.setText(String.valueOf(arrayList_size) + " " + "items");
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
