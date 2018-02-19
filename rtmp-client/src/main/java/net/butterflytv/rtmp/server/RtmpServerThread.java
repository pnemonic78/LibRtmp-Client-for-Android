package net.butterflytv.rtmp.server;

import net.butterflytv.rtmp.RtmpOpenException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

/**
 * RTMP server thread.
 */
public class RtmpServerThread extends Thread {

    private static final String TAG = "RtmpServerThread";

    private ServerSocket serverSocket;

    public RtmpServerThread(int port) throws RtmpOpenException {
        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
        } catch (IOException e) {
            throw new RtmpOpenException(RtmpOpenException.OPEN_ALLOC);
        }
    }

    @Override
    public void run() {
//        Socket clientSocket;
//
//        while (isRunning()) {
//            try {
//                clientSocket = serverSocket.accept();
//            } catch (IOException e) {
//                Log.e(TAG, "Error accepting client: " + e.getLocalizedMessage(), e);
//                continue;
//            }
//            serve(clientSocket);
//        }
    }

    protected boolean isRunning() {
        return (serverSocket != null) && isAlive() && !isInterrupted();
    }

    protected void serve(Socket socket) {

    }

    public void cancel() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ignore) {
            }
        }
        interrupt();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }
}
