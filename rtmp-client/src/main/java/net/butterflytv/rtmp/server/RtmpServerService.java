package net.butterflytv.rtmp.server;

import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;

/**
 * RTMP server service.
 */
public class RtmpServerService {

    private static final String TAG = "RtmpServerService";

    public static final int DEFAULT_PORT = 1935;

    private RtmpServerThread serverThread;

    /**
     * Start the service on the default port.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void start() throws IOException {
        start(DEFAULT_PORT);
    }

    /**
     * Start the service.
     *
     * @param port the port to listen for incoming client connections.
     * @throws IOException if an I/O error occurs.
     */
    public RtmpServerThread start(int port) throws IOException {
        Log.d(TAG, "start enter " + this);
        if (serverThread != null) {
            throw new ConnectException("RTMP service already started!");
        }
        serverThread = new RtmpServerThread(port);
        serverThread.start();
        Log.d(TAG, "start leave " + this);
        return serverThread;
    }

    public boolean isRunning() {
        return (serverThread != null) && serverThread.isRunning();
    }

    /**
     * Stop the service.
     */
    public void stop() {
        Log.d(TAG, "stop enter " + this);
        if (serverThread != null) {
            serverThread.cancel();
            serverThread = null;
        }
        Log.d(TAG, "stop leave " + this);
    }

    public int getPort() {
        return (serverThread != null) ? serverThread.getPort() : 0;
    }
}
