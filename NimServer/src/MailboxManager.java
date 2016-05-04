//******************************************************************************
//
// File:    MailboxManager.java
// Package: ---
// Unit:    Class MailboxManager
//
// This Java source file is copyright (C) 2010 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
//
// This Java source file is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 3 of the License, or (at your option) any
// later version.
//
// This Java source file is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details.
//
// You may obtain a copy of the GNU General Public License on the World Wide Web
// at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.HashMap;

/**
 * Class MailboxManager provides the server program's mailbox manager in the
 * Network Go Game. The mailbox manager keeps track of all view proxy objects,
 * reads all incoming datagrams, and forwards each datagram to the appropriate
 * view proxy.
 *
 * @author  Alan Kaminsky
 * @version 28-Sep-2013
 */
public class MailboxManager
	{

// Hidden data members.

	private DatagramSocket mailbox;

	private HashMap<SocketAddress,ViewProxy> proxyMap =
		new HashMap<SocketAddress,ViewProxy>();

	private byte[] payload = new byte [128]; /* CAREFUL OF BUFFER LENGTH */

	private SessionManager sessionManager = new SessionManager();

// Exported constructors.

	/**
	 * Construct a new mailbox manager.
	 *
	 * @param  mailbox  Mailbox from which to read datagrams.
	 */
	public MailboxManager
		(DatagramSocket mailbox)
		{
		this.mailbox = mailbox;
		}

// Exported operations.

	/**
	 * Receive and process the next datagram.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void receiveMessage()
		throws IOException
		{
		DatagramPacket packet = new DatagramPacket (payload, payload.length);
		mailbox.receive (packet);
		SocketAddress clientAddress = packet.getSocketAddress();
		ViewProxy proxy = proxyMap.get (clientAddress);
		if (proxy == null)
			{
			proxy = new ViewProxy (mailbox, clientAddress);
			proxy.setViewListener (sessionManager);
			proxyMap.put (clientAddress, proxy);
			}
		if (proxy.process (packet)) // Returns true to discard view proxy
			{
			proxyMap.remove (clientAddress);
			}
		}

	}