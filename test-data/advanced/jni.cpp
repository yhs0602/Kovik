#include <jni.h>


extern "C" JNICALL jstring
Java_com_example_sample_NativeExample_testNative(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("Hello from JNI !");
}
