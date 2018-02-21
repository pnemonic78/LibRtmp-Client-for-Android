package net.butterflytv.rtmp.server;

import net.butterflytv.rtmp.RtmpOpenException;

import android.util.Log;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ServerSocketFactory;

/**
 * RTMP server thread.
 */
public class RtmpServerThread extends Thread {

    private static final String TAG = "RtmpServerThread";

    private final ServerSocket serverSocket;
    private final List<RtmpServerSession> sessions = new ArrayList<>();

    public RtmpServerThread(int port) throws RtmpOpenException {
        setName(TAG);
        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
        } catch (IOException e) {
            throw new RtmpOpenException(RtmpOpenException.OPEN_ALLOC);
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "run enter " + this);
        Socket clientSocket;

        while (isRunning()) {
            try {
                clientSocket = serverSocket.accept();
            } catch (SocketException se) {
                if ("Socket closed".equals(se.getMessage())) {
                    // thread cancelled?
                    break;
                }
                Log.e(TAG, "Error accepting RTMP client: " + se.getLocalizedMessage(), se);
                continue;
            } catch (InterruptedIOException ie) {
                // thread cancelled?
                break;
            } catch (IOException e) {
                Log.e(TAG, "Error accepting RTMP client: " + e.getLocalizedMessage(), e);
                continue;
            }
            serve(clientSocket);
            cleanSessions();
        }
        Log.d(TAG, "run leave " + this);
    }

    protected boolean isRunning() {
        return (serverSocket != null) && isAlive() && !isInterrupted();
    }

    protected void serve(Socket socket) {
        Log.d(TAG, "serve enter " + this + " " + socket);
        RtmpServerSession session = new RtmpServerSession(socket);
        session.start();
        sessions.add(session);
        Log.d(TAG, "serve leave " + this + " " + socket);
    }

    public void cancel() {
        Log.d(TAG, "cancel enter " + this);
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ignore) {
            }
        }

        for (RtmpServerSession session : sessions) {
            session.close();
        }
        sessions.clear();

        interrupt();
        Log.d(TAG, "cancel leave " + this);
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    /** Remove closed sessions. */
    protected void cleanSessions() {
        //TODO implement me!
    }
}
