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
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

public class DoctorCallActivity extends AppCompatActivity {
    String ecode,name,designation,hq,webpath,logo,c_id;
    TextView dcename,dchq,dcdesig,dcreportdate;
    ImageView dclogo;
    private int mYear,mMonth,mDay;
    public ArrayList<String> doctorlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_call);

        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        ecode = preff.getString("ecode", "");
        name = preff.getString("name", "");
        designation=preff.getString("designation", "");
        hq=preff.getString("hq", "");
        webpath=preff.getString("webpath", "");
        logo=preff.getString("logo", "");
        c_id=preff.getString("c_id", "");

        dcename=findViewById(R.id.dcename);
        dchq=findViewById(R.id.dchq);
        dcdesig=findViewById(R.id.dcdesig);
        dclogo=findViewById(R.id.dclogo);
        dcreportdate=findViewById(R.id.dcreportdate);

        dcename.setText(Html.fromHtml("E.Name: <b>"+name+"</b>"));
        dchq.setText(Html.fromHtml("HQ: <b>"+hq+"</b>"));
        dcdesig.setText(Html.fromHtml("Desig: <b>"+designation+"</b>"));
        Glide.with(DoctorCallActivity.this)
                .load("https://mydcr.victriz.com/admin/uploads/logo/"+logo)
                .centerInside()
                .into(dclogo);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dcreportdate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);


        BackgroundProcess process=new BackgroundProcess(DoctorCallActivity.this);
        process.execute("dcfetchdoctorlist",webpath,c_id,ecode,dcreportdate.getText().toString());


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BackgroundProcess process=new BackgroundProcess(DoctorCallActivity.this);
        process.execute("dcfetchdoctorlist",webpath,c_id,ecode,dcreportdate.getText().toString());
    }

    public void loadTable() {
        String labels[]={"SKU Code","SKU Name","PTR","Qty","Total","Action"};
        TableRow row=null;
        TextView tview;
        ImageView iview;

        TableLayout tl=findViewById(R.id.dctable);
        removeRows();
        //Toast.makeText(this, String.valueOf(tl.getChildCount()), Toast.LENGTH_SHORT).show();
        try {
            for(int x=0;x<doctorlist.size();x++)
            {
                row = new TableRow(this);

                String rowval[]=doctorlist.get(x).split(":");


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
                        String id=doctorlist.get(index).split(":")[0];
                        Intent i=new Intent(DoctorCallActivity.this,CompleteCallActivity.class);
                        i.putExtra("id",id);
                        i.putExtra("dname",doctorlist.get(index).split(":")[1]);
                        i.putExtra("speciality",doctorlist.get(index).split(":")[2]);
                        i.putExtra("reportdate",dcreportdate.getText().toString());
                        startActivity(i);
                    }
                });
                row.addView(iview);
                tl.addView(row,new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            /*total=Math.round(total * 100.0) / 100.0;
            eodtotal.setText(Html.fromHtml("Total: <font color='#006E00'>"+total+"</font>"));*/
        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }

    }
    private void removeRows() {
        TableLayout tl=findViewById(R.id.dctable);
        if(tl.getChildCount()>1)
        {
            tl.removeViews(1,tl.getChildCount()-1);
        }
    }

    public void dcfetchreportdate(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dcreportdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        BackgroundProcess process=new BackgroundProcess(DoctorCallActivity.this);
                        process.execute("dcfetchdoctorlist",webpath,c_id,ecode,dcreportdate.getText().toString());
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}