package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.api_common.ApiClient;
import com.mezyapps.vgreenbasket.api_common.ApiInterface;
import com.mezyapps.vgreenbasket.model.SuccessModel;
import com.mezyapps.vgreenbasket.model.UserProfileModel;
import com.mezyapps.vgreenbasket.utils.ErrorDialog;
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;
import com.mezyapps.vgreenbasket.utils.ShowProgressDialog;
import com.mezyapps.vgreenbasket.utils.SuccessDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView textSignUp;
    private TextInputEditText edit_username,edit_password;
    private String username,password;
    public static ApiInterface apiInterface;
    private ArrayList<UserProfileModel> userProfileModelArrayList=new ArrayList<>();
    private ShowProgressDialog showProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        find_View_Ids();
        events();

    }
    private void find_View_Ids() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog=new ShowProgressDialog(LoginActivity.this);
        btn_login=findViewById(R.id.btn_login);
        textSignUp=findViewById(R.id.textSignUp);
        edit_username=findViewById(R.id.edit_username);
        edit_password=findViewById(R.id.edit_password);
    }

    private void events() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()) {
                    if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
                        calllogin();
                    } else {
                        NetworkUtils.isNetworkNotAvailable(LoginActivity.this);
                    }
                }
            }
        });
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }

    private boolean validation() {
        username=edit_username.getText().toString().trim();
        password=edit_password.getText().toString().trim();
        if(username.equalsIgnoreCase(""))
        {
          edit_username.setError("Enter Username");
          edit_username.requestFocus();
          return false;
        }
        else if(username.length()<10)
        {
            edit_username.setError("Enter Valid 10 Digit Mobile Number ");
            edit_username.requestFocus();
            return false;
        }else if(password.equalsIgnoreCase(""))
        {

            edit_password.setError("Enter Password");
            edit_password.requestFocus();
            return false;
        }
        return  true;
    }

    private void calllogin() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.login(username,password);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        String message = null, code = null;
                        if (successModule != null) {
                            message = successModule.getMsg().toUpperCase();
                            code = successModule.getCode();
                            userProfileModelArrayList.clear();
                            if (code.equalsIgnoreCase("1")) {
                                userProfileModelArrayList = successModule.getUserProfileLoginModelArrayList();
                                if (userProfileModelArrayList.size() != 0) {
                                    String id=userProfileModelArrayList.get(0).getId();
                                    String name=userProfileModelArrayList.get(0).getName();
                                    String mobile=userProfileModelArrayList.get(0).getMobile_no();

                                    SharedLoginUtils.putLoginSharedUtils(LoginActivity.this);
                                    SharedLoginUtils.addUserId(LoginActivity.this,id,name,mobile);
                                    SuccessDialog successDialog=new SuccessDialog(LoginActivity.this);
                                    successDialog.showDialog("Login Successfully");

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 2000);

                                }

                            } else {
                                ErrorDialog errorDialog=new ErrorDialog(LoginActivity.this);
                                errorDialog.showDialog(message);
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                showProgressDialog.dismissDialog();
                t.printStackTrace();
            }
        });
    }

}
