//******************************************************************************
//
// File:    SessionManager.java
// Package: ---
// Unit:    Class SessionManager.java
//
//******************************************************************************

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class SessionManager maintains all sessions for the Nim server
 *
 * @author  Kyle Blyth
 * @version 01-Dec-2015
 */
public class SessionManager
	implements ViewListener
	{

// Hidden data members

	private ArrayList<Session> sessions =
		new ArrayList<Session>();
	
	int oldPlayerId;
	String oldPlayerName;
	ViewProxy oldProxy;

// Exported constructors

	/**
	 * Construct a new session manager
	 */
	public SessionManager() {}

// Exported operations

	/**
	 * Join the given session
	 *
	 * @param  proxy    Reference to view proxy object
	 * @param  playerName The name of the player joining
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public synchronized void join
		(ViewProxy proxy, 
		 String playerName)
		throws IOException {
		NimModel model = null;
		int playerId = 0;
		
		// if a session is not full, populate it
		for(Session s : sessions)
			{
				if(!s.isFull()) {
					model = s.getModel();
					s.setFull();
					playerId = 1;
					break;
				}
			}
		
		// all sessions are full or there are no sessions, make a new one
		if(model == null) {
			model = new NimModel();
			model.setSessionManager(this);
			Session session = new Session(model);
			sessions.add (session);
		}
		model.addModelListener (proxy);
		proxy.setViewListener (model);
		
		// tell model to send name command
		model.setId(proxy, playerId, playerName);
		if(playerId == 1) {
			// send old client's name to new player
			model.setName(proxy, oldPlayerId, oldPlayerName);
			// send old client new player's name
			model.setName(oldProxy, playerId, playerName);
			model.setTurn(0);
		} else {
			oldPlayerId = playerId;
			oldPlayerName = playerName;
			oldProxy = proxy;
		}
	}

	/**
	 * Remove markers from a heap
	 *
	 * @param  h  Id of the heap removed from
	 * @param  m  The number of markers removed
	 * 
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void removeMarker
		(int h,
		 int m) 
		throws IOException {}

	/**
	 * Start a new game
	 * 
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void newGame() 
		throws IOException {}

	/**
	 * Quit the session
	 */
	public void quit() {}

	/**
	 * Set a player name
	 * 
	 * @param  proxy       Proxy of the client we're notifying
	 * @param  playerId    The ID of the player
	 * @param  playerName  The player's name
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void setName
		(ViewProxy proxy, 
		 int playerId, 
		 String playerName)
		throws IOException {}

	/**
	 * Set a player ID
	 * 
	 * @param  proxy       Proxy of the client we're notifying
	 * @param  playerId    The ID of the player
	 * @param  playerName  The player's name
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void setId
		(ViewProxy proxy, 
		 int playerId, 
		 String playerName) 
		throws IOException {}

	/**
	 * Set the current turn
	 * 
	 * @param  playerId  the player whose turn it is
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void setTurn
		(int playerId) 
		throws IOException {}
	
	/**
	 * Remove a session
	 * @param  m  The model of the session to remove
	 */
	public synchronized void removeSession
		(NimModel m) 
		{
		int i = -1;
		
		for(Session s : sessions) {
			if(s.getModel() == m)
			    i = sessions.indexOf(s);
		}
		try {
			sessions.remove(i);
		} catch(Exception e) { }
	}
}