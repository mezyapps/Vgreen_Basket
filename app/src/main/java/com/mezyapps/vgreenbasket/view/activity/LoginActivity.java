package com.mezyapps.vgreenbasket.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView textSignUp;
    private TextInputEditText edit_username,edit_password;
    private String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        find_View_Ids();
        events();

    }
    private void find_View_Ids() {
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
                    SharedLoginUtils.putLoginSharedUtils(LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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

}
