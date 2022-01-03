package me.alexpanov.net;

import java.net.InetAddress;
import java.net.ServerSocket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FreePortFinderTest {

    @Test
    public void canBindToFoundPort() throws Exception {
        int port = FreePortFinder.findFreeLocalPort();
        ServerSocket serverSocket = new ServerSocket(port);
        assertThat(serverSocket.isBound(), is(true));
    }

    @Test
    public void overTheLimitPortAllocationShouldFail() throws Exception {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                FreePortFinder.findFreeLocalPort(FreePortFinder.MAX_PORT_NUMBER + 1);
            }
        });
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
