package com.tecvertex.mydcr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class PlanningActivity extends AppCompatActivity {
    TextView planename,planhq,plandesig;
    ImageView planlogo;
    String ecode,name,designation,hq,webpath,logo,c_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);

        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        ecode = preff.getString("ecode", "");
        name = preff.getString("name", "");
        designation=preff.getString("designation", "");
        hq=preff.getString("hq", "");
        webpath=preff.getString("webpath", "");
        logo=preff.getString("logo", "");
        c_id=preff.getString("c_id", "");

        planename=findViewById(R.id.planningename);
        planhq=findViewById(R.id.planninghq);
        plandesig=findViewById(R.id.planningdesig);
        planlogo=findViewById(R.id.planninglogo);

        planename.setText(Html.fromHtml("E.Name: <b>"+name+"</b>"));
        planhq.setText(Html.fromHtml("HQ: <b>"+hq+"</b>"));
        plandesig.setText(Html.fromHtml("Desig: <b>"+designation+"</b>"));
        Glide.with(PlanningActivity.this)
                .load("https://mydcr.victriz.com/admin/uploads/logo/"+logo)
                .centerInside()
                .into(planlogo);
    }

    @Override
    public void onBackPressed() {

    }

    public void planback(View view) {
        finish();
    }

    public void tpschedule(View view) {

        startActivity(new Intent(PlanningActivity.this,TpscheduleActivity.class));
    }

    public void activities(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","activities.php");
        i.putExtra("activityname","activities");
        startActivity(i);
    }

    public void inputs(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","inputs.php");
        i.putExtra("activityname","input/sample");
        startActivity(i);
    }

    public void drlist(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","drlist.php");
        i.putExtra("activityname","update records : doctor list");
        startActivity(i);
    }

    public void chemlist(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","chemlist.php");
        i.putExtra("activityname","update records : chemist list");
        startActivity(i);
    }

    public void mtp(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","mtp.php");
        i.putExtra("activityname","master tour plan");
        startActivity(i);
    }

    public void rxanalysis(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","rxanalysis.php");
        i.putExtra("activityname","rx analysis");
        startActivity(i);
    }

    public void competitors(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","competitors.php");
        i.putExtra("activityname","competitors");
        startActivity(i);
    }

    public void sharehq(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","sharehq.php");
        i.putExtra("activityname","share hq");
        startActivity(i);
    }

    public void drsurvey(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","drsurvey.php");
        i.putExtra("activityname","doctor survey");
        startActivity(i);
    }

    public void chemsurvey(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","chemsurvey.php");
        i.putExtra("activityname","chemist survey");
        startActivity(i);
    }

    public void surveyfeedback(View view) {
        Intent i=new Intent(PlanningActivity.this,WebActivity.class);
        i.putExtra("page","surveyfeedback.php");
        i.putExtra("activityname","feedback");
        startActivity(i);
    }
}