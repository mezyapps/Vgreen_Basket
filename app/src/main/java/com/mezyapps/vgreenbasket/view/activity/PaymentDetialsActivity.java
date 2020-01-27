package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.adapter.ProductListAdapter;
import com.mezyapps.vgreenbasket.api_common.ApiClient;
import com.mezyapps.vgreenbasket.api_common.ApiInterface;
import com.mezyapps.vgreenbasket.db.AppDatabase;
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.model.SuccessModel;
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;
import com.mezyapps.vgreenbasket.utils.ShowProgressDialog;
import com.mezyapps.vgreenbasket.utils.SuccessDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetialsActivity extends AppCompatActivity {
    private ImageView iv_back;
    private ArrayList<CardProductModel> cardProductModelArrayList = new ArrayList<>();
    private AppDatabase appDatabase;
    private TextView textTotalAmt, textTotalSavedAmt, textName, textMobileNumber, textTotalMrp, textAddress;
    private String name, mobile_no, user_id, payment_type, address, isCheckAddress = "no", other_address;
    private LinearLayout ll_login_sign_up, ll_user_details, ll_login, ll_sign_up;
    private Button btn_place_order;
    private RadioButton radioCashOnDelivery;
    private RadioGroup rbg_delivery_address;
    private ShowProgressDialog showProgressDialog;
    public static ApiInterface apiInterface;
    private EditText edt_other_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detials);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        showProgressDialog = new ShowProgressDialog(PaymentDetialsActivity.this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
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
        radioCashOnDelivery = findViewById(R.id.radioCashOnDelivery);
        textAddress = findViewById(R.id.textAddress);
        rbg_delivery_address = findViewById(R.id.rbg_delivery_address);
        edt_other_address = findViewById(R.id.edt_other_address);

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

        payment_type = radioCashOnDelivery.getText().toString();

        name = SharedLoginUtils.getUserName(PaymentDetialsActivity.this);
        mobile_no = SharedLoginUtils.getUserMobile(PaymentDetialsActivity.this);
        user_id = SharedLoginUtils.getUserId(PaymentDetialsActivity.this);
        address = SharedLoginUtils.getUserAddress(PaymentDetialsActivity.this);

        if (!name.equalsIgnoreCase("") && !mobile_no.equalsIgnoreCase("")) {
            String name1 = "Name : " + name, mobile_no1 = "Mobile No : " + mobile_no, address1 = "Address : " + address;
            textName.setText(name1);
            textMobileNumber.setText(mobile_no1);
            textAddress.setText(address1);
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
                if (validation()) {
                    if (NetworkUtils.isNetworkAvailable(PaymentDetialsActivity.this)) {
                        callPlaceOrder();
                    } else {
                        NetworkUtils.isNetworkNotAvailable(PaymentDetialsActivity.this);
                    }

                }
            }
        });
        rbg_delivery_address.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSame_as_address) {
                    edt_other_address.setVisibility(View.GONE);
                    isCheckAddress = "no";
                } else if (checkedId == R.id.rbOther_address) {
                    edt_other_address.setVisibility(View.VISIBLE);
                    isCheckAddress = "yes";
                }
            }
        });
    }

    private void callPlaceOrder() {
        showProgressDialog.showDialog();
        JSONArray jsonArrayProductID = getProductID();
        JSONArray jsonArrayUnitID = getProductUnitID();
        JSONArray jsonArrayWeight = getProductWeightID();
        JSONArray jsonArrayTotalMRP = getProductTotalMRP();
        JSONArray jsonArrayTotalPrice = getProductTotalPrice();
        JSONArray total_qty = getProductTotalQty();

        Call<SuccessModel> call = apiInterface.callPlaceOrder(user_id, jsonArrayProductID, jsonArrayUnitID, jsonArrayWeight, jsonArrayTotalMRP, jsonArrayTotalPrice, total_qty, payment_type, isCheckAddress, other_address);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        String message = null, code = null, folder;
                        if (successModule != null) {
                            code = successModule.getCode();
                            folder = successModule.getFolder();
                            if (code.equalsIgnoreCase("1")) {
                                SuccessDialog successDialog = new SuccessDialog(PaymentDetialsActivity.this);
                                successDialog.showDialog("Order Place Successfully");
                                appDatabase.getProductDAO().deleteAllProduct();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(PaymentDetialsActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 2000);
                            } else {
                                Toast.makeText(PaymentDetialsActivity.this, "Order Not Place", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PaymentDetialsActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showProgressDialog.dismissDialog();
                }


            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                showProgressDialog.dismissDialog();
            }
        });
    }

    private boolean validation() {
        other_address = edt_other_address.getText().toString().trim();
        if (user_id == null || user_id.equalsIgnoreCase("")) {
            Toast.makeText(PaymentDetialsActivity.this, "Please Login Your Account", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isCheckAddress.equalsIgnoreCase("yes")) {
            if (other_address.equalsIgnoreCase("")) {
                textAddress.setError("please Enter Other Address");
                textAddress.requestFocus();
                return false;
            }
        }
        return true;
    }

    private JSONArray getProductID() {
        List<String> ppList = new ArrayList<>();
        for (int i = 0; i < cardProductModelArrayList.size(); i++) {
            ppList.add(String.valueOf(cardProductModelArrayList.get(i).getProduct_id()));
        }
        JSONArray ppJsonArray = new JSONArray(ppList);
        return ppJsonArray;
    }

    private JSONArray getProductUnitID() {
        List<String> ppList = new ArrayList<>();
        for (int i = 0; i < cardProductModelArrayList.size(); i++) {
            ppList.add(String.valueOf(cardProductModelArrayList.get(i).getUnit()));
        }
        JSONArray ppJsonArray = new JSONArray(ppList);
        return ppJsonArray;
    }

    private JSONArray getProductWeightID() {
        List<String> ppList = new ArrayList<>();
        for (int i = 0; i < cardProductModelArrayList.size(); i++) {
            ppList.add(String.valueOf(cardProductModelArrayList.get(i).getWeight_id()));
        }
        JSONArray ppJsonArray = new JSONArray(ppList);
        return ppJsonArray;
    }

    private JSONArray getProductTotalMRP() {
        List<String> ppList = new ArrayList<>();
        for (int i = 0; i < cardProductModelArrayList.size(); i++) {
            ppList.add(String.valueOf(cardProductModelArrayList.get(i).getMrp_total()));
        }
        JSONArray ppJsonArray = new JSONArray(ppList);
        return ppJsonArray;
    }

    private JSONArray getProductTotalPrice() {
        List<String> ppList = new ArrayList<>();
        for (int i = 0; i < cardProductModelArrayList.size(); i++) {
            ppList.add(String.valueOf(cardProductModelArrayList.get(i).getPrice_total()));
        }
        JSONArray ppJsonArray = new JSONArray(ppList);
        return ppJsonArray;
    }

    private JSONArray getProductTotalQty() {
        List<String> ppList = new ArrayList<>();
        for (int i = 0; i < cardProductModelArrayList.size(); i++) {
            ppList.add(String.valueOf(cardProductModelArrayList.get(i).getQty()));
        }
        JSONArray ppJsonArray = new JSONArray(ppList);
        return ppJsonArray;
    }
}
