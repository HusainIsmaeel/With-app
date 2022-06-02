package com.application.witch;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.net.URL;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class url extends AppCompatActivity {

    ImageView imageView, donateView;
    TextView streamerTv, textView;
    EditText et;
    Button copyBtn, dldBtn, streamBtn;
    RadioGroup rGroup;
    AdView adView;
    RadioButton rb1080, rb720, rb480, rb360, rb160;
    public String part1 = null;
    public String part3 = null;
    public String part4 = null;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        streamerTv = findViewById(R.id.streamerTv);
        imageView = findViewById(R.id.imageView);
        streamBtn = findViewById(R.id.streamBtn);
        et = findViewById(R.id.et);
        adView = findViewById(R.id.adView);
        copyBtn = findViewById(R.id.copyBtn);
        rGroup = findViewById(R.id.rGroup);
        rb1080 = findViewById(R.id.rb1080);
        rb720 = findViewById(R.id.rb720);
        rb480 = findViewById(R.id.rb480);
        rb360 = findViewById(R.id.rb360);
        rb160 = findViewById(R.id.rb160);
        donateView = findViewById(R.id.donateView);
        textView = findViewById(R.id.textView);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://www.instagram.com/he96eh"));
                startActivity(browserIntent);
            }
        });

        donateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://www.paypal.me/h5en"));
                startActivity(browserIntent);
            }
        });

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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pasteText();
            }
        }, 1000);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Picasso.get().load(et.getText().toString()).into(imageView);
                Pattern pattern = Pattern.compile("(.*?)_(.*?)_");
                Matcher matcher = pattern.matcher(et.getText().toString());
                while (matcher.find()) {
                    streamerTv.setText(matcher.group(1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb1080.isChecked()) {
                    getUrl();
                    url = "https://" + part1 + ".cloudfront.net/" + part3 + "/chunked/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", url);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copy: 1080p 60fps", Toast.LENGTH_SHORT).show();
                } else if (rb720.isChecked()) {
                    getUrl();
                    url = "https://"+part1+".cloudfront.net/"+part3+"/720p60/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",url);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Copy: 720p 60fps",Toast.LENGTH_SHORT).show();
                } else if (rb480.isChecked()) {
                    getUrl();
                    url = "https://"+part1+".cloudfront.net/"+part3+"/480p30/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",url);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Copy: 480p 30fps",Toast.LENGTH_SHORT).show();
                } else if (rb360.isChecked()) {
                    getUrl();
                    url = "https://"+part1+".cloudfront.net/"+part3+"/360p30/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",url);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Copy: 360p 30fps",Toast.LENGTH_SHORT).show();
                } else if (rb160.isChecked()) {
                    getUrl();
                    url = "https://"+part1+".cloudfront.net/"+part3+"/160p30/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",url);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Copy: 160p 30fps",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Select a resolution إختر جودة البث",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        dldBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (rb1080.isChecked()) {
//                    getClip();
//                    url = "https://clips-media-assets2.twitch.tv/"+part4+".mp4";
//                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText("label",url);
//                    clipboard.setPrimaryClip(clip);
//                    Toast.makeText(getApplicationContext(),"Download: 1080p 60fps",Toast.LENGTH_SHORT).show();
//                    Uri uri = Uri.parse(url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                } else if (rb720.isChecked()) {
//                    getClip();
//                    url = "https://clips-media-assets2.twitch.tv/"+part4+"-720.mp4";
//                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText("label",url);
//                    clipboard.setPrimaryClip(clip);
//                    Toast.makeText(getApplicationContext(),"Download: 720p 60fps",Toast.LENGTH_SHORT).show();
//                    Uri uri = Uri.parse(url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                } else if (rb480.isChecked()) {
//                    getClip();
//                    url = "https://clips-media-assets2.twitch.tv/"+part4+"-480.mp4";
//                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText("label",url);
//                    clipboard.setPrimaryClip(clip);
//                    Toast.makeText(getApplicationContext(),"Download: 480p 30fps",Toast.LENGTH_SHORT).show();
//                    Uri uri = Uri.parse(url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                } else if (rb360.isChecked()) {
//                    getClip();
//                    url = "https://clips-media-assets2.twitch.tv/"+part4+"-360.mp4";
//                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText("label",url);
//                    clipboard.setPrimaryClip(clip);
//                    Toast.makeText(getApplicationContext(),"Download: 360p 30fps",Toast.LENGTH_SHORT).show();
//                    Uri uri = Uri.parse(url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getApplicationContext(),"Select a resolution إختر جودة المقطع",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        streamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPlayer = new Intent(view.getContext(),player.class);
                if (rb1080.isChecked()) {
                    getUrl();
                    url = "https://" + part1 + ".cloudfront.net/" + part3 + "/chunked/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", url);
                    clipboard.setPrimaryClip(clip);
                    intentPlayer.putExtra("link",url);
                    startActivity(intentPlayer);
                } else if (rb720.isChecked()) {
                    getUrl();
                    url = "https://"+part1+".cloudfront.net/"+part3+"/720p60/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",url);
                    clipboard.setPrimaryClip(clip);
                    intentPlayer.putExtra("link",url);
                    startActivity(intentPlayer);
                } else if (rb480.isChecked()) {
                    getUrl();
                    url = "https://"+part1+".cloudfront.net/"+part3+"/480p30/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",url);
                    clipboard.setPrimaryClip(clip);
                    intentPlayer.putExtra("link",url);
                    startActivity(intentPlayer);
                } else if (rb360.isChecked()) {
                    getUrl();
                    url = "https://"+part1+".cloudfront.net/"+part3+"/360p30/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",url);
                    clipboard.setPrimaryClip(clip);
                    intentPlayer.putExtra("link",url);
                    startActivity(intentPlayer);
                } else if (rb160.isChecked()) {
                    getUrl();
                    url = "https://"+part1+".cloudfront.net/"+part3+"/160p30/index-dvr.m3u8";
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",url);
                    clipboard.setPrimaryClip(clip);
                    intentPlayer.putExtra("link",url);
                    startActivity(intentPlayer);
                } else {
                    Toast.makeText(getApplicationContext(),"Select a resolution إختر جودة البث",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void pasteText() {

        if (getIntent().getStringExtra("myUrl").equals("https://m.twitch.tv/")) {
            // Do nothing
        } else if (getIntent().getStringExtra("myUrl").startsWith("https://static")) {
            et.setText(getIntent().getStringExtra("myUrl"));
            if (et.getText().length() > 30) {
                et.setFocusable(false);
            }
        } else {
            et.setText(getIntent().getStringExtra("myUrl"));
        }
    }

    private void getUrl () {
        Pattern pattern = Pattern.compile("https://static-cdn.jtvnw.net/cf_vods/(.*?)/");
        Matcher matcher = pattern.matcher(et.getText().toString());
        while (matcher.find()) {
            part1 = matcher.group(1);
        }
        String part2 = "https://static-cdn.jtvnw.net/cf_vods/"+part1+"/";
        Pattern pattern2 = Pattern.compile(part2+"(.*?)/");
        Matcher matcher2 = pattern2.matcher(et.getText().toString());
        while (matcher2.find()) {
            part3 = matcher2.group(1);
        }
    }

//    private void getClip () {
//        Pattern pattern3 = Pattern.compile("https://clips-media-assets2.twitch.tv/(.*?)-preview-260x147.jpg");
//        Matcher matcher3 = pattern3.matcher(et.getText().toString());
//        while (matcher3.find()) {
//            part4 = matcher3.group(1);
//        }
//    }

}