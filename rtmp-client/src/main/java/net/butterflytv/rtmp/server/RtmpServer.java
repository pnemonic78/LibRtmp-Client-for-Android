package net.butterflytv.rtmp.server;

import net.butterflytv.rtmp.RtmpOpenException;

/**
 * RTMP server.
 */
public class RtmpServer {

    static {
        System.loadLibrary("rtmp-jni");
    }

    private final static int OPEN_SUCCESS = 1;

    private long rtmpPointer = 0;

    public void open(String url, boolean isPublishMode) throws RtmpOpenException {
        rtmpPointer = nativeAlloc();
        //TODO implement me!
    }

    private native long nativeAlloc();

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
        nativeClose(rtmpPointer);
        rtmpPointer = 0;
    }

    private native void nativeClose(long rtmpPointer);

}
