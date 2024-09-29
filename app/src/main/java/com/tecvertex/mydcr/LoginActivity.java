package com.tecvertex.mydcr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {
   EditText ecode,password;
   String c_id,webpath,empcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ecode=findViewById(R.id.etloginecode);
        password=findViewById(R.id.etloginpass);

        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        c_id = preff.getString("c_id", "");
        webpath = preff.getString("webpath", "");
        empcode=preff.getString("ecode", "");
        if(!empcode.equals(""))
        {
            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
        }
    }
    public void login(View v)
    {
        if(!ecode.getText().toString().equals("") && !password.getText().toString().equals("") ) {

            BackgroundProcess process = new BackgroundProcess(LoginActivity.this);
            process.execute("login",c_id,webpath, ecode.getText().toString(),password.getText().toString(), (Build.MANUFACTURER+"-"+Build.MODEL).toUpperCase(),BuildConfig.VERSION_NAME);
        }
        else
        {
            Toast.makeText(this, "Provide Complete Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    public void forgotpassword(View view) {
        if(!ecode.getText().toString().equals("")) {

            BackgroundProcess process = new BackgroundProcess(LoginActivity.this);
            process.execute("forgotpassword",c_id,webpath,ecode.getText().toString());
        }
        else
        {
            Toast.makeText(this, "Provide Employee Code", Toast.LENGTH_SHORT).show();
        }
    }
}