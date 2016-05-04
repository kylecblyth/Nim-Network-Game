//******************************************************************************
//
// File:    ViewProxy.java
// Package: ---
// Unit:    Class ViewProxy.java
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
 * Class ViewProxy provides the network proxy for the server-side view object.
 * It communicates with the ModelProxy object in the Nim client
 *
 * @author  Kyle Blyth
 * @version 2-Dec-2015
 */
public class ViewProxy
	implements ModelListener
	{

// Hidden data members

	private DatagramSocket mailbox;
	private SocketAddress clientAddress;
	private ViewListener viewListener;

// Exported constructors

	/**
	 * Construct a new view proxy
	 *
	 * @param  mailbox        Server's mailbox
	 * @param  clientAddress  Client's mailbox address
	 */
	public ViewProxy
		(DatagramSocket mailbox,
		 SocketAddress clientAddress)
		{
		this.mailbox = mailbox;
		this.clientAddress = clientAddress;
	}

// Exported operations

	/**
	 * Set the view listener object for this view proxy
	 *
	 * @param  viewListener  View listener
	 */
	public void setViewListener
		(ViewListener viewListener)
		{
		this.viewListener = viewListener;
	}

	/**
	 * Report that a player ID has been set
	 *
	 * @param  id  The player's ID
	 * 
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void idSet(int id) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('I');
		out.writeByte(id);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	 * Report that a player name has been set
	 *
	 * @param  playerId    Id of the player
	 * @param  playerName  The player's name
	 * 
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void nameSet(int playerId, String playerName) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('N');
		out.writeByte (playerId);
		out.writeUTF (playerName);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, clientAddress));
	}
	
	/**
	 * Report that a player scored
	 *
	 * @param  isMe   Is this player me?
	 * @param  i      The Id of the player who scored
	 * @param  s      The player's score
	 * @param  pName  The name of the player who scored
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void scoreSet
		(boolean isMe,
		 int i,
		 int s,
		 String pName)
		throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('S');
		out.writeByte (i);
		out.writeByte (s);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, clientAddress));
	}
	
	/**
	 * Report that markers were removed from a heap
	 *
	 * @param  h  Id of the heap removed from
	 * @param  m  The number of markers removed
	 * 
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void heapSet
		(int h,
		 int m)
		throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('H');
		out.writeByte (h);
		out.writeByte (m);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, clientAddress));
	}
	
	/**
	 * Report that it's another player's turn
	 * 
	 * @param  pid  Id of the player whose turn it is
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void turnSet(int pid) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('T');
		out.writeByte(pid);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	 * Report that there's been a winner
	 * 
	 * @param  id  The id of the winner
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void winnerSet
		(int id) 
		throws IOException 
		{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('W');
		out.writeByte(id);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, clientAddress));
	}
	
	/**
	 * Tell clients to quit the session
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void gameQuit() 
		throws IOException 
		{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('Q');
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send
			(new DatagramPacket (payload, payload.length, clientAddress));
	}
	
	/**
	 * Process a received datagram
	 *
	 * @param  datagram  Datagram
	 *
	 * @return  True to discard this view proxy, false otherwise
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public boolean process
		(DatagramPacket datagram)
		throws IOException
		{
		boolean discard = false;
		DataInputStream in =
			new DataInputStream
				(new ByteArrayInputStream
					(datagram.getData(), 0, datagram.getLength()));
		String playerName;
		int h, m;
		byte b = in.readByte();
		switch (b)
			{
			case 'J':
				playerName = in.readUTF();
				viewListener.join (ViewProxy.this, playerName);
				break;
			case 'T':
				h = in.readByte();
				m = in.readByte();
				viewListener.removeMarker (h, m);
				break;
			case 'N':
				viewListener.newGame();
				break;
			case 'Q':
				discard = true;
				viewListener.quit();
				break;
			default:
				System.err.println ("Bad message"); // Shouldn't happen
				break;
			}
		return discard;
		}

	/**
	 * Removes the winner text from the UI
	 */
	public void clearWinner() { }

	/**
	 * Reports a session is full and a new game can be started
	 */
	public void enableNewGame() { }

	/**
	 * Report that the New Game button was clicked
	 */
	public void newGameClicked() { }

	/**
	 * enables all heaps
	 */
	public void enableHeaps() { }

	/**
	 * disables all heaps
	 */
	public void disableHeaps() { }

	/**
	 * Shows winner text on the UI
	 * @param playerName  The name of the winner
	 */
	public void setWinner(String playerName) { }
}