package net.butterflytv.rtmp;

import java.io.IOException;

/**
 * RTMP open connection exception.
 */
public class RtmpOpenException extends IOException {

    /**
     * it means there is a problem in memory allocation
     */
    public final static int OPEN_ALLOC = -1;

    /**
     * it means there is a problem in setting url, check the RTMP URL
     */
    public final static int OPEN_SETUP_URL = -2;

    /**
     * it means there is a problem in connecting to the RTMP server,
     * check there is an active network connection,
     * check RTMP server is running,
     */
    public final static int OPEN_CONNECT = -3;

    /**
     * it means there is a problem in connecting stream
     */
    public final static int OPEN_CONNECT_STREAM = -4;

    public final int errorCode;

    public RtmpOpenException(int errorCode) {
        this.errorCode = errorCode;
    }

}
