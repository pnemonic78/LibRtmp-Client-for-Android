package net.butterflytv.rtmp.server;

import net.butterflytv.rtmp.RtmpOpenException;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * RTMP server session to handle a single RTMP client.
 */
public class RtmpServerSession extends Thread {

    private static final String TAG = "RtmpServerSession";

    private final Socket socket;
    private RtmpServer server;

    public RtmpServerSession(Socket socket) {
        setName(TAG + "-" + socket);
        this.socket = socket;
        this.server = new RtmpServer();
    }

    @Override
    public void run() {
        process();
    }

    /**
     * Process the client request.
     */
    public void process() {
        //TODO read the client URL from the socket.
        String url = "";
        try {
            server.open(url);
        } catch (RtmpOpenException e) {
            Log.e(TAG, "Error opening RTMP server:" + e.getLocalizedMessage(), e);
        }
    }

    /**
     * Close the session.
     */
    public void close() {
        if (server != null) {
            server.close();
            server = null;
        }
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing RTMP socket:" + e.getLocalizedMessage(), e);
        }
        interrupt();
    }
}
