package me.alexpanov.net;

import java.net.InetAddress;
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

    @Test(expected = IllegalArgumentException.class)
    public void overTheLimitPortAllocationShouldFail() throws Exception {
        FreePortFinder.findFreeLocalPort(FreePortFinder.MAX_PORT_NUMBER + 1);
    }

    @Test
    public void canBindToLocalHostToFoundPort() throws Exception {
        int port = FreePortFinder.findFreeLocalPort(InetAddress.getLocalHost());
        ServerSocket serverSocket = new ServerSocket(port, 50 , InetAddress.getLocalHost());
        assertThat(serverSocket.isBound(), is(true));
    }

    @Test
    public void canBindToMultipleHostsToFoundPort() throws Exception {
        int port = FreePortFinder.findFreeLocalPortOnAddresses(null, InetAddress.getLocalHost());
        ServerSocket serverSocket = new ServerSocket(port, 50 , InetAddress.getLocalHost());
        assertThat(serverSocket.isBound(), is(true));
    }

    @Test
    public void canBindToMultipleHostsMultipleAllocations() throws Exception {
        int port = FreePortFinder.findFreeLocalPortOnAddresses(null, InetAddress.getLocalHost());
        int nextPort = FreePortFinder.findFreeLocalPortOnAddresses(null, InetAddress.getLocalHost());
        assertThat(nextPort > port, is (true));
    }

}
