package com.tecvertex.mydcr;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



public class Utilities {

    public static void showAlert(Context cxt, String type, String content)
    {
        Button btnDialog;
        Dialog customDialog;
        ImageView image;
        TextView text;
        //Initialize Custom Dialog Box
        customDialog = new Dialog(cxt);
        customDialog.setContentView(R.layout.layout_alert);
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button btnSubmit = customDialog.findViewById(R.id.dialogButtonOK);
        image=customDialog.findViewById(R.id.image);
        text=customDialog.findViewById(R.id.text);
        text.setText(content);
        if(type.equalsIgnoreCase("error"))
        {
            image.setImageResource(R.drawable.error);
        } else if (type.equalsIgnoreCase("warning")) {
            image.setImageResource(R.drawable.warning);
        } else if (type.equalsIgnoreCase("info")) {
            image.setImageResource(R.drawable.info);
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"You Click On Submit Button",Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            }
        });
       customDialog.show();
    }

}
