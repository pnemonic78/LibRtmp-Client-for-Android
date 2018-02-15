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

    public void open() throws RtmpOpenException {
        rtmpPointer = nativeAlloc();
        //TODO implement me!
    }

    private native long nativeAlloc();

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
