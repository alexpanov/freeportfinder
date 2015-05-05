package me.alexpanov.net;

import java.io.IOException;
import java.net.ServerSocket;

public final class FreePortFinder {

    private FreePortFinder() {
    }

    public static int findFreeLocalPort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            serverSocket.close();
            return port;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
