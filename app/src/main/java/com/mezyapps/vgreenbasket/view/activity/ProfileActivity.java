package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;
import com.mezyapps.vgreenbasket.utils.ShowProgressDialog;
import com.mezyapps.vgreenbasket.utils.SuccessDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView iv_back, iv_no_data_found;
    private ScrollView scrollView;
    private AutoCompleteTextView textName, textAddress, textMobileNumber;
    private Spinner SpinnerLocation, SpinnerRoute;

    public static ApiInterface apiInterface;

    private String location_id = "", location = "", route_id = "", route = "", user_id;
    //Spinner Location
    private ArrayList<LocationModel> locationModelArrayList = new ArrayList<>();
    private ArrayList<String> location_string_arrayList = new ArrayList<>();
    //Spinner Location
    private ArrayList<RouteModel> routeModelArrayList = new ArrayList<>();
    private ArrayList<String> route_string_arrayList = new ArrayList<>();
    private Button btn_chang_profile;
    private String name, address, mobile;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();

    /*Spinner Array Adapter*/
    ArrayAdapter spinnerRouteArrayAdapter, spinnerLocationArrayAdapter;
    Boolean localtionTouch = false, routeTouch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        find_View_IDS();
        events();
    }

    private void find_View_IDS() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(ProfileActivity.this);
        iv_back = findViewById(R.id.iv_back);
        textName = findViewById(R.id.textName);
        textAddress = findViewById(R.id.textAddress);
        textMobileNumber = findViewById(R.id.textMobileNumber);
        SpinnerLocation = findViewById(R.id.SpinnerLocation);
        SpinnerRoute = findViewById(R.id.SpinnerRoute);
        btn_chang_profile = findViewById(R.id.btn_chang_profile);
        iv_no_data_found = findViewById(R.id.iv_no_data_found);
        scrollView = findViewById(R.id.scrollView);

    }

    private void events() {
        callUserInfo();

        if (NetworkUtils.isNetworkAvailable(ProfileActivity.this)) {
            callLocationList();
        } else {
            NetworkUtils.isNetworkNotAvailable(ProfileActivity.this);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SpinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    if (localtionTouch) {
                        int location_int = Integer.parseInt(locationModelArrayList.get(position).getId());
                        location_id = String.valueOf(location_int);
                        if (NetworkUtils.isNetworkAvailable(ProfileActivity.this)) {
                            callRouteList(location_id);
                        } else {
                            NetworkUtils.isNetworkNotAvailable(ProfileActivity.this);
                        }
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
                    if (routeTouch) {
                        int route_id_int = Integer.parseInt(routeModelArrayList.get(position).getRoute_id());
                        route_id = String.valueOf(route_id_int);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_chang_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (NetworkUtils.isNetworkAvailable(ProfileActivity.this)) {
                        callChangeProfile();
                    } else {
                        NetworkUtils.isNetworkNotAvailable(ProfileActivity.this);
                    }
                }
            }
        });
        SpinnerLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                localtionTouch = true;
                return false;
            }
        });
        SpinnerRoute.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                routeTouch = true;
                return false;
            }
        });
    }

    private void callUserInfo() {
        String name = SharedLoginUtils.getUserName(ProfileActivity.this);
        user_id = SharedLoginUtils.getUserId(ProfileActivity.this);
        String address = SharedLoginUtils.getUserAddress(ProfileActivity.this);
        String mobile_no = SharedLoginUtils.getUserMobile(ProfileActivity.this);
        location = SharedLoginUtils.getUserLocation(ProfileActivity.this);
        route = SharedLoginUtils.getUserRoute(ProfileActivity.this);

        textName.setText(name);
        textAddress.setText(address);
        textMobileNumber.setText(mobile_no);

        if (user_id.equalsIgnoreCase("")) {
            iv_no_data_found.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }

    }

    private void callChangeProfile() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.updateUserProfile(user_id, name, mobile, address, location_id, route_id);
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
                                userProfileModelArrayList = successModule.getUserProfileUpdateLoginModelArrayList();
                                if (userProfileModelArrayList.size() != 0) {
                                    String id = userProfileModelArrayList.get(0).getId();
                                    String name = userProfileModelArrayList.get(0).getName();
                                    String mobile = userProfileModelArrayList.get(0).getMobile_no();
                                    String address = userProfileModelArrayList.get(0).getAddress();
                                    String location = userProfileModelArrayList.get(0).getLocation_id();
                                    String route = userProfileModelArrayList.get(0).getRoute_id();

                                    SharedLoginUtils.putLoginSharedUtils(ProfileActivity.this);
                                    SharedLoginUtils.addUserId(ProfileActivity.this, id, name, mobile, address, location, route);
                                    Toast.makeText(ProfileActivity.this, "Your Profile Update Successfully", Toast.LENGTH_SHORT).show();
                                    callUserInfo();
                                }

                            } else {
                                Toast.makeText(ProfileActivity.this, "Your Profile Not Update", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(ProfileActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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

    private boolean validation() {
        name = textName.getText().toString().trim();
        address = textAddress.getText().toString().trim();
        mobile = textMobileNumber.getText().toString().trim();

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
                                    int index = locationModelArrayList.indexOf(location);
                                    for (LocationModel locationModel : locationModelArrayList) {
                                        location_string_arrayList.add(locationModel.getLocation_name());
                                    }
                                    spinnerLocationArrayAdapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_spinner_item, location_string_arrayList);
                                    spinnerLocationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    SpinnerLocation.setAdapter(spinnerLocationArrayAdapter);
                                    SpinnerLocation.setSelection(index);
                                    spinnerLocationArrayAdapter.notifyDataSetChanged();
                                } else {
                                    spinnerLocationArrayAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(ProfileActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ProfileActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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
                                    int index = locationModelArrayList.indexOf(route);
                                    for (RouteModel routeModel : routeModelArrayList) {
                                        route_string_arrayList.add(routeModel.getRoute_name());
                                    }
                                    spinnerRouteArrayAdapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_spinner_item, route_string_arrayList);
                                    spinnerRouteArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    SpinnerRoute.setAdapter(spinnerRouteArrayAdapter);
                                    SpinnerRoute.setSelection(index);
                                    spinnerRouteArrayAdapter.notifyDataSetChanged();

                                } else {
                                    spinnerRouteArrayAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(ProfileActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(ProfileActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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
}
