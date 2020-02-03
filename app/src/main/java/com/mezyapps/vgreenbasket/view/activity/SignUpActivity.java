package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.api_common.ApiClient;
import com.mezyapps.vgreenbasket.api_common.ApiInterface;
import com.mezyapps.vgreenbasket.model.LocationModel;
import com.mezyapps.vgreenbasket.model.RouteModel;
import com.mezyapps.vgreenbasket.model.SuccessModel;
import com.mezyapps.vgreenbasket.model.UserProfileModel;
import com.mezyapps.vgreenbasket.utils.ErrorDialog;
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;
import com.mezyapps.vgreenbasket.utils.ShowProgressDialog;
import com.mezyapps.vgreenbasket.utils.SuccessAppDialog;
import com.mezyapps.vgreenbasket.utils.SuccessDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ImageView iv_back;
    private AutoCompleteTextView textName, textAddress, textMobileNumber, textPassword;
    private Button btn_sign_up;
    private Spinner SpinnerLocation, SpinnerRoute;
    private String name, address, mobile, password;
    public static ApiInterface apiInterface;

    private String location_id = "", route_id = "";
    //Spinner Location
    private ArrayList<LocationModel> locationModelArrayList = new ArrayList<>();
    private ArrayList<String> location_string_arrayList = new ArrayList<>();
    //Spinner Location
    private ArrayList<RouteModel> routeModelArrayList = new ArrayList<>();
    private ArrayList<String> route_string_arrayList = new ArrayList<>();
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private ShowProgressDialog showProgressDialog;

    /*Spinner Array Adapter*/
    ArrayAdapter spinnerRouteArrayAdapter,spinnerLocationArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(SignUpActivity.this);
        iv_back = findViewById(R.id.iv_back);
        textName = findViewById(R.id.textName);
        textAddress = findViewById(R.id.textAddress);
        textMobileNumber = findViewById(R.id.textMobileNumber);
        textPassword = findViewById(R.id.textPassword);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        SpinnerLocation = findViewById(R.id.SpinnerLocation);
        SpinnerRoute = findViewById(R.id.SpinnerRoute);


        if (NetworkUtils.isNetworkAvailable(SignUpActivity.this)) {
            callLocationList();
        } else {
            NetworkUtils.isNetworkNotAvailable(SignUpActivity.this);
        }

    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (NetworkUtils.isNetworkAvailable(SignUpActivity.this)) {
                        callSignup();
                    } else {
                        NetworkUtils.isNetworkNotAvailable(SignUpActivity.this);
                    }
                }
            }
        });
        SpinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    int location_int = Integer.parseInt(locationModelArrayList.get(position).getId());
                    location_id = String.valueOf(location_int);

                    if (NetworkUtils.isNetworkAvailable(SignUpActivity.this)) {
                        callRouteList(location_id);
                    } else {
                        NetworkUtils.isNetworkNotAvailable(SignUpActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    int route_id_int = Integer.parseInt(routeModelArrayList.get(position).getRoute_id());
                    route_id = String.valueOf(route_id_int);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private boolean validation() {
        name = textName.getText().toString().trim();
        address = textAddress.getText().toString().trim();
        mobile = textMobileNumber.getText().toString().trim();
        password = textPassword.getText().toString().trim();

        if (name.equalsIgnoreCase("")) {
            textName.setError("Please Enter Name");
            textName.requestFocus();
            return false;
        } else if (address.equalsIgnoreCase("")) {
            textAddress.setError("Please Enter Address");
            textAddress.requestFocus();
            return false;
        } else if (mobile.equalsIgnoreCase("")) {
            textMobileNumber.setError("Please Enter Mobile Number");
            textMobileNumber.requestFocus();
            return false;
        } else if (mobile.length() < 10) {
            textMobileNumber.setError("Please Valid 10 Digit Mobile Number");
            textMobileNumber.requestFocus();
            return false;
        } else if (password.equalsIgnoreCase("")) {
            textPassword.setError("Please Enter Password");
            textPassword.requestFocus();
            return false;
        } else if (location_id.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Location", Toast.LENGTH_SHORT).show();
            return false;
        } else if (route_id.equalsIgnoreCase("")) {
            Toast.makeText(this, "Select Route", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void callLocationList() {
        Call<SuccessModel> call = apiInterface.locationList();
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        String message = null, code = null;
                        if (successModule != null) {
                            message = successModule.getMsg();
                            code = successModule.getCode();
                            locationModelArrayList.clear();
                            if (code.equalsIgnoreCase("1")) {
                                locationModelArrayList = successModule.getLocationModelArrayList();
                                if (locationModelArrayList.size() != 0) {
                                    location_string_arrayList.clear();
                                    for (LocationModel locationModel : locationModelArrayList) {
                                        location_string_arrayList.add(locationModel.getLocation_name());
                                    }
                                    spinnerLocationArrayAdapter=new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_spinner_item, location_string_arrayList);
                                    spinnerLocationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    SpinnerLocation.setAdapter(spinnerLocationArrayAdapter);
                                    spinnerLocationArrayAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    spinnerLocationArrayAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(SignUpActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(SignUpActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
            }
        });
    }

    private void callRouteList(String location_id) {
        Call<SuccessModel> call = apiInterface.routeList(location_id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        String message = null, code = null;
                        if (successModule != null) {
                            message = successModule.getMsg();
                            code = successModule.getCode();
                            route_string_arrayList.clear();
                            if (code.equalsIgnoreCase("1")) {
                                routeModelArrayList = successModule.getRouteModelArrayList();
                                if (routeModelArrayList.size() != 0) {
                                    route_string_arrayList.clear();
                                    for (RouteModel routeModel : routeModelArrayList) {
                                        route_string_arrayList.add(routeModel.getRoute_name());
                                    }
                                    spinnerRouteArrayAdapter = new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_spinner_item, route_string_arrayList);
                                    spinnerRouteArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    SpinnerRoute.setAdapter(spinnerRouteArrayAdapter);
                                    spinnerRouteArrayAdapter.notifyDataSetChanged();

                                } else {
                                    spinnerRouteArrayAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(SignUpActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(SignUpActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
            }
        });
    }

    private void callSignup() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.signUp(name, mobile, address, password, location_id, route_id);
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
                                userProfileModelArrayList = successModule.getUserProfileSignupModelArrayList();
                                if (userProfileModelArrayList.size() != 0) {
                                    String id = userProfileModelArrayList.get(0).getId();
                                    String name = userProfileModelArrayList.get(0).getName();
                                    String mobile = userProfileModelArrayList.get(0).getMobile_no();
                                    String address = userProfileModelArrayList.get(0).getAddress();
                                    String location = userProfileModelArrayList.get(0).getLocation_id();
                                    String route = userProfileModelArrayList.get(0).getRoute_id();

                                    SharedLoginUtils.putLoginSharedUtils(SignUpActivity.this);
                                    SharedLoginUtils.addUserId(SignUpActivity.this, id, name, mobile, address, location, route);
                                    SuccessDialog successDialog = new SuccessDialog(SignUpActivity.this);
                                    successDialog.showDialog("Your Registration Successfully");

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 2000);

                                }

                            } else {
                                ErrorDialog errorDialog = new ErrorDialog(SignUpActivity.this);
                                errorDialog.showDialog(message);
                            }


                        } else {
                            Toast.makeText(SignUpActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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
