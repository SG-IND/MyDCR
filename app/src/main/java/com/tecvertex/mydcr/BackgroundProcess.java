package com.tecvertex.mydcr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AlertDialog;

public class BackgroundProcess extends AsyncTask<String,Void,String> {
    private Context cxt;
    private String type,string_url;
    private AlertDialog display;
    private ProgressDialog progress;
    public BackgroundProcess(Context cxt)
    {
        this.cxt=cxt;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress=new ProgressDialog(cxt);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }
    @Override
    protected String doInBackground(String... params) {
        type=params[0];
        String result=null;
        switch (type)
        {
            case "fetchCID":{
                result=onFetchCompanyProcess(params[1]);
                break;
            }
            case "login":{
                result=onLoginProcess(params[1],params[2],params[3],params[4],params[5],params[6]);
                break;
            }
            case "loginout":{
                result=onLoginOutProcess(params[1],params[2],params[3],params[4]);
                break;
            }
            case "forgotpassword":{
                result=onForgotPassword(params[1],params[2],params[3]);
                break;
            }
            case "tpfetchroute":{

                result=onTpFetchRoute(params[1],params[2],params[3]);
                break;
            }
            case "tpfetchdoctorlist":{
                result=onTpFetchDoctorList(params[1],params[2],params[3],params[4]);
                break;
            }
            case "saveschedule":{
                result=onSaveSchedule(params[1],params[2],params[3],params[4],params[5],params[6]);
                break;
            }
            case "dcfetchdoctorlist":{
                result=onDcFetchDoctorList(params[1],params[2],params[3],params[4]);
                break;
            }
            case "ccfetchchemistlist":{
                result=onCcFetchChemistList(params[1],params[2],params[3],params[4]);
                break;
            }
            case "cdcfetchproduct":{
                result=oCdcFetchProduct(params[1],params[2],params[3]);
                break;
            }
            case "cccfetchproduct":{
                result=oCccFetchProduct(params[1],params[2],params[3]);
                break;
            }
            case "cdcfetchactivity":{
                result=oCdcFetchActivity(params[1],params[2],params[3]);
                break;
            }
            case "cccfetchactivity":{
                result=oCccFetchActivity(params[1],params[2],params[3]);
                break;
            }
            case "completecall":{
                result=onCompleteCall(params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12],params[13],params[14]);
                break;
            }
            case "completechemcall":{
                result=onCompleteChemCall(params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12],params[13],params[14],params[15]);
                break;
            }
        }
        return result;
    }

    private String onCompleteChemCall(String webpath,String c_id,String ecode,String chemist_id,String p_id,String input,String orderqty,String total,String activity,String activitypositiveresponse,String activityfeedback,String reportdate,String latitude,String longitude,String uploadaddress) {
        String result=null;
        string_url=webpath+"android/chemistcall.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&chemist_id="+ URLEncoder.encode(chemist_id,"UTF-8")
                    +"&p_id="+ URLEncoder.encode(p_id,"UTF-8")
                    +"&input="+ URLEncoder.encode(input,"UTF-8")
                    +"&orderqty="+ URLEncoder.encode(orderqty,"UTF-8")
                    +"&total="+ URLEncoder.encode(total,"UTF-8")
                    +"&activity="+ URLEncoder.encode(activity,"UTF-8")
                    +"&activitypositiveresponse="+ URLEncoder.encode(activitypositiveresponse,"UTF-8")
                    +"&activityfeedback="+ URLEncoder.encode(activityfeedback,"UTF-8")
                    +"&reportdate="+ URLEncoder.encode(reportdate,"UTF-8")
                    +"&latitude="+ URLEncoder.encode(latitude,"UTF-8")
                    +"&longitude="+ URLEncoder.encode(longitude,"UTF-8")
                    +"&uploadaddress="+ URLEncoder.encode(uploadaddress,"UTF-8")
                    +"&completechemcall="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;   }

    private String oCccFetchActivity(String webpath, String c_id, String curr_month) {
        String result=null;
        string_url=webpath+"android/chemistcall.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&curr_month="+ URLEncoder.encode(curr_month,"UTF-8")
                    +"&cccfetchactivity="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String oCccFetchProduct(String webpath, String c_id, String curr_month) {
        String result=null;
        string_url=webpath+"android/chemistcall.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&curr_month="+ URLEncoder.encode(curr_month,"UTF-8")
                    +"&cccfetchproduct="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String onCcFetchChemistList(String webpath, String c_id, String ecode, String reportdate) {
        String result=null;
        string_url=webpath+"android/chemistcall.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&reportdate="+ URLEncoder.encode(reportdate,"UTF-8")
                    +"&ccfetchchemistlist="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;    }

    private String onCompleteCall(String webpath,String c_id,String ecode,String doctor_id,String p_id,String input,String sampleqty,String activity,String activitypositiveresponse,String activityfeedback,String reportdate,String latitude,String longitude,String uploadaddress) {
        String result=null;
        string_url=webpath+"android/doctorcall.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&doctor_id="+ URLEncoder.encode(doctor_id,"UTF-8")
                    +"&p_id="+ URLEncoder.encode(p_id,"UTF-8")
                    +"&input="+ URLEncoder.encode(input,"UTF-8")
                    +"&sampleqty="+ URLEncoder.encode(sampleqty,"UTF-8")
                    +"&activity="+ URLEncoder.encode(activity,"UTF-8")
                    +"&activitypositiveresponse="+ URLEncoder.encode(activitypositiveresponse,"UTF-8")
                    +"&activityfeedback="+ URLEncoder.encode(activityfeedback,"UTF-8")
                    +"&reportdate="+ URLEncoder.encode(reportdate,"UTF-8")
                    +"&latitude="+ URLEncoder.encode(latitude,"UTF-8")
                    +"&longitude="+ URLEncoder.encode(longitude,"UTF-8")
                    +"&uploadaddress="+ URLEncoder.encode(uploadaddress,"UTF-8")
                    +"&completecall="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String oCdcFetchActivity(String webpath, String c_id, String curr_month) {
        String result=null;
        string_url=webpath+"android/doctorcall.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&curr_month="+ URLEncoder.encode(curr_month,"UTF-8")
                    +"&cdcfetchactivity="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String oCdcFetchProduct(String webpath, String c_id, String curr_month) {
        String result=null;
        string_url=webpath+"android/doctorcall.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&curr_month="+ URLEncoder.encode(curr_month,"UTF-8")
                    +"&cdcfetchproduct="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String onDcFetchDoctorList(String webpath, String c_id, String ecode, String reportdate) {
        String result=null;
        string_url=webpath+"android/doctorcall.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&reportdate="+ URLEncoder.encode(reportdate,"UTF-8")
                    +"&dcfetchdoctorlist="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String onSaveSchedule(String webpath, String c_id, String ecode, String visitdate, String visitdoctors, String visitroutes) {
        String result=null;
        string_url=webpath+"android/tpvisit.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&visitdate="+ URLEncoder.encode(visitdate,"UTF-8")
                    +"&visitdoctors="+ URLEncoder.encode(visitdoctors,"UTF-8")
                    +"&visitroutes="+ URLEncoder.encode(visitroutes,"UTF-8")
                    +"&saveschedule="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String onTpFetchDoctorList(String webpath, String c_id, String ecode, String routes) {
        String result=null;
        string_url=webpath+"android/tpvisit.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&routes="+ URLEncoder.encode(routes,"UTF-8")
                    +"&tpfetchdoctorlist="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String onTpFetchRoute(String webpath, String c_id, String ecode) {
        String result=null;
        string_url=webpath+"android/tpvisit.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&tpfetchroute="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String onForgotPassword(String c_id, String webpath,String ecode) {
        String result=null;
        string_url=webpath+"android/login.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&forgotpassword="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }

    private String onFetchCompanyProcess(String code) {
        String result=null;
        string_url="https://mydcr.co.in/admin/android/client.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&code="+ URLEncoder.encode(code,"UTF-8")
                    +"&fetchcompany="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;

    }

    private String onLoginProcess(String c_id,String webpath,String ecode,String password,String device,String version) {
        String result=null;
        string_url=webpath+"android/login.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&password="+ URLEncoder.encode(password,"UTF-8")
                    +"&device="+ URLEncoder.encode(device,"UTF-8")
                    +"&version="+ URLEncoder.encode(version,"UTF-8")
                    +"&login="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }
    private String onLoginOutProcess(String c_id,String webpath,String ecode,String device) {
        String result=null;
        string_url=webpath+"android/logout.php";
        try {
            URL url=new URL(string_url);

            HttpsURLConnection hcon=(HttpsURLConnection)url.openConnection();
            hcon.setRequestMethod("POST");
            hcon.setDoOutput(true);
            hcon.setDoInput(true);


            OutputStream out=hcon.getOutputStream();
            BufferedWriter buff=new BufferedWriter(
                    new OutputStreamWriter(out,"UTF-8"));

            String postdata="&c_id="+ URLEncoder.encode(c_id,"UTF-8")
                    +"&ecode="+ URLEncoder.encode(ecode,"UTF-8")
                    +"&device="+ URLEncoder.encode(device,"UTF-8")
                    +"&logout="+URLEncoder.encode("true","UTF-8");

            buff.write(postdata);
            buff.flush();
            buff.close();
            out.close();

            InputStream in=hcon.getInputStream();
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(in,"ISO-8859-1"));
            String line;
            result="";
            while ((line=br.readLine())!=null)
            {
                result+=line+"\n";
            }

            br.close();
            in.close();

            hcon.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DCR",e.getMessage());
        }
        return result;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progress.cancel();
        display = new AlertDialog.Builder(cxt).setCancelable(true).create();
        if (type.equals("fetchCID")) {
            /*display.setTitle("Fetch Company Status!");
            display.setMessage(result);
            display.show();*/
           if(result.trim().startsWith("Invalid"))
           {
               Utilities.showAlert(cxt,"error",result);
           }
           else
           {
               String id,webpath,logo;
               id=result.trim().split("#")[0];
               webpath=result.trim().split("#")[1];
               logo=result.trim().split("#")[2];
               SharedPreferences preff=cxt.getSharedPreferences("mydcr", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor=preff.edit();
               editor.putString("c_id",id);
               editor.putString("webpath",webpath);
               editor.putString("logo",logo);
               editor.apply();
               cxt.startActivity(new Intent(cxt,LoginActivity.class));
           }
        }
        else if(type.equals("login")){
            if(result.trim().startsWith("Invalid") || result.trim().startsWith("User"))
            {
                Utilities.showAlert(cxt,"error",result);
            }
            else
            {
                String ecode,name,designation,hq;
                ecode=result.trim().split("#")[0];
                name=result.trim().split("#")[1];
                designation=result.trim().split("#")[2];
                hq=result.trim().split("#")[3];
                SharedPreferences preff=cxt.getSharedPreferences("mydcr", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preff.edit();
                editor.putString("ecode",ecode);
                editor.putString("name",name);
                editor.putString("designation",designation);
                editor.putString("hq",hq);
                editor.apply();
                cxt.startActivity(new Intent(cxt,DashboardActivity.class));
            }
        } else if (type.equals("forgotpassword")) {
            if(result.trim().startsWith("Invalid") || result.trim().startsWith("Error") ) {
                Utilities.showAlert(cxt, "error", result);
            }
            else {
                Utilities.showAlert(cxt, "info", result);
            }

        }
        else if(type.equals("tpfetchroute"))
        {

            if(result.trim().equals(""))
            {
                ((TpscheduleActivity)cxt).langArray=null;
                ((TpscheduleActivity)cxt).selectedLanguage=null;
            }
            else if(result.trim().contains("#"))
            {
                ((TpscheduleActivity)cxt).langArray=result.trim().split("#");
                ((TpscheduleActivity)cxt).selectedLanguage = new boolean[((TpscheduleActivity)cxt).langArray.length];
                ((TpscheduleActivity)cxt).setRouteEventDialog();
            }
            else
            {
                ((TpscheduleActivity)cxt).langArray=new String[1];
                ((TpscheduleActivity)cxt).langArray[0]=result.trim();
                ((TpscheduleActivity)cxt).selectedLanguage = new boolean[((TpscheduleActivity)cxt).langArray.length];
                ((TpscheduleActivity)cxt).setRouteEventDialog();
            }

        }
        else if(type.equals("tpfetchdoctorlist"))
        {
            if(result.trim().equals(""))
            {
                Toast.makeText(cxt, "No Doctor Found", Toast.LENGTH_SHORT).show();
            }
           else if(result.trim().contains("#"))
           {
               String[] data=result.trim().split("#");
               ((TpscheduleActivity)cxt).doctorlist=new ArrayList<>(data.length);
               ((TpscheduleActivity)cxt).selecteddoctor=new ArrayList<>(data.length);
               for (String val: data) {
                   ((TpscheduleActivity)cxt).doctorlist.add(val);
               }
               ((TpscheduleActivity)cxt).loadTable();

           }
           else
           {
               ((TpscheduleActivity)cxt).doctorlist=new ArrayList<>(1);
               ((TpscheduleActivity)cxt).selecteddoctor=new ArrayList<>(1);
               ((TpscheduleActivity)cxt).doctorlist.add(result.trim());
               ((TpscheduleActivity)cxt).loadTable();

           }

        }
        else if(type.equals("ccfetchchemistlist"))
        {
            if(result.trim().equals(""))
            {
                if(((ChemistCallActivity)cxt).chemistlist!=null)
                {
                    ((ChemistCallActivity)cxt).chemistlist.clear();
                    ((ChemistCallActivity)cxt).loadTable();
                }
                Toast.makeText(cxt, "No Chemist Planned", Toast.LENGTH_SHORT).show();
            }
            else if(result.trim().contains("#"))
            {
                String[] data=result.trim().split("#");
                ((ChemistCallActivity)cxt).chemistlist=new ArrayList<>(data.length);

                for (String val: data) {
                    ((ChemistCallActivity)cxt).chemistlist.add(val);
                }
                ((ChemistCallActivity)cxt).loadTable();

            }

            else
            {
                ((ChemistCallActivity)cxt).chemistlist=new ArrayList<>(1);

                ((ChemistCallActivity)cxt).chemistlist.add(result.trim());
                ((ChemistCallActivity)cxt).loadTable();

            }
        }
        else if(type.equals("dcfetchdoctorlist"))
        {
            if(result.trim().equals(""))
            {
                if(((DoctorCallActivity)cxt).doctorlist!=null)
                {
                    ((DoctorCallActivity)cxt).doctorlist.clear();
                    ((DoctorCallActivity)cxt).loadTable();
                }
                Toast.makeText(cxt, "No Doctor Planned", Toast.LENGTH_SHORT).show();
            }
            else if(result.trim().contains("#"))
            {
                String[] data=result.trim().split("#");
                ((DoctorCallActivity)cxt).doctorlist=new ArrayList<>(data.length);

                for (String val: data) {
                    ((DoctorCallActivity)cxt).doctorlist.add(val);
                }
                ((DoctorCallActivity)cxt).loadTable();

            }

            else
            {
                ((DoctorCallActivity)cxt).doctorlist=new ArrayList<>(1);

                ((DoctorCallActivity)cxt).doctorlist.add(result.trim());
                ((DoctorCallActivity)cxt).loadTable();

            }
            //Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
        }
        else if(type.equals("cdcfetchproduct"))
        {
            if(result.trim().equals(""))
            {
                ((CompleteCallActivity)cxt).data=null;
                ((CompleteCallActivity)cxt).langArray=null;
                ((CompleteCallActivity)cxt).selectedLanguage=null;
            }
            else if(result.trim().contains("#"))
            {
                ((CompleteCallActivity)cxt).productlist=new ArrayList<>();
                ((CompleteCallActivity)cxt).data=result.trim().split("#");

                ((CompleteCallActivity)cxt).langArray=new String[((CompleteCallActivity)cxt).data.length];
                for (int i=0;i<((CompleteCallActivity)cxt).data.length;i++) {
                    ((CompleteCallActivity)cxt).langArray[i]= ((CompleteCallActivity)cxt).data[i].split(":")[1]+" ["+((CompleteCallActivity)cxt).data[i].split(":")[2]+", "+((CompleteCallActivity)cxt).data[i].split(":")[3]+"]";
                    ((CompleteCallActivity)cxt).productlist.add(((CompleteCallActivity)cxt).data[i]);
                }
                ((CompleteCallActivity)cxt).selectedLanguage = new boolean[((CompleteCallActivity)cxt).langArray.length];

            }
            else
            {
                ((CompleteCallActivity)cxt).productlist=new ArrayList<>();
                ((CompleteCallActivity)cxt).data=new String[1];
                ((CompleteCallActivity)cxt).langArray=new String[1];
                ((CompleteCallActivity)cxt).productlist.add(((CompleteCallActivity)cxt).data[0]);
                ((CompleteCallActivity)cxt).langArray[0]=result.trim().split(":")[1]+" ["+result.trim().split(":")[2]+", "+result.trim().split(":")[3]+"]";
                ((CompleteCallActivity)cxt).selectedLanguage = new boolean[((CompleteCallActivity)cxt).langArray.length];
                //((CompleteCallActivity)cxt).setRouteEventDialog();
            }
            ((CompleteCallActivity)cxt).loadActivity();
            //Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
        }
        else if(type.equals("cccfetchproduct"))
        {
            if(result.trim().equals(""))
            {
                ((CompleteChemistCallActivity)cxt).data=null;
                ((CompleteChemistCallActivity)cxt).langArray=null;
                ((CompleteChemistCallActivity)cxt).selectedLanguage=null;
            }
            else if(result.trim().contains("#"))
            {
                ((CompleteChemistCallActivity)cxt).productlist=new ArrayList<>();
                ((CompleteChemistCallActivity)cxt).data=result.trim().split("#");

                ((CompleteChemistCallActivity)cxt).langArray=new String[((CompleteChemistCallActivity)cxt).data.length];
                for (int i=0;i<((CompleteChemistCallActivity)cxt).data.length;i++) {
                    ((CompleteChemistCallActivity)cxt).langArray[i]= ((CompleteChemistCallActivity)cxt).data[i].split(":")[1]+" ["+((CompleteChemistCallActivity)cxt).data[i].split(":")[2]+", "+((CompleteChemistCallActivity)cxt).data[i].split(":")[3]+"]";
                    ((CompleteChemistCallActivity)cxt).productlist.add(((CompleteChemistCallActivity)cxt).data[i]);
                }
                ((CompleteChemistCallActivity)cxt).selectedLanguage = new boolean[((CompleteChemistCallActivity)cxt).langArray.length];

            }
            else
            {
                ((CompleteChemistCallActivity)cxt).productlist=new ArrayList<>();
                ((CompleteChemistCallActivity)cxt).data=new String[1];
                ((CompleteChemistCallActivity)cxt).langArray=new String[1];
                ((CompleteChemistCallActivity)cxt).productlist.add(((CompleteChemistCallActivity)cxt).data[0]);
                ((CompleteChemistCallActivity)cxt).langArray[0]=result.trim().split(":")[1]+" ["+result.trim().split(":")[2]+", "+result.trim().split(":")[3]+"]";
                ((CompleteChemistCallActivity)cxt).selectedLanguage = new boolean[((CompleteChemistCallActivity)cxt).langArray.length];
                //((CompleteCallActivity)cxt).setRouteEventDialog();
            }
            ((CompleteChemistCallActivity)cxt).loadActivity();
            //Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
        }
        else if(type.equals("cdcfetchactivity"))
        {
            // TO DO remove hardcode
            Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
            if(result.trim().equals(""))
            {
                ((CompleteCallActivity)cxt).hasActivity=false;
                ((CompleteCallActivity)cxt).activitydata=null;
            }
            else if(result.trim().contains("#"))
            {
                ((CompleteCallActivity)cxt).hasActivity=true;
                ((CompleteCallActivity)cxt).activitydata=result.trim().split("#");
            }
            else
            {
                ((CompleteCallActivity)cxt).hasActivity=true;
                ((CompleteCallActivity)cxt).activitydata=new String[1];
                ((CompleteCallActivity)cxt).activitydata[0]=result.trim();
            }
            ((CompleteCallActivity)cxt).loadActivitySpinner();
            //Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
        }
        else if(type.equals("cccfetchactivity"))
        {
            if(result.trim().equals(""))
            {
                ((CompleteChemistCallActivity)cxt).hasActivity=false;
                ((CompleteChemistCallActivity)cxt).activitydata=null;
            }
            else if(result.trim().contains("#"))
            {
                ((CompleteChemistCallActivity)cxt).hasActivity=true;
                ((CompleteChemistCallActivity)cxt).activitydata=result.trim().split("#");
            }
            else
            {
                ((CompleteChemistCallActivity)cxt).hasActivity=true;
                ((CompleteChemistCallActivity)cxt).activitydata=new String[1];
                ((CompleteChemistCallActivity)cxt).activitydata[0]=result.trim();
            }
            ((CompleteChemistCallActivity)cxt).loadActivitySpinner();
            //Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
        }
        else if(type.equals("completecall"))
        {
            if(result.trim().startsWith("Error"))
            {
                Utilities.showAlert(cxt,"error",result);
            }
            else
            {
                Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
                ((CompleteCallActivity)cxt).finish();
            }
        }
        else if(type.equals("completechemcall"))
        {
            if(result.trim().startsWith("Error"))
            {
                Utilities.showAlert(cxt,"error",result);
            }
            else
            {
                Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
                ((CompleteChemistCallActivity)cxt).finish();
            }
        }
        else if(type.equals("saveschedule"))
        {
            if(result.trim().startsWith("Already") || result.trim().startsWith("Error"))
            {
                Utilities.showAlert(cxt,"error",result);
            }
            else {
                Toast.makeText(cxt, result, Toast.LENGTH_SHORT).show();
            }
        }
        else if (type.equals("loginout")) {
            if(result.trim().equals("0"))
            {
                SharedPreferences preff=cxt.getSharedPreferences("mydcr", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preff.edit();
                editor.putString("ecode","");
                editor.putString("c_id","");
                editor.apply();
            }
            else
            {
                cxt.startActivity(new Intent(cxt,LoginActivity.class));
            }


        }
    }
}
