package net.butterflytv.rtmp.server;

import net.butterflytv.rtmp.RtmpOpenException;

/**
 * RTMP server.
 */
public class RtmpServer {

    private static final String TAG = "RtmpServer";

    public static final int DEFAULT_PORT = 1935;

    static {
        System.loadLibrary("rtmp-jni");
    }

    private final static int OPEN_SUCCESS = 1;

    private long rtmpPointer = 0;

    public void open(String url) throws RtmpOpenException {
        rtmpPointer = nativeAlloc();
        int result = nativeOpen(url, rtmpPointer);
        if (result != OPEN_SUCCESS) {
            rtmpPointer = 0;
            throw new RtmpOpenException(result);
        }
    }

    private native long nativeAlloc();

    private native int nativeOpen(String url, long rtmpPointer);

    /**
     * Is the server connected?
     *
     * @return {@code true} if connected.
     */
    public boolean isConnected() {
        return nativeIsConnected(rtmpPointer);
    }

    private native boolean nativeIsConnected(long rtmpPointer);

    /**
     * Close the connection. Don't forget to call when finished!
     */
    public void close() {
        nativeClose(rtmpPointer);
        rtmpPointer = 0;
    }

    private native void nativeClose(long rtmpPointer);

}
