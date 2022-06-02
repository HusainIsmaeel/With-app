package com.application.witch;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    EditText etUrl;
    ImageView homeBtn;
    AdView adView;
    Button btnGo, btnNum, urlBtn;
    int id = 1;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        homeBtn = findViewById(R.id.homeBtn);
        urlBtn = findViewById(R.id.urlBtn);
        btnGo = findViewById(R.id.btnGo);
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        etUrl = findViewById(R.id.etUrl);
        btnNum = findViewById(R.id.btnNum);
        WebSettings webSettings = webView.getSettings();
        adView = findViewById(R.id.adView);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                adView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new MyWebViewClient());
        if (!checkConnection()) {
            showDialog();
        }

        btnNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                id = 0;
                btnNum.setText(String.valueOf(id));
                return false;
            }
        });

        Intent intentHome = new Intent(this, MainActivity.class);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentHome);
            }
        });

        Intent intentUrl = new Intent(this, url.class);
        urlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.getUrl().startsWith("https://m.twitch.tv/videos/")) {
                    webView.loadUrl("javascript:var stream_img = document.getElementsByTagName(\"img\")["+id+"].src;\n" +
                            "    window.open(stream_img);");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String myUrl = webView.getUrl();
                            Intent intentUrl = new Intent(view.getContext(),url.class);
                            intentUrl.putExtra("myUrl",myUrl);
                            startActivity(intentUrl);
                        }
                    }, 1000);
                } else {
                    webView.loadUrl("javascript:var clip_vid = document.getElementsByTagName(\"video\")[0].src;\n" +
                            "    window.open(clip_vid);");
                    String url = webView.getUrl();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        btnNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id ++;
                btnNum.setText(String.valueOf(id));
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = etUrl.getText().toString().trim();
                webView.loadUrl(url);
                etUrl.setText(webView.getUrl());
            }
        });

        webView.loadUrl("https://www.twitch.tv");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    etUrl.setText(webView.getUrl());
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setDownloadListener(new MyDownLoadListener());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("market://") || url.startsWith("twitch://")) {
                try {
                    Context context = view.getContext();
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                    if (intent != null) {
                        view.stopLoading();

                        PackageManager packageManager = context.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            context.startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            view.loadUrl(fallbackUrl);
                        }

                        return true;
                    }
                } catch (URISyntaxException e) {
                        Log.e(TAG, "Can't resolve intent://", e);
                }
            }

            if (url.startsWith("market://")) {
                try {
                    Context context = view.getContext();
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                    if (intent != null) {
                        view.stopLoading();

                        PackageManager packageManager = context.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            context.startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            view.loadUrl(fallbackUrl);
                        }

                        return true;
                    }
                } catch (URISyntaxException e) {
                    Log.e(TAG, "Can't resolve intent://", e);
                }
            }

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to WIFI or Exit")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class MyDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            if (url != null) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
    }
}