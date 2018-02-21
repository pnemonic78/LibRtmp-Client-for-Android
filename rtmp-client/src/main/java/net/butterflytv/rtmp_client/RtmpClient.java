package net.butterflytv.rtmp_client;

import net.butterflytv.rtmp.RtmpOpenException;

import android.util.Log;

import java.io.IOException;

/**
 * Created by faraklit on 01.01.2016.
 */
public class RtmpClient {

    private static final String TAG = "RtmpClient";

    static {
        System.loadLibrary("rtmp-jni");
    }


    private final static int OPEN_SUCCESS = 1;
    private long rtmpPointer = 0;

    public static class RtmpIOException extends RtmpOpenException {

        public RtmpIOException(int errorCode) {
            super(errorCode);
        }

    }

    public void open(String url, boolean isPublishMode) throws RtmpIOException {
        Log.d(TAG, "open enter " + this + " url=" + url);
        rtmpPointer = nativeAlloc();
        int result = nativeOpen(url, isPublishMode, rtmpPointer);
        if (result != OPEN_SUCCESS) {
            rtmpPointer = 0;
            throw new RtmpIOException(result);
        }
        Log.d(TAG, "open enter " + this + " url=" + url);
    }

    private native long nativeAlloc();

    /**
     * opens the rtmp url
     * @param url
     * url of the stream
     * @param isPublishMode
     * if this is an publication it is true,
     * if connection is for getting stream it is false
     * @return return a minus value if it fails
     * returns

     *
     * returns {@link #OPEN_SUCCESS} if it is successful, throws RtmpIOException if it is failed
     */
    private native int nativeOpen(String url, boolean isPublishMode, long rtmpPointer);

    /**
     * read data from rtmp connection
     *
     * @param data
     * buffer that will be filled
     * @param offset
     * offset to read data
     * @param size
     * size of the data to be reat
     * @return
     * number of bytes to be read
     *
     * if it returns -1, it means stream is complete
     *  and close function can be called.
     *
     *
     *  @throws IOException if connection is not opened or connection to server is lost
     *
     */
    public int read(byte[] data, int offset, int size) throws IOException {
        return nativeRead(data, offset, size, rtmpPointer);
    }

    private native int nativeRead(byte[] data, int offset, int size, long rtmpPointer) throws IOException;

    /**
     *
     * @param data
     * @return number of bytes written
     * @throws IOException if connection is not opened or connection to server is lost
     */
    public int write(byte[] data) throws IOException {
        return write(data, 0, data.length);
    }

    /**
     *
     * @param data
     * @return number of bytes written
     * @throws IOException if connection is not opened or connection to server is lost
     */
    public int write(byte[] data, int offset, int size) throws IOException {
        return nativeWrite(data, offset, size, rtmpPointer);
    }

    private native int nativeWrite(byte[] data, int offset, int size, long rtmpPointer) throws IOException;


    /**
     * @param pause
     * if pause is true then stream is going to be paused
     *
     * If pause is false, it unpauses the stream and it is ready to to play again
     *
     * @return true if it is successfull else returns false
     */
    public boolean pause(boolean pause) {
        return nativePause(pause, rtmpPointer);
    }
    private native boolean nativePause(boolean pause, long rtmpPointer);



    /**
     *
     * @return true if it is connected
     * false if it is not connected
     */
    public boolean isConnected() {
        return nativeIsConnected(rtmpPointer);
    }

    private native boolean nativeIsConnected(long rtmpPointer);

    /**
     *
     * closes the connection. Dont forget to call
     */
    public void close() {
        Log.d(TAG, "close enter " + this);
        nativeClose(rtmpPointer);
        rtmpPointer = 0;
        Log.d(TAG, "close leave " + this);
    }

    private native void nativeClose(long rtmpPointer);

}
