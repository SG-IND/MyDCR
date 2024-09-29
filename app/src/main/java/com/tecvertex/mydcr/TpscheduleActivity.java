package com.tecvertex.mydcr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class TpscheduleActivity extends AppCompatActivity {
    TextView tpename,tphq,tpdesig,tpselectroute,tpshowcount;
    public ArrayList<String> doctorlist,selecteddoctor;
    ImageView tplogo;
    String ecode,name,designation,hq,webpath,logo,c_id;
    private int mYear,mMonth,mDay;
    public boolean[] selectedLanguage;
    public ArrayList<Integer> langList = new ArrayList<>();
    public String[] langArray = null;
TextView tpvisitdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpschedule);



        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        ecode = preff.getString("ecode", "");
        name = preff.getString("name", "");
        designation=preff.getString("designation", "");
        hq=preff.getString("hq", "");
        webpath=preff.getString("webpath", "");
        logo=preff.getString("logo", "");
        c_id=preff.getString("c_id", "");

        tpename=findViewById(R.id.tpename);
        tphq=findViewById(R.id.tphq);
        tpdesig=findViewById(R.id.tpdesig);
        tplogo=findViewById(R.id.tplogo);
        tpvisitdate=findViewById(R.id.tpvisitdate);
        tpselectroute=findViewById(R.id.tpselectroute);
        tpshowcount=findViewById(R.id.tpshowcount);

        tpename.setText(Html.fromHtml("E.Name: <b>"+name+"</b>"));
        tphq.setText(Html.fromHtml("HQ: <b>"+hq+"</b>"));
        tpdesig.setText(Html.fromHtml("Desig: <b>"+designation+"</b>"));
        Glide.with(TpscheduleActivity.this)
                .load("https://mydcr.victriz.com/admin/uploads/logo/"+logo)
                .centerInside()
                .into(tplogo);



        BackgroundProcess process=new BackgroundProcess(TpscheduleActivity.this);
        process.execute("tpfetchroute",webpath,c_id,ecode);



    }

    public void tpfetchdate(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tpvisitdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public void setRouteEventDialog()
    {
        tpselectroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TpscheduleActivity.this);

                // set title
                builder.setTitle("Select Route");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(",");
                            }
                        }
                        // set text on textView
                        tpselectroute.setText(stringBuilder.toString());
                        fetchdoctorlist();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            tpselectroute.setText("");
                            fetchdoctorlist();
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });
    }

    private void fetchdoctorlist() {
        if(!tpselectroute.getText().toString().equals(""))
        {
            BackgroundProcess process=new BackgroundProcess(TpscheduleActivity.this);
            process.execute("tpfetchdoctorlist",webpath,c_id,ecode,tpselectroute.getText().toString().trim());
        }
    }


    public void loadTable() {
        String labels[]={"SKU Code","SKU Name","PTR","Qty","Total","Action"};
        TableRow row=null;
        TextView tview;
        CheckBox checkBox = null;

        TableLayout tl=findViewById(R.id.tptable);
        removeRows();
        //Toast.makeText(this, String.valueOf(tl.getChildCount()), Toast.LENGTH_SHORT).show();
        try {
            for(int x=0;x<doctorlist.size();x++)
            {
                row = new TableRow(this);

                String rowval[]=doctorlist.get(x).split(":");


                checkBox=new CheckBox(this);
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
               checkBox.setBackgroundResource(R.drawable.cell_style_norm);
               checkBox.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
               checkBox.setTag(x);
               checkBox.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       final int index=(int)v.getTag();
                       String id=doctorlist.get(index).split(":")[0];
                       if(((CheckBox)v).isChecked())
                       {
                           selecteddoctor.add(id);
                       }
                       else
                       {
                           selecteddoctor.remove(id);
                       }
                       tpshowcount.setText(Html.fromHtml("TOTAL DR PLANNED: <font color='#FC0300'><b>"+selecteddoctor.size()+"</b></font>"));
                   }
               });
                  row.addView(checkBox);
                tl.addView(row,new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            /*total=Math.round(total * 100.0) / 100.0;
            eodtotal.setText(Html.fromHtml("Total: <font color='#006E00'>"+total+"</font>"));*/
        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }

    }
    private void removeRows() {
        TableLayout tl=findViewById(R.id.tptable);
        if(tl.getChildCount()>1)
        {
            tl.removeViews(1,tl.getChildCount()-1);
        }
    }

    public void saveschedule(View view) {
        String visitdate,visitroutes,visitdoctors;
        if(tpvisitdate.getText().toString().equals("") || tpselectroute.getText().toString().equals("") || selecteddoctor.size()==0)
        {
            Toast.makeText(this, "Select/Provide Complete Information", Toast.LENGTH_SHORT).show();
        }
        else
        {
            BackgroundProcess process=new BackgroundProcess(this);
            visitdate=tpvisitdate.getText().toString().trim();
            visitroutes=tpselectroute.getText().toString().trim();
            visitdoctors=String.join(",",selecteddoctor);
            process.execute("saveschedule",webpath,c_id,ecode,visitdate,visitdoctors,visitroutes);
        }
    }
}
