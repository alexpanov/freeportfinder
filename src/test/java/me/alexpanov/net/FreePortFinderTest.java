package me.alexpanov.net;

import java.net.ServerSocket;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FreePortFinderTest {

    @Test
    public void canBindToFoundPort() throws Exception {
        int port = FreePortFinder.findFreeLocalPort();
        ServerSocket serverSocket = new ServerSocket(port);
        assertThat(serverSocket.isBound(), is(true));
    }
}
