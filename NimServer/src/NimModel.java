//******************************************************************************
//
// File:    NimModel.java
// Package: ---
// Unit:    Class NimModel.java
//
//******************************************************************************

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class NimModel provides the server-side model object for Nim
 *
 * @author  Kyle Blyth
 * @version 02-Dec-2015
 */
public class NimModel
	implements ViewListener
	{

// Hidden data members

	private static final int NUMHEAPS = 3;
	private int[] heaps = {0, 0, 0};
	private int[] playerScores = {0, 0};
	private int turn;
	private SessionManager sessionManager;
	
	private ArrayList<ModelListener> listeners =
		new ArrayList<ModelListener>();

// Exported constructors

	/**
	 * Construct a new Nim model
	 */
	public NimModel() {
		// initialize heaps
		for(int i = 0; i < NUMHEAPS; i++) {
			heaps[i] = 3 + i;
		}
	}

// Exported operations

	/**
	 * Add the given model listener to this Nim model
	 *
	 * @param  modelListener  Model listener
	 */
	public synchronized void addModelListener
		(ModelListener modelListener)
		{
		// Record listener.
		listeners.add (modelListener);
	}
	
	/**
	 * Set the session manager for this model
	 * 
	 * @param  s  The session manager
	 */
	public synchronized void setSessionManager
		(SessionManager s)
		{
		this.sessionManager = s;
	}

	/**
	 * Join the given session
	 *
	 * @param  proxy       Reference to view proxy object
	 * @param  playerName  Name of the player joining
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void join
		(ViewProxy proxy,
	     String playerName)
		throws IOException
		{
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
	public synchronized void removeMarker
		(int h,
		 int m)
		throws IOException 
		{
		// Update heaps
		heaps[h] = heaps[h] - m;
		
		// switch player turn
		turn = 1 - turn;

		// Report update to all clients
		for(int i = 0; i < 2; i++) {
			listeners.get(i).heapSet (h, m);
			listeners.get(i).turnSet(turn);
		}
		
		// check for winner
		int res = 0;
		
		for(int i : heaps) {
			res += i;
		}
		
		if(res == 0) {
			playerScores[1-turn]++;
			// Report win to all clients
			for(int i = 0; i < 2; i++) {
				listeners.get(i).winnerSet(1 - turn);
				listeners.get(i).scoreSet(false, 1 - turn, playerScores[1 - turn], "");
			}
			return;
		}
	}

	/**
	 * Start a new game
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public synchronized void newGame()
		throws IOException
		{
			// set heaps
			for(int i = 0; i < 3; i++) {
				heaps[i] = 3 + i;
			}
			// Report update to all clients.
			Iterator<ModelListener> iter = listeners.iterator();
			while (iter.hasNext()) {
				ModelListener listener = iter.next();
				turn = 0;
				listener.turnSet(0);
			}
		}

	/**
	 * Quit the session
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public synchronized void quit() {
		// tell clients to quit
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.gameQuit();
			} catch (IOException exc) {
				// Client failed, stop reporting to it.
				iter.remove();
			}
		}
		
		// quit this session
		sessionManager.removeSession(this);
	}

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
	public synchronized void setName
		(ViewProxy proxy, int playerId, String playerName) 
		throws IOException 
		{
		// tell client to set name
		proxy.nameSet(playerId, playerName);
	}
	
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
	public synchronized void setId
		(ViewProxy proxy, int playerId, String playerName) 
		throws IOException 
		{
		setName(proxy, playerId, playerName);
		proxy.idSet(playerId);
	}

	/**
	 * Set the current turn
	 * 
	 * @param  playerId  the player whose turn it is
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public synchronized void setTurn
		(int playerId) 
		throws IOException 
		{
		// tell clients the turn
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			listener.turnSet(playerId);
		}
	}
}