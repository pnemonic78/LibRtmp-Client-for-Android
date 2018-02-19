package net.butterflytv.rtmp.server;

import java.io.IOException;
import java.net.ConnectException;

/**
 * RTMP server service.
 */
public class RtmpServerService {

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
    public void start(int port) throws IOException {
        if (serverThread != null) {
            throw new ConnectException("RTMP service already started!");
        }
        serverThread = new RtmpServerThread(port);
        serverThread.start();
    }

    public boolean isStarted() {
        return (serverThread != null) && serverThread.isRunning();
    }

    /**
     * Stop the service.
     */
    public void stop() {
        if (serverThread != null) {
            serverThread.cancel();
            serverThread = null;
        }
    }

    public int getPort() {
        return (serverThread != null) ? serverThread.getPort() : 0;
    }
}
