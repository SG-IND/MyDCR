package com.tecvertex.mydcr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

public class ChemistCallActivity extends AppCompatActivity {
    String ecode,name,designation,hq,webpath,logo,c_id;
    TextView ccename,cchq,ccdesig,ccreportdate;
    ImageView cclogo;
    private int mYear,mMonth,mDay;
    public ArrayList<String> chemistlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemist_call);

        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        ecode = preff.getString("ecode", "");
        name = preff.getString("name", "");
        designation=preff.getString("designation", "");
        hq=preff.getString("hq", "");
        webpath=preff.getString("webpath", "");
        logo=preff.getString("logo", "");
        c_id=preff.getString("c_id", "");

        ccename=findViewById(R.id.ccename);
        cchq=findViewById(R.id.cchq);
        ccdesig=findViewById(R.id.ccdesig);
        cclogo=findViewById(R.id.cclogo);
        ccreportdate=findViewById(R.id.ccreportdate);

        ccename.setText(Html.fromHtml("E.Name: <b>"+name+"</b>"));
        cchq.setText(Html.fromHtml("HQ: <b>"+hq+"</b>"));
        ccdesig.setText(Html.fromHtml("Desig: <b>"+designation+"</b>"));
        Glide.with(ChemistCallActivity.this)
                .load("https://mydcr.victriz.com/admin/uploads/logo/"+logo)
                .centerInside()
                .into(cclogo);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        ccreportdate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);

        BackgroundProcess process=new BackgroundProcess(ChemistCallActivity.this);
        process.execute("ccfetchchemistlist",webpath,c_id,ecode,ccreportdate.getText().toString());
    }

    public void ccfetchreportdate(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        ccreportdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        BackgroundProcess process=new BackgroundProcess(ChemistCallActivity.this);
                        process.execute("ccfetchchemistlist",webpath,c_id,ecode,ccreportdate.getText().toString());
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void loadTable() {
        String labels[]={"SKU Code","SKU Name","PTR","Qty","Total","Action"};
        TableRow row=null;
        TextView tview;
        ImageView iview;

        TableLayout tl=findViewById(R.id.cctable);
        removeRows();
        //Toast.makeText(this, String.valueOf(tl.getChildCount()), Toast.LENGTH_SHORT).show();
        try {
            for(int x=0;x<chemistlist.size();x++)
            {
                row = new TableRow(this);

                String rowval[]=chemistlist.get(x).split(":");


                iview=new ImageView(this);
                for(int y=0;y<rowval.length;y++) {

                    tview=new TextView(this);
                    if(y==1)
                    {
                        tview.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                    }
                    else if(y==4)
                    {

                        tview.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    }
                    else {
                        tview.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    }

                    if(y!=0) {
                        tview.setBackgroundResource(R.drawable.cell_style_norm);
                        tview.setPadding(5, 5, 5, 5);
                        String data = rowval[y];
                        tview.setText(data);
                    }
                    else
                    {
                        tview.setBackgroundResource(R.drawable.cell_style_norm);
                        tview.setPadding(5, 5, 5, 5);
                        String data = String.valueOf(x+1);
                        tview.setText(data);
                    }
                    tview.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
                    row.addView(tview);
                }
                iview.setBackgroundResource(R.drawable.cell_style_norm);
                iview.setImageResource(R.drawable.ic_view);
                iview.setTag(x);
                iview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int index=(int)v.getTag();
                        String id=chemistlist.get(index).split(":")[0];
                        Intent i=new Intent(ChemistCallActivity.this,CompleteChemistCallActivity.class);
                        i.putExtra("id",id);
                        i.putExtra("cname",chemistlist.get(index).split(":")[1]);
                        i.putExtra("route",chemistlist.get(index).split(":")[3]);
                        i.putExtra("reportdate",ccreportdate.getText().toString());
                        startActivity(i);
                    }
                });
                row.addView(iview);
                tl.addView(row,new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            }

        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }
    }
    private void removeRows() {
        TableLayout tl=findViewById(R.id.cctable);
        if(tl.getChildCount()>1)
        {
            tl.removeViews(1,tl.getChildCount()-1);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BackgroundProcess process=new BackgroundProcess(ChemistCallActivity.this);
        process.execute("ccfetchchemistlist",webpath,c_id,ecode,ccreportdate.getText().toString());
    }
}