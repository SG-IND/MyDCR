package com.tecvertex.mydcr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.android.material.snackbar.Snackbar;

public class DashboardActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce;
    String ecode,name,designation,hq,webpath,logo,c_id;
    TextView dashename,dashhq,dashdesig;
    ImageView dashlogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        ecode = preff.getString("ecode", "");
        name = preff.getString("name", "");
        designation=preff.getString("designation", "");
        hq=preff.getString("hq", "");
        webpath=preff.getString("webpath", "");
        logo=preff.getString("logo", "");
        c_id=preff.getString("c_id", "");


        dashename=findViewById(R.id.dashename);
        dashhq=findViewById(R.id.dashhq);
        dashdesig=findViewById(R.id.dashdesig);
        dashlogo=findViewById(R.id.dashlogo);

        dashename.setText(Html.fromHtml("E.Name: <b>"+name+"</b>"));
        dashhq.setText(Html.fromHtml("HQ: <b>"+hq+"</b>"));
        dashdesig.setText(Html.fromHtml("Desig: <b>"+designation+"</b>"));
        Glide.with(DashboardActivity.this)
                .load("https://mydcr.victriz.com/admin/uploads/logo/"+logo)
                .centerInside()
                .into(dashlogo);
    }
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            System.exit(0);
        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar.make(findViewById(android.R.id.content),"Please click BACK again to exit", Snackbar.LENGTH_LONG).show();

        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    public void logout(View v)
    {

    }
    public void doccall(View v)
    {
        startActivity(new Intent(DashboardActivity.this,DoctorCallActivity.class));
    }
    public void chemcall(View v)
    {
        startActivity(new Intent(DashboardActivity.this,ChemistCallActivity.class));
    }
    public void planning(View v)
    {
        startActivity(new Intent(DashboardActivity.this,PlanningActivity.class));
    }

    public void hr(View view) {
        Intent i=new Intent(DashboardActivity.this,WebActivity.class);
        i.putExtra("page","hr.php");
        i.putExtra("activityname","hr");
        startActivity(i);
    }

    public void finance(View view) {
        Intent i=new Intent(DashboardActivity.this,WebActivity.class);
        i.putExtra("page","finance.php");
        i.putExtra("activityname","finance");
        startActivity(i);
    }

    public void training(View view) {
        Intent i=new Intent(DashboardActivity.this,WebActivity.class);
        i.putExtra("page","training.php");
        i.putExtra("activityname","training");
        startActivity(i);
    }


    public void sales(View view) {
        Intent i=new Intent(DashboardActivity.this,WebActivity.class);
        i.putExtra("page","sales.php");
        i.putExtra("activityname","sales");
        startActivity(i);
    }

    public void corpcomm(View view) {
        Intent i=new Intent(DashboardActivity.this,WebActivity.class);
        i.putExtra("page","corpcomm.php");
        i.putExtra("activityname","corp. comm.");
        startActivity(i);
    }

    public void profile(View view) {
        Intent i=new Intent(DashboardActivity.this,WebActivity.class);
        i.putExtra("page","profile.php");
        i.putExtra("activityname","profile");
        startActivity(i);
    }
}