package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;

public class SignUpActivity extends AppCompatActivity {

    private ImageView iv_back;
    private AutoCompleteTextView textName,textAddress,textMobileNumber,textPassword;
    private Button btn_sign_up;
    private Spinner SpinnerLocation,SpinnerRoute;
    private String name,address,mobile,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        iv_back=findViewById(R.id.iv_back);
        textName=findViewById(R.id.textName);
        textAddress=findViewById(R.id.textAddress);
        textMobileNumber=findViewById(R.id.textMobileNumber);
        textPassword=findViewById(R.id.textPassword);
        btn_sign_up=findViewById(R.id.btn_sign_up);
        SpinnerLocation=findViewById(R.id.SpinnerLocation);
        SpinnerRoute=findViewById(R.id.SpinnerRoute);
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
                if(validation())
                {
                    SharedLoginUtils.putLoginSharedUtils(SignUpActivity.this);
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean validation() {
        name=textName.getText().toString().trim();
        address=textAddress.getText().toString().trim();
        mobile=textMobileNumber.getText().toString().trim();
        password=textPassword.getText().toString().trim();

        if(name.equalsIgnoreCase(""))
        {
            textName.setError("Please Enter Name");
            textName.requestFocus();
            return false;
        }else if(address.equalsIgnoreCase(""))
        {
            textAddress.setError("Please Enter Address");
            textAddress.requestFocus();
            return false;
        }else if(mobile.equalsIgnoreCase(""))
        {
            textMobileNumber.setError("Please Enter Mobile Number");
            textMobileNumber.requestFocus();
            return false;
        }else if (mobile.length()<10)
        {
            textMobileNumber.setError("Please Valid 10 Digit Mobile Number");
            textMobileNumber.requestFocus();
            return false;
        }else if (password.equalsIgnoreCase(""))
        {
            textPassword.setError("Please Enter Password");
            textPassword.requestFocus();
            return false;
        }
        return  true;
    }
}
