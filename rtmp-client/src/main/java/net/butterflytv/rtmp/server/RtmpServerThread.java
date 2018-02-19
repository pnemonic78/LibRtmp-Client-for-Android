package net.butterflytv.rtmp.server;

import net.butterflytv.rtmp.RtmpOpenException;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
        Socket clientSocket;

        while (isRunning()) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Error accepting RTMP client: " + e.getLocalizedMessage(), e);
                continue;
            }
            serve(clientSocket);
        }
    }

    protected boolean isRunning() {
        return (serverSocket != null) && isAlive() && !isInterrupted();
    }

    protected void serve(Socket socket) {
        RtmpServerSession session = new RtmpServerSession(socket);
        session.start();
        sessions.add(session);
    }

    public void cancel() {
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
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }
}
