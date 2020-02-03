package com.mezyapps.vgreenbasket.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.api_common.ApiClient;
import com.mezyapps.vgreenbasket.api_common.ApiInterface;
import com.mezyapps.vgreenbasket.model.SuccessModel;
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;
import com.mezyapps.vgreenbasket.utils.ShowProgressDialog;
import com.mezyapps.vgreenbasket.view.activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {
    private Context mContext;
    private Button btn_update;
    private TextInputEditText edit_password, edit_confirm_password;
    private ShowProgressDialog showProgressDialog;
    private String user_id, password, confirm_password;
    public static ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        mContext = getActivity();
        find_View_IdS(view);
        events();
        return view;
    }

    private void find_View_IdS(View view) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        edit_password = view.findViewById(R.id.edit_password);
        btn_update = view.findViewById(R.id.btn_update);
        edit_confirm_password = view.findViewById(R.id.edit_confirm_password);
        showProgressDialog = new ShowProgressDialog(mContext);

        user_id = SharedLoginUtils.getUserId(mContext);
    }

    private void events() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (NetworkUtils.isNetworkAvailable(mContext)) {
                        callChangePassword();
                    } else {
                        NetworkUtils.isNetworkNotAvailable(mContext);
                    }
                }
            }
        });
    }

    private boolean validation() {
        password = edit_password.getText().toString().trim();
        confirm_password = edit_confirm_password.getText().toString().trim();

        if (password.equalsIgnoreCase("")) {
            edit_password.setError("Enter Password");
            edit_password.requestFocus();
            return false;
        } else if (confirm_password.equalsIgnoreCase("")) {
            edit_confirm_password.setError("Enter  Confirm Password");
            edit_confirm_password.requestFocus();
            return false;
        } else if (!password.equals(confirm_password)) {
            Toast.makeText(mContext, "Password And Confirm Password Not Match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void callChangePassword() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.chnagePassword(user_id, password);
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
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(mContext, "Password Change Successfully", Toast.LENGTH_SHORT).show();
                                ((MainActivity)mContext).logoutApplication();
                                edit_confirm_password.setText("");
                                edit_password.setText("");
                                edit_password.requestFocus();
                            }
                        } else {
                            Toast.makeText(mContext, "Password Not Change", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Response Null", Toast.LENGTH_SHORT).show();
                    }


                } catch (
                        Exception e) {
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
