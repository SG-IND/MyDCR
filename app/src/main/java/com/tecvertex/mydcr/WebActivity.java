package com.tecvertex.mydcr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class WebActivity extends AppCompatActivity {

    String ecode,name,designation,hq,webpath,logo,page,activityname;
    WebView webView;
    TextView webename,webhq,webdesig,labelheading;
    ImageView weblogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        SharedPreferences preff = getSharedPreferences("mydcr", Context.MODE_PRIVATE);
        ecode = preff.getString("ecode", "");
        name = preff.getString("name", "");
        designation=preff.getString("designation", "");
        hq=preff.getString("hq", "");
        webpath = preff.getString("webpath", "");
        logo=preff.getString("logo", "");
        page=getIntent().getStringExtra("page").toString();
        activityname=getIntent().getStringExtra("activityname").toString().toUpperCase();
        page=page+"?ecode="+ecode.trim();

        webView=findViewById(R.id.showwebView);
        webename=findViewById(R.id.webename);
        webhq=findViewById(R.id.webhq);
        webdesig=findViewById(R.id.webdesig);
        weblogo=findViewById(R.id.weblogo);
        labelheading=findViewById(R.id.labelheading);

        labelheading.setText(activityname);
        webename.setText(Html.fromHtml("E.Name: <b>"+name+"</b>"));
        webhq.setText(Html.fromHtml("HQ: <b>"+hq+"</b>"));
        webdesig.setText(Html.fromHtml("Desig: <b>"+designation+"</b>"));
        Glide.with(WebActivity.this)
                .load("https://mydcr.victriz.com/admin/uploads/logo/"+logo)
                .centerInside()
                .into(weblogo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        final ProgressDialog progressBar = new ProgressDialog(WebActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!progressBar.isShowing()) {
                    progressBar.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            // Need to accept permissions to use the camera
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                // L.d("onPermissionRequest");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }
        });
        webView.loadUrl(webpath+"webapp/"+page.trim());

    }


    @Override
    public void onBackPressed() {

    }

    public void back(View view) {
        finish();
    }
}