//******************************************************************************
//
// File:    ModelProxy.java
// Package: ---
// Unit:    Class ModelProxy.java
//
//******************************************************************************

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 * Class ModelProxy performs all application logic of the Nim game
 *
 * @author  Kyle Blyth
 * @version 02-Dec-2015
 */
public class ModelProxy
	implements ViewListener
	{

// Hidden data members

	private DatagramSocket mailbox;
	private SocketAddress destination;
	private ModelListener modelListener;

// Exported constructors

	/**
	 * Construct a new model proxy
	 *
	 * @param  mailbox  Mailbox
	 * @param  destination  Destination mailbox address
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public ModelProxy
		(DatagramSocket mailbox,
		 SocketAddress destination)
		throws IOException
		{
			this.mailbox = mailbox;
			this.destination = destination;
	}

// Exported operations.

	/**
	 * Set the model listener object for this model proxy.
	 *
	 * @param  modelListener  Model listener.
	 */
	public void setModelListener
		(ModelListener modelListener)
		{
		this.modelListener = modelListener;
		new ReaderThread().start();
	}
	
	/**
	 * Join the given session
	 *
	 * @param  playerName    Name of the player joining
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void join
		(ViewProxy viewProxy,
		 String playerName)
		throws IOException
		{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('J');
		out.writeUTF (playerName);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, destination));
	}

	/**
	 * Remove markers from a heap
	 *
	 * @param  h  Heap from which to remove the markers.
	 * @param  m  Number of markers to remove from heap
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void removeMarker
		(int h,
		 int m)
		throws IOException 
		{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('T');
		out.writeByte (h);
		out.writeByte (m);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, destination));
	}

	/**
	 * Start a new game.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void newGame()
		throws IOException
		{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('N');
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, destination));
	}

	/**
	 * Quit the game
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void quit()
		throws IOException
		{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('Q');
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, destination));
	}

// Hidden helper classes

	/**
	 * Class ReaderThread receives messages from the network, decodes them, and
	 * invokes the proper methods to process them
	 *
	 * @author  Alan Kaminsky
	 * @author  Kyle Blyth
	 * @version 02-Dec-2015
	 */
	private class ReaderThread
		extends Thread
		{
		// main thread function
		public void run()
			{
			byte[] payload = new byte [128]; /* CAREFUL OF BUFFER SIZE! */
			try {
				for (;;) {
					DatagramPacket packet =
						new DatagramPacket (payload, payload.length);
					mailbox.receive (packet);
					DataInputStream in =
						new DataInputStream
							(new ByteArrayInputStream
								(payload, 0, packet.getLength()));
					int h, m, pid, s;
					String playerName;
					byte b = in.readByte();
					switch (b) {
						case 'I':
							pid = in.readByte();
							modelListener.idSet(pid);
							break;
						case 'N':
							pid = in.readByte();
							playerName = in.readUTF();
							modelListener.nameSet(pid, playerName);
							if(pid == 1) {
								modelListener.enableNewGame();
							}
							break;
						case 'S':
							pid = in.readByte();
							s = in.readByte();
							modelListener.scoreSet(false, s, pid, "");
							break;
						case 'H':
							h = in.readByte();
							m = in.readByte();
							modelListener.heapSet (h, m);
							break;
						case 'T':
							pid = in.readByte();
							modelListener.turnSet (pid);
							break;
						case 'W':
							pid = in.readByte();
							modelListener.winnerSet(pid);
							break;
						case 'Q':
							System.exit(0);
							break;
						default:
							System.err.println ("Bad message"); // Shouldn't happen
							break;
					}
				}
			} catch (IOException exc) {
			} finally {
				mailbox.close();
			}
		} /* end run */
	} /* end ReaderThread */
} /* end ModelProxy */