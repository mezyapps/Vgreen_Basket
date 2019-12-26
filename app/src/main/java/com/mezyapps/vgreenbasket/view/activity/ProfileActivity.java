package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView iv_back;
    private AutoCompleteTextView textName,textAddress,textMobileNumber;
    private Spinner SpinnerLocation,SpinnerRoute;

    public static ApiInterface apiInterface;

    private String location_id="",route_id="",user_id;
    //Spinner Location
    private ArrayList<LocationModel> locationModelArrayList = new ArrayList<>();
    private ArrayList<String> location_string_arrayList = new ArrayList<>();
    //Spinner Location
    private ArrayList<RouteModel> routeModelArrayList = new ArrayList<>();
    private ArrayList<String> route_string_arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        find_View_IDS();
        events();
    }

    private void find_View_IDS() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        iv_back=findViewById(R.id.iv_back);
        textName=findViewById(R.id.textName);
        textAddress=findViewById(R.id.textAddress);
        textMobileNumber=findViewById(R.id.textMobileNumber);
        SpinnerLocation=findViewById(R.id.SpinnerLocation);
        SpinnerRoute=findViewById(R.id.SpinnerRoute);

        String name= SharedLoginUtils.getUserName(ProfileActivity.this);
        String user_id= SharedLoginUtils.getUserId(ProfileActivity.this);
        String address=SharedLoginUtils.getUserAddress(ProfileActivity.this);
        String mobile_no=SharedLoginUtils.getUserMobile(ProfileActivity.this);

        textName.setText(name);
        textAddress.setText(address);
        textMobileNumber.setText(mobile_no);

        if (NetworkUtils.isNetworkAvailable(ProfileActivity.this)) {
            callLocationList();
        } else {
            NetworkUtils.isNetworkNotAvailable(ProfileActivity.this);
        }
    }

    private void events() {
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
                    int location_int= Integer.parseInt(locationModelArrayList.get(position).getId());
                    location_id = String.valueOf(location_int);

                    if (NetworkUtils.isNetworkAvailable(ProfileActivity.this)) {
                        callRouteList(location_id);
                    } else {
                        NetworkUtils.isNetworkNotAvailable(ProfileActivity.this);
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
                    int route_id_int= Integer.parseInt(routeModelArrayList.get(position).getRoute_id());
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
                                    ArrayAdapter arrayAdapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_spinner_item, location_string_arrayList);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    SpinnerLocation.setAdapter(arrayAdapter);
                                    arrayAdapter.notifyDataSetChanged();

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
                                    for (RouteModel routeModel : routeModelArrayList) {
                                        route_string_arrayList.add(routeModel.getRoute_name());
                                    }
                                    ArrayAdapter arrayAdapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_spinner_item, route_string_arrayList);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    SpinnerRoute.setAdapter(arrayAdapter);
                                    arrayAdapter.notifyDataSetChanged();

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
