package com.tecvertex.mydcr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CompleteChemistCallActivity extends AppCompatActivity {

    double totalordervalue=0.0;
    AlertDialog dialog;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int IMAGE_GALLERY = 2, IMAGE_CAPTURE = 1;
    boolean imageloaded, uploadedimage;
    String latitude = null, longitude = null, uploadaddress = null;
    boolean check = true;
    private Bitmap bitmapLogo;
    private Uri filePathLogo;
    String currentImagePath = null;
    String ecode, name, designation, hq, webpath, logo, c_id;
    TextView cccename, ccchq, cccdesig, cccreportdate, ccccname, cccroute,cccshowtotal;
    EditText cccchemfeedback;
    Spinner cccactivityspinner, cccresponsespinner;
    ImageView ccclogo, cccuploadiview;
    String id, cname, route, reportdate, curr_month;
    public boolean hasActivity;
    private boolean activityImageUploaded;
    public boolean[] selectedLanguage;
    public ArrayList<Integer> langList = new ArrayList<>();
    ArrayList<Double> order=null;
    public String[] langArray = null, data = null, activitydata = null;
    public ArrayList<String> productlist = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_chemist_call);

        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        ecode = preff.getString("ecode", "");
        name = preff.getString("name", "");
        designation = preff.getString("designation", "");
        hq = preff.getString("hq", "");
        webpath = preff.getString("webpath", "");
        logo = preff.getString("logo", "");
        c_id = preff.getString("c_id", "");

        cccename = findViewById(R.id.cccename);
        ccchq = findViewById(R.id.ccchq);
        cccdesig = findViewById(R.id.cccdesig);
        ccclogo = findViewById(R.id.ccclogo);
        cccreportdate = findViewById(R.id.cccreportdate);
        ccccname = findViewById(R.id.ccccname);
        cccroute = findViewById(R.id.cccroute);
        cccshowtotal=findViewById(R.id.cccshowtotal);

        cccactivityspinner = findViewById(R.id.cccactivityspinner);
        cccresponsespinner = findViewById(R.id.cccresponsespinner);
        cccuploadiview = findViewById(R.id.cccuploadiview);
        cccchemfeedback=findViewById(R.id.cccchemfeedback);
        cccresponsespinner.setEnabled(false);
        cccuploadiview.setEnabled(false);
        cccchemfeedback.setEnabled(false);
        cccactivityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)
                {
                    cccresponsespinner.setEnabled(true);
                    cccuploadiview.setEnabled(true);
                    cccchemfeedback.setEnabled(true);
                }
                else
                {
                    cccresponsespinner.setEnabled(false);
                    cccuploadiview.setEnabled(false);
                    cccchemfeedback.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cccename.setText(Html.fromHtml("E.Name: <b>" + name + "</b>"));
        ccchq.setText(Html.fromHtml("HQ: <b>" + hq + "</b>"));
        cccdesig.setText(Html.fromHtml("Desig: <b>" + designation + "</b>"));
        Glide.with(CompleteChemistCallActivity.this)
                .load("https://mydcr.victriz.com/admin/uploads/logo/" + logo)
                .centerInside()
                .into(ccclogo);
        id = getIntent().getStringExtra("id");
        cname = getIntent().getStringExtra("cname");
        route = getIntent().getStringExtra("route");
        reportdate = getIntent().getStringExtra("reportdate");
        cccreportdate.setText(reportdate);
        cccroute.setText(route);
        ccccname.setText(cname);

        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Yes", "No"});
        ad.setDropDownViewResource(
                android.R.layout
                        .select_dialog_singlechoice);
        cccresponsespinner.setAdapter(ad);

        curr_month = reportdate.substring(reportdate.indexOf("-") + 1, reportdate.lastIndexOf("-"));

        BackgroundProcess process = new BackgroundProcess(CompleteChemistCallActivity.this);
        process.execute("cccfetchproduct", webpath, c_id, curr_month);


    }

    public void cccshowDialog(View view) {
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteChemistCallActivity.this);

        // set title
        builder.setTitle("Select Product");

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
                        stringBuilder.append(", ");
                    }
                }

                //Load Table
                LoadTable();
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

                    //Load Table
                    LoadTable();
                }
            }
        });
        // show dialog
        builder.show();

    }

    private void LoadTable() {
        String labels[] = {"SKU Code", "SKU Name", "PTR", "Qty", "Total", "Action"};
        TableRow row = null;
        TextView tview;
        TextView etext;
        order=new ArrayList<>(langList.size());
        for(int i=0;i<langList.size();i++)
        {
            order.add(0.0);
        }

        TableLayout tl = findViewById(R.id.ccctable);
        removeRows();

        //Toast.makeText(this, String.valueOf(tl.getChildCount()), Toast.LENGTH_SHORT).show();
        try {
            for (int x = 0; x < langList.size(); x++) {
                row = new TableRow(this);

                String rowval[] = productlist.get(langList.get(x)).split(":");


                for (int y = 0; y < rowval.length-2; y++) {

                    tview = new TextView(this);
                    tview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                    if (y == 1) {
                        tview.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                    } else if (y == 4) {

                        tview.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    } else {
                        tview.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    }

                    if (y != 0) {
                        tview.setBackgroundResource(R.drawable.cell_style_norm);
                        tview.setPadding(5, 5, 5, 5);
                        String data = rowval[y];
                        tview.setText(data);
                    } else {
                        tview.setBackgroundResource(R.drawable.cell_style_norm);
                        tview.setPadding(5, 5, 5, 5);
                        String data = String.valueOf(x + 1);
                        tview.setText(data);
                    }

                    row.addView(tview);
                }
                etext=new TextView(this);
                etext.setId(x);
                etext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                etext.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                etext.setInputType(InputType.TYPE_CLASS_NUMBER);
                etext.setBackgroundResource(R.drawable.edittext_background);
                etext.setPadding(5, 5, 5, 5);
                etext.setText("0");

                etext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(CompleteChemistCallActivity.this)
                                .setTitle("Order Quantity");
                        LayoutInflater inflater = getLayoutInflater();
                        View v1 = inflater.inflate(R.layout.layout_sample, null);  // this line
                        dialog1.setView(v1);
                        dialog1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText sample = v1.findViewById(R.id.layoutedit);
                                        int sampleqty = Integer.parseInt(sample.getText().toString());
                                        ((TextView)v).setText(String.valueOf(sampleqty));
                                        order.set(v.getId(),Double.parseDouble(String.valueOf(sampleqty)));
                                        sumordervalue();
                                        }
                                })
                                .setNegativeButton("Cancel", null);
                        AlertDialog alertDialog=dialog1.create();

                        alertDialog.show();
                    }
                });
                row.addView(etext);
                tl.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            /*total=Math.round(total * 100.0) / 100.0;
            eodtotal.setText(Html.fromHtml("Total: <font color='#006E00'>"+total+"</font>"));*/
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }

    private void sumordervalue() {
        totalordervalue=0.0;
        for(int x=0;x<langList.size();x++)
        {
            totalordervalue=totalordervalue+(order.get(x)*Double.parseDouble(productlist.get(langList.get(x)).split(":")[4]));
        }
        totalordervalue=Math.round(totalordervalue);
        cccshowtotal.setText(Html.fromHtml("TOTAL ORDER VALUE:  <font color='#055CB4'><b>"+totalordervalue+"</b></font>"));

    }

    private void removeRows() {
        TableLayout tl = findViewById(R.id.ccctable);
        if (tl.getChildCount() > 1) {
            tl.removeViews(1, tl.getChildCount() - 1);
        }
    }

    public void completechemcall(View view) {
        String activity, activityimage, activitypositiveresponse, activityfeedback, chemist_id, p_id, input, sampleqty;
        chemist_id = id;
        if (hasActivity && !cccactivityspinner.getSelectedItem().toString().equals("No Activity")) {
            if (uploadedimage) {
                fetchLatLongAddress();
                activity = cccactivityspinner.getSelectedItem().toString();
                activityimage = "";
                activitypositiveresponse = cccresponsespinner.getSelectedItem().toString();
                activityfeedback = cccchemfeedback.getText().toString();
                if (activity.equals("") || activitypositiveresponse.equals("") || activityfeedback.equals("")) {
                    Toast.makeText(this, "Complete the Activity Section with Feedback", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Upload Activity Image First", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            fetchLatLongAddress();
            activity = "";
            activityimage = "";
            activitypositiveresponse = "";
            activityfeedback = "";
        }
        if (langList.size() == 0) {
            p_id = "";
            input = "";
            sampleqty = "";
        } else {
            StringBuilder p = new StringBuilder();
            StringBuilder in = new StringBuilder();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < langList.size(); i++) {
                int x = langList.get(i);
                p.append(data[x].split(":")[0]);
                in.append(data[x].split(":")[2]);

                s.append(((TextView)findViewById(i)).getText().toString());
                if (i != langList.size() - 1) {
                    p.append(",");
                    in.append(",");
                    s.append(",");
                }
            }
            p_id = p.toString();
            input = in.toString();
            sampleqty = s.toString();
        }
        if (latitude != null && longitude != null) {
            BackgroundProcess process = new BackgroundProcess(CompleteChemistCallActivity.this);
            process.execute("completechemcall", webpath, c_id, ecode, chemist_id, p_id, input, sampleqty,String.valueOf(totalordervalue), activity, activitypositiveresponse, activityfeedback, reportdate, latitude, longitude, uploadaddress);
            //Final code
        }
    }

    public void loadActivity() {
        BackgroundProcess process = new BackgroundProcess(CompleteChemistCallActivity.this);
        process.execute("cccfetchactivity", webpath, c_id, curr_month);
    }

    public void loadActivitySpinner() {
        if (activitydata.length > 0) {
            ArrayList<String> activitylist = new ArrayList<>(activitydata.length+1);
            activitylist.add("No Activity");
            for (String val : activitydata) {
                activitylist.add(val.split(":")[1] + " [" + val.split(":")[2] + "]");
            }
            ArrayAdapter ad
                    = new ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    activitylist);
            ad.setDropDownViewResource(
                    android.R.layout
                            .select_dialog_singlechoice);
            cccactivityspinner.setAdapter(ad);
        } else {
            //Disable Activity Section
        }
    }
    public void loadgallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Photo"),IMAGE_GALLERY);
    }
    public void askload(View view)
    {
        dialog=new AlertDialog.Builder(CompleteChemistCallActivity.this)
                .setTitle("Confirmation")
                .setMessage("Choose Upload Source")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadgallery();
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadcamera();
                    }
                })

                .setCancelable(false).create();

        dialog.show();
        Button b = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        if(b != null) {
            b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background));
        }
        b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if(b != null) {
            b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background));
        }
    }
    public void loadcamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = getImageFile();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageFile != null) {
                filePathLogo = FileProvider.getUriForFile(this, "com.tecvertex.android.fileprovider", imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePathLogo);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE);

            }
        }
    }

    private File getImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "MyDCR_" + timestamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageloaded = true;
            filePathLogo = data.getData();
            try {
                bitmapLogo = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathLogo);
                cccuploadiview.setImageBitmap(bitmapLogo);
                uploadtoserver();
            } catch (IOException e) {
            }
        } else if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageloaded = true;

            try {
                bitmapLogo = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathLogo);
                cccuploadiview.setImageBitmap(bitmapLogo);
                uploadtoserver();
            } catch (IOException e) {
            }
        }
    }

    private void uploadtoserver() {
        if (imageloaded) {
            AlertDialog dialog = new AlertDialog.Builder(CompleteChemistCallActivity.this)
                    .setTitle("Confirmation")
                    .setMessage("Do You want to Upload the Captured Image to Server.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ImageUploadToServerFunction(bitmapLogo, "Activity", "Activity Image", ecode, id, reportdate);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })

                    .setCancelable(false).create();

            dialog.show();
            Button b = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            if (b != null) {
                b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background));
            }
            b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

            if (b != null) {
                b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background));
            }

        } else {
            Toast.makeText(this, "Load Activity Image", Toast.LENGTH_SHORT).show();
        }

    }

    public void ImageUploadToServerFunction(Bitmap bitmap, final String imageName, final String type, final String ecode, final String id, final String reportdate) {

        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        Toast.makeText(this, "Image File being Compressed", Toast.LENGTH_SHORT).show();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        Toast.makeText(this, "Converting Image Format", Toast.LENGTH_SHORT).show();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(CompleteChemistCallActivity.this, String.format("%s is Uploading", type), "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();
                AlertDialog display;
                display = new AlertDialog.Builder(CompleteChemistCallActivity.this)
                        .setCancelable(true)
                        .create();
                display.setTitle("Activity Image Upload Status!!");
                display.setMessage(string1);
                display.show();
                /*cdcuploadiview.setImageResource(R.drawable.ic_upload);*/
                if (string1.startsWith("Your")) {
                    uploadedimage = true;
                } else {
                    uploadedimage = false;
                }

            }

            @Override
            protected String doInBackground(Void... params) {
                //Toast.makeText(CompleteCallActivity.this, "Initiating Image Processing", Toast.LENGTH_SHORT).show();
                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put("image_name", imageName);

                HashMapParams.put("image_path", ConvertImage);

                HashMapParams.put("ecode", ecode);

                HashMapParams.put("chemist_id", id);

                HashMapParams.put("reportdate", reportdate);

                HashMapParams.put("type", type);

                String FinalData = imageProcessClass.ImageHttpRequest(webpath + "android/uploadchem.php", HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }


    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

    private void fetchLatLongAddress() {
        LocationManager lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gps_enabled) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(CompleteChemistCallActivity.this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {

                        try {
                            Geocoder geocoder = new Geocoder(CompleteChemistCallActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());

                            //tvlat.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude :</b><br></font>" + addresses.get(0).getLatitude()));
                            uploadaddress = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getAdminArea();
                            //Toast.makeText(CompleteCallActivity.this, uploadaddress+" ["+latitude+", "+longitude+"]", Toast.LENGTH_SHORT).show();


                        } catch (IOException e) {
                            latitude="";
                            longitude="";
                            uploadaddress="";
                            Log.e("MYDCR", e.getMessage());
                        }
                    }

                }
            });


        }
        else
        {
            showAlert();
        }
    }

    private void showAlert() {
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteChemistCallActivity.this);

        // set title
        builder.setTitle("GPS Alert");

        // set dialog non cancelable
        builder.setCancelable(false);

        builder.setMessage("Your GPS is Turned Off. Do you want to submit your report without location info.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                latitude="";
                longitude="";
                uploadaddress="";
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
            }
        });
        // show dialog
        builder.show();
    }
}