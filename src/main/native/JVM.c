#include <stdio.h>
#include <jni.h>

// public native long getTypeId(Class<?> clazz);
JNIEXPORT jlong JNICALL Java_jdk_jfr_internal_JVM_getTypeId(JNIEnv *env, jobject self, jclass clazz) {
    return -1;
}
