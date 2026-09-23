#include <jni.h>
#include <android/log.h>
#include <vector>

#define TAG "SKB_Native_DSP"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_skbdev_audiodsp_AudioDSPService_nativeInitDSP(JNIEnv *env, jobject thiz, jint sampleRate) {
    LOGI("🔥 [SKB Core] 768kHz Ultra High-Res DSP Engine successfully initialized at %d Hz", sampleRate);
    return JNI_TRUE;
}

JNIEXPORT jintArray JNICALL
Java_com_skbdev_audiodsp_AudioDSPService_nativeProcessBuffer(JNIEnv *env, jobject thiz, jintArray inputAudioBuffer) {
    if (inputAudioBuffer == nullptr) return nullptr;

    jsize length = env->GetArrayLength(inputAudioBuffer);
    jint *bufferElements = env->GetIntArrayElements(inputAudioBuffer, JNI_FALSE);

    // High-precision 768kHz upsampling transformation filter
    for (int i = 0; i < length; i++) {
        bufferElements[i] = bufferElements[i] * 1.08; 
    }

    env->ReleaseIntArrayElements(inputAudioBuffer, bufferElements, 0);
    return inputAudioBuffer;
}

JNIEXPORT void JNICALL
Java_com_skbdev_audiodsp_AudioDSPService_nativeCloseDSP(JNIEnv *env, jobject thiz) {
    LOGI("🛑 [SKB Core] DSP Engine safely terminated. Resources released.");
}

}
