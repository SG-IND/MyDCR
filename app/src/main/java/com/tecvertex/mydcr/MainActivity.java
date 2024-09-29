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



public class MainActivity extends AppCompatActivity {
    EditText clientcode;
    String c_id,webpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        clientcode=findViewById(R.id.etclientcode);



        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        c_id = preff.getString("c_id", "");
        webpath = preff.getString("webpath", "");
        if(!c_id.equals(""))
        {
            String ecode=preff.getString("ecode", "");
            BackgroundProcess process = new BackgroundProcess(MainActivity.this);
            process.execute("loginout",c_id,webpath, ecode, (Build.MANUFACTURER+"-"+Build.MODEL).toUpperCase());
            //startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    }
    private void checkPermissions(){
        PermissionsUtil.askPermissions(this);
    }
    public void fetchCompanyDetails(View v)
    {
        if(!clientcode.getText().toString().equals("")) {
            BackgroundProcess process = new BackgroundProcess(MainActivity.this);
            process.execute("fetchCID", clientcode.getText().toString());
        }
        else
        {
            Toast.makeText(this, "Provide a Company Code", Toast.LENGTH_SHORT).show();
        }
    }
}