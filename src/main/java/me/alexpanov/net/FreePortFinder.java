package me.alexpanov.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Finds currently available server ports.
 */
public final class FreePortFinder {

    /**
     * The minimum server currentMinPort number for IPv4.
     * Set at 1100 to avoid returning privileged currentMinPort numbers.
     */
    public static final int MIN_PORT_NUMBER = 1100;

    /**
     * The maximum server currentMinPort number for IPv4.
     */
    public static final int MAX_PORT_NUMBER = 65535;

    /**
     * We'll hold open the lowest port in this process
     * so parallel processes won't use the same block
     * of ports.   They'll go up to the next block.
     */
    private static final ServerSocket LOCK;

    /**
     * Incremented to the next lowest available port when findFreeLocalPort() is called.
     */
    private static AtomicInteger currentMinPort = new AtomicInteger(MIN_PORT_NUMBER);

    /**
     * Creates a new instance.
     */
    private FreePortFinder() {
        // Do nothing
    }
    
    static {
        int port = MIN_PORT_NUMBER;
        ServerSocket ss = null;

        while (ss == null) {
            try {
                ss = new ServerSocket(port);
            } catch (Exception e) {
                ss = null;
                port += 200;
            }
        } 
        LOCK = ss;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LOCK.close();
                } catch (Exception ex) {
                    //ignore
                }
            }
        });
        currentMinPort.set(port + 1);
    }

    /**
     * Gets the next available port starting at the lowest number. This is the preferred
     * method to use. The port return is immediately marked in use and doesn't rely on the caller actually opening
     * the port.
     *
     * @throws IllegalArgumentException is thrown if the port number is out of range
     * @throws NoSuchElementException if there are no ports available
     * @return the available port
     */
    public static synchronized int findFreeLocalPort() {
        int next = findFreeLocalPort(currentMinPort.get());
        currentMinPort.set(next + 1);
        return next;
    }

    /**
     * Gets the next available port starting at a given from port.
     *
     * @param fromPort the from port to scan for availability
     * @throws IllegalArgumentException is thrown if the port number is out of range
     * @throws NoSuchElementException if there are no ports available
     * @return the available port
     */
    public static synchronized int findFreeLocalPort(int fromPort) {
        if (fromPort < currentMinPort.get() || fromPort > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("From port number not in valid range: " + fromPort);
        }

        for (int i = fromPort; i <= MAX_PORT_NUMBER; i++) {
            if (available(i)) {
                return i;
            }
        }

        throw new NoSuchElementException("Could not find an available port above " + fromPort);
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port number to check for availability
     * @return <tt>true</tt> if the port is available, or <tt>false</tt> if not
     * @throws IllegalArgumentException is thrown if the port number is out of range
     */
    public static boolean available(int port) throws IllegalArgumentException {
        if (port < currentMinPort.get() || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start currentMinPort: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            // Do nothing
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

}

