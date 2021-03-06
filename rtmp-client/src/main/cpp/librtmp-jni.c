#include <malloc.h>
#include "librtmp-jni.h"
#include "rtmp.h"
#include <android/log.h>
//
// Created by faraklit on 01.01.2016.
//


#define  LOG_TAG    "rtmp-jni"

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

//RTMP *rtmp = NULL;


JNIEXPORT jlong JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativeAlloc(JNIEnv* env, jobject thiz) {
    RTMP *rtmp = RTMP_Alloc();
    if (rtmp == NULL) {
        return -1;
    }
    return (jlong) rtmp;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativeOpen(JNIEnv* env, jobject thiz, jstring url_,
                                                        jboolean isPublishMode, jlong rtmpPointer) {

    const char *url = (*env)->GetStringUTFChars(env, url_, NULL);
    RTMP *rtmp = (RTMP *) rtmpPointer;
   // rtmp = RTMP_Alloc();
    if (rtmp == NULL) {
        (*env)->ReleaseStringUTFChars(env, url_, url);
        return -1;
    }

    RTMP_Init(rtmp);
    int ret = RTMP_SetupURL(rtmp, url);
    if (!ret) {
        RTMP_Free(rtmp);
        (*env)->ReleaseStringUTFChars(env, url_, url);
        return -2;
    }
    if (isPublishMode) {
        RTMP_EnableWrite(rtmp);
    }

    ret = RTMP_Connect(rtmp, NULL);
    if (!ret) {
        RTMP_Free(rtmp);
        (*env)->ReleaseStringUTFChars(env, url_, url);
        return -3;
    }

    ret = RTMP_ConnectStream(rtmp, 0);
    if (!ret) {
        (*env)->ReleaseStringUTFChars(env, url_, url);
        return -4;
    }

    (*env)->ReleaseStringUTFChars(env, url_, url);
    return 1;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    read
 * Signature: ([CI)I
 */
JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativeRead(JNIEnv* env, jobject thiz, jbyteArray data_,
                                                        jint offset, jint size, jlong rtmpPointer) {

    RTMP *rtmp = (RTMP *) rtmpPointer;
    if (rtmp == NULL) {
        throwIOException(env, "First call open function");
    }
    int connected = RTMP_IsConnected(rtmp);
    if (!connected) {
        throwIOException(env, "Connection to server is lost");
    }

    char* data = malloc(size);

    int readCount = RTMP_Read(rtmp, data, size);

    if (readCount > 0) {
        (*env)->SetByteArrayRegion(env, data_, offset, readCount, data);  // copy
    }
    free(data);
    if (readCount == 0) {
        return -1;
    }
    return readCount;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    write
 * Signature: ([CI)I
 */
JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativeWrite(JNIEnv* env, jobject thiz, jbyteArray data,
                                                         jint offset, jint size, jlong rtmpPointer) {

    RTMP *rtmp = (RTMP *) rtmpPointer;
    if (rtmp == NULL) {
        throwIOException(env, "First call open function");
    }

    int connected = RTMP_IsConnected(rtmp);
    if (!connected) {
        throwIOException(env, "Connection to server is lost");
    }

    jbyte* buf = malloc(size);
    (*env)->GetByteArrayRegion(env, data, offset, size, buf);
    int result = RTMP_Write(rtmp, buf, size);
    free(buf);

    return result;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    seek
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_seek(JNIEnv* env, jobject thiz, jint seekTime) {
    return 0;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    pause
 * Signature: (I)I
 */
JNIEXPORT jboolean JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativePause(JNIEnv* env, jobject thiz, jboolean pause,
                                                         jlong rtmpPointer) {

    RTMP *rtmp = (RTMP *) rtmpPointer;
    if (rtmp == NULL) {
        throwIOException(env, "First call open function");
    }

    int paused = RTMP_Pause(rtmp, pause);
    return paused ? true : false;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT void JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativeClose(JNIEnv* env, jobject thiz, jlong rtmpPointer) {

    RTMP *rtmp = (RTMP *) rtmpPointer;
    if (rtmp != NULL) {
        RTMP_Close(rtmp);
        RTMP_Free(rtmp);
    }
}

JNIEXPORT jboolean JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativeIsConnected(JNIEnv* env, jobject thiz, jlong rtmpPointer) {
    RTMP *rtmp = (RTMP *) rtmpPointer;
    if (rtmp == NULL) {
        return false;
    }
    int connected = RTMP_IsConnected(rtmp);
    return connected ? true : false;
}

JNIEXPORT jlong JNICALL Java_net_butterflytv_rtmp_server_RtmpServer_nativeAlloc
        (JNIEnv* env, jobject thiz) {
    RTMP* rtmp = RTMP_Alloc();
    if (rtmp == NULL) {
        return -1;
    }
    return (jlong) rtmp;
}

JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_server_RtmpServer_nativeOpen(JNIEnv *env, jobject thiz,
                                                       jint socketDescriptor, jlong rtmpPointer) {
    RTMP* rtmp = (RTMP*) rtmpPointer;
    if (rtmp == NULL) {
        return -1;
    }

    RTMP_Init(rtmp);
    rtmp->m_sb.sb_socket = socketDescriptor;

    int ret = RTMP_Serve(rtmp);
    if (!ret) {
        RTMP_Free(rtmp);
        return -3;
    }

    ret = RTMP_ConnectStream(rtmp, 0);
    if (!ret) {
        return -4;
    }

    return 1;
}

JNIEXPORT jboolean JNICALL Java_net_butterflytv_rtmp_server_RtmpServer_nativeIsConnected
        (JNIEnv* env, jobject thiz, jlong rtmpPointer) {
    RTMP* rtmp = (RTMP*) rtmpPointer;
    if (rtmp == NULL) {
        return false;
    }
    int connected = RTMP_IsConnected(rtmp);
    return connected ? true : false;
}

JNIEXPORT void JNICALL Java_net_butterflytv_rtmp_server_RtmpServer_nativeClose
        (JNIEnv* env, jobject thiz, jlong rtmpPointer) {
    RTMP* rtmp = (RTMP*) rtmpPointer;
    if (rtmp != NULL) {
        RTMP_Close(rtmp);
        RTMP_Free(rtmp);
    }
}

jint throwIOException (JNIEnv* env, char* message)
{
    jclass exception = (*env)->FindClass(env, "java/io/IOException");
    return (*env)->ThrowNew(env, exception, message);
}
