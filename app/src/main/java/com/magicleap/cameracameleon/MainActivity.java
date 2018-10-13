package com.magicleap.cameracameleon;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class MainActivity extends AppCompatActivity {
    public static final String PREVIOUS_URL = "PREVIOUS_URL";
    private ImageView imageView;
    private EditText textUrl;
    private String base_url;

    private static MyTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        imageView = (ImageView) findViewById(R.id.dump);
        textUrl = (EditText) findViewById(R.id.ip_address);

        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        textUrl.setText(preferences
                .getString(PREVIOUS_URL, getString(R.string.test_url)));

    }

    public void onClickBtnStartRequests(View view) {
        base_url = textUrl.getText().toString();

        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREVIOUS_URL, base_url);
        editor.apply();

        if(timer==null) {
            timer = new MyTimer(30000L, 500L);
        }
        if(!timer.isTicking()) {
            timer.begin();
        } else {
            timer.stop();
            timer = null;
        }
    }

    public void onClickBtnTestConnection(View view) {
        String test_url = getString(R.string.test_url);
        if(textUrl.getText().toString().equals(test_url)) {
            SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
            preferences.getString(PREVIOUS_URL, getString(R.string.test_url));
            textUrl.setText(preferences
                    .getString(PREVIOUS_URL, getString(R.string.test_url)));
        } else {
            textUrl.setText(R.string.test_url);
        }
        String base_url = textUrl.getText().toString();
        Glide
                .with(this)
                .load(base_url)
                .into(imageView);
    }

    public void onClickBtnTest(View view) {
        String url = getString(R.string.test_base_url);
        String endPoint = getString(R.string.test_end_point);
        String fileUrl = getString(R.string.test_filename);
        WebServiceController webController = new WebServiceController(new MyWebServiceEvents());
        webController.start(url, endPoint, fileUrl);
    }

    private class MyWebServiceEvents implements WebServiceEvents {

        @Override
        public void onSuccess(Bitmap bitmap) {
            ImageView dump = findViewById(R.id.dump);
            dump.setImageBitmap(bitmap);
        }

        @Override
        public void onFailure(String message) {

        }
    }

    private class MyTimer {
        private boolean ticking;
        private CountDownTimer countDownTimer;

        public MyTimer(long millisInFuture, long countDownInterval) {
            ticking = false;
            countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
                @Override
                public void onTick(long l) {
                    String endPoint = "";
                    String fileUrl = "";
                    WebServiceController webController = new WebServiceController(new MyWebServiceEvents());
                    webController.start(base_url, endPoint, fileUrl);

                }

                @Override
                public void onFinish() {

                }
            };
        }

        public void stop() {
            ticking = false;
            countDownTimer.cancel();
            Log.d("LUIS", "Stopped Ticking!");
        }

        public void begin() {
            ticking = true;
            countDownTimer.start();
            Log.d("LUIS", "Ticking!");
        }

        public boolean isTicking() {
            return ticking;
        }
    }

}
