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
        Log.d(TAG, "run enter " + this + " socket=" + socket);
        process();
        close();
        Log.d(TAG, "run leave " + this + " socket=" + socket);
    }

    /**
     * Process the client request.
     */
    public void process() {
        Log.d(TAG, "process enter " + this + " socket=" + socket);
        try {
            server.open(socket);
        } catch (RtmpOpenException e) {
            Log.e(TAG, "Error opening RTMP server: " + e.getErrorCode(), e);
        }
        Log.d(TAG, "process leave " + this + " socket=" + socket);
    }

    /**
     * Close the session.
     */
    public void close() {
        Log.d(TAG, "close enter " + this + " socket=" + socket);
        if (server != null) {
            server.close();
            server = null;
        }
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing RTMP socket: " + e.getLocalizedMessage(), e);
        }
        interrupt();
        Log.d(TAG, "close leave " + this);
    }
}
