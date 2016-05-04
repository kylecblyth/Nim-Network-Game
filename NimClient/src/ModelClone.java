//******************************************************************************
//
// File:    ModelClone.java
// Package: ---
// Unit:    Class ModelClone.java
//
//******************************************************************************

import java.io.IOException;
import java.util.HashMap;

/**
 * Class ModelClone keeps a copy of application state during the Nim game
 *
 * @author  Kyle Blyth
 * @version 02-Dec-2015
 */
public class ModelClone
	implements ModelListener 
	{
	
// Hidden data members
	
	private int myId;
	private HashMap<Integer, String> players = 
			new HashMap<Integer, String>();
	private boolean newGame = false;
	
	private ModelListener modelListener;

// Exported constructors

	/**
	 * Construct a new model clone
	 *
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public ModelClone() 
		throws IOException {}

	/**
	 * Set this model listener
	 * 
	 * @param  view  the view object listening
	 */
	public void setModelListener(NimUI view) {
		this.modelListener = view;
	}
	
	/**
	 * Disables the heaps on initial join and
	 * reports id has been set
	 * 
	 * @param  pid  the player's id
	 * @throws IOException 
	 */
	public void idSet
		(int pid) 
		throws IOException 
		{
		this.myId = pid;
		modelListener.idSet(pid);
	}

	/**
	 * Report a new player name
	 * 
	 * @param  pid    The player's id
	 * @param  pName  The player's name
	 * @throws IOException 
	 */
	public void nameSet
		(int pid, 
		 String playerName) 
		throws IOException 
		{
		players.put(pid, playerName);
		modelListener.nameSet(pid, playerName);
	}

	/**
	 * Set initial player scores
	 * 
	 * @param isMe   Is this player me?
	 * @param s      The score
	 * @param pName  Name of the player who scored
	 * @throws IOException 
	 */
	public void scoreSet
		(boolean isMe, 
		 int s, 
		 int pid, 
		 String playerName) 
		throws IOException 
		{
		modelListener.scoreSet(pid == myId, s, pid, players.get(pid));
	}

	/**
	 * Report that markers were removed from a heap
	 *
	 * @param  h  Id of the heap removed from
	 * @param  m  The number of markers removed
	 * 
	 * @throws IOException 
	 */
	public void heapSet
		(int h, 
		int m) 
		throws IOException 
		{
		modelListener.heapSet (h, m);
		newGame = false;
	}

	/**
	 * Enables or disables the heaps based on who's turn it is
	 * 
	 * @param  pid  The id of whose turn it is
	 */
	public void turnSet
		(int pid) 
		{
		if(pid == myId) {
			modelListener.enableHeaps();
		} else {
			modelListener.disableHeaps();
		}
		
		if(newGame) {
			//reset heaps and notify UI
			modelListener.newGameClicked();
			newGame = false;
			modelListener.clearWinner();
		}
		
		newGame = true;
	}

	/**
	 * Reports a winner
	 * 
	 * @param  pid  Player Id of the winner
	 */
	public void winnerSet(int pid) {
		newGame = true;
		modelListener.setWinner(players.get(pid));
	}

	/**
	 * Removes the winner text from the UI
	 */
	public void clearWinner() {
	}

	/**
	 * Reports a session is full and a new game can be started
	 */
	public void enableNewGame() {
		modelListener.enableNewGame();
	}

	/**
	 * Report that the New Game button was clicked
	 */
	public void newGameClicked() {
	}

	/**
	 * Enables all heaps
	 */
	public void enableHeaps() {}

	/**
	 * Disables all heaps
	 */
	public void disableHeaps() {}
	
	/**
	 * Shows winner text on the UI
	 * @param playerName  The name of the winner
	 */
	public void setWinner
		(String playerName) {}

	/**
	 * Tell clients to quit the session
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void gameQuit() 
		throws IOException {}
}
