package net.butterflytv.rtmp.server;

import net.butterflytv.rtmp.RtmpOpenException;

import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.Socket;

import static net.butterflytv.rtmp.RtmpOpenException.OPEN_CONNECT;

/**
 * RTMP server.
 */
public class RtmpServer {

    private static final String TAG = "RtmpServer";

    static {
        System.loadLibrary("rtmp-jni");
    }

    private final static int OPEN_SUCCESS = 1;

    private long rtmpPointer = 0;

    public void open(Socket socket) throws RtmpOpenException {
        Log.d(TAG, "open enter " + this + " socket=" + socket);
        rtmpPointer = nativeAlloc();

        if (socket.isClosed() || !socket.isConnected()) {
            throw new RtmpOpenException(OPEN_CONNECT);
        }
        int fd;
        try {
            fd = getFileDescriptor(socket);
            if (fd == -1) {
                throw new RtmpOpenException(OPEN_CONNECT);
            }
        } catch (IOException e) {
            throw new RtmpOpenException(OPEN_CONNECT);
        }
        int result = nativeOpen(fd, rtmpPointer);
        if (result != OPEN_SUCCESS) {
            rtmpPointer = 0;
            throw new RtmpOpenException(result);
        }
        Log.d(TAG, "open leave " + this + " socket=" + socket);
    }

    private native long nativeAlloc();

    private native int nativeOpen(int socketDescriptor, long rtmpPointer);

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
        Log.d(TAG, "close enter " + this);
        nativeClose(rtmpPointer);
        rtmpPointer = 0;
        Log.d(TAG, "close leave " + this);
    }

    private native void nativeClose(long rtmpPointer);

    private int getFileDescriptor(Socket socket) throws IOException {
        FileDescriptor fd = null;
        try {
            Class<? extends Socket> clazz = socket.getClass();
            Method method = clazz.getDeclaredMethod("getFileDescriptor$");
            fd = (FileDescriptor) method.invoke(socket);
        } catch (Exception e) {
        }

        if (fd == null) {
            InputStream in = socket.getInputStream();
            if (in instanceof FileInputStream) {
                FileInputStream fin = (FileInputStream) in;
                fd = fin.getFD();
            }
        }

        if (fd != null) {
            Class<? extends FileDescriptor> clazz = fd.getClass();
            try {
                Method method = clazz.getDeclaredMethod("getInt$");
                return (int) method.invoke(fd);
            } catch (Exception e) {
            }
        }

        return -1;
    }

}
