//******************************************************************************
//
// File:   NimServer.java
// Package: ---
// Unit:    Class NimServer.java
//
//******************************************************************************

import java.net.InetSocketAddress;
import java.net.DatagramSocket;

/**
 * Class NimServer is the server main program for Nim
 *
 * @author Kyle Blyth
 * @version 02-Dec-2015
 */
public class NimServer
    {

    /**
     * Main program
     * 
     * @param  args  initial params
     * 
     * @throws  Exception  thrown if an exception occurs
     */
    public static void main
        (String[] args)
        throws Exception
        {
    	String host = "";
    	int port = 0;
    	
    	// read in parameters
        if (args.length != 2) usage();
        try {
        	host = args[0];
        	port = Integer.parseInt (args[1]);
        } catch(Exception e) {
        	System.err.println(e.getMessage());
        	System.exit(1);
        }

        DatagramSocket mailbox =
            new DatagramSocket
                (new InetSocketAddress (host, port));

        MailboxManager manager = new MailboxManager (mailbox);

        while(true) {
            manager.receiveMessage();
        }
	}

    /**
     * Print a usage message and exit
     */
    private static void usage() {
        System.err.println ("Usage: java NimServer <host> <port>");
        System.exit (1);
    }
}