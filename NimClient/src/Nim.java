//******************************************************************************
//
// File:    Nim.java
// Package: ---
// Unit:    Class Nim.java
//
//******************************************************************************

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.DatagramSocket;

/**
 * Class Nim is the main class for the Nim game, and sets up the UI, model, and
 * UDP connection
 *
 * @author  Kyle Blyth
 * @version 02-Dec-2015
 */
public class Nim {

	/**
	 * Main program.
	 * 
	 * @param  args  Initial params
	 * 
	 * @throw  Exception
	 * 	   thrown if an exception occurs
	 */
	public static void main
		(String[] args)
		throws Exception 
		{
		
		String serverhost = "";
		int serverport = 0;
		String clienthost = "";
		int clientport= 0;
		String playerName = "";

		// read in parameters
		if (args.length != 5) usage();
		try {
			serverhost = args[0];
			serverport = Integer.parseInt (args[1]);
			clienthost = args[2];
			clientport = Integer.parseInt (args[3]);
		 	playerName = args[4];
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// set up mailbox
		DatagramSocket mailbox =
			new DatagramSocket
				(new InetSocketAddress (clienthost, clientport));

		// create Models and View
		NimUI view = NimUI.create(playerName);
		
		InetSocketAddress serverAddress = new InetSocketAddress (serverhost, serverport);
		if(serverAddress.isUnresolved()) {
			System.err.println("Could not resolve hostname");
			System.exit(1);
		}
		
		final ModelProxy proxy =
			new ModelProxy
				(mailbox,
				 serverAddress);
		
		ModelClone model = new ModelClone();
		// set Listeners
		model.setModelListener(view);
		view.setViewListener (proxy);
		proxy.setModelListener (model);

		// add shutdown behavior
		Runtime.getRuntime().addShutdownHook (new Thread()
			{
			public void run()
				{
				try { proxy.quit(); }
					catch (IOException exc) {}
				}
			});

		// connect to the server
		proxy.join (null, playerName);
	} /* end main */

	/**
	 * Print a usage message and exit.
	 */
	private static void usage() {
		System.err.println ("Usage: java Nim <serverhost> <serverport> <clienthost> <clientport> <playername>");
		System.exit (1);
	}
} /* end class Nim */