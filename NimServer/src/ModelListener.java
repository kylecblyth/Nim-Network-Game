import java.io.IOException;

//******************************************************************************
//
// File:    ModelListener.java
// Package: ---
// Unit:    Interface ModelListener.java
//
//******************************************************************************


/**
 * Interface for an object triggered by a Nim model object
 *
 * @author  Kyle Blyth
 * @version 02-Dec-2015
 */
public interface ModelListener {

	/**
	 * Disables the heaps on initial join and
	 * reports id has been set
	 * 
	 * @param  pid  the player's id
	 */
	public void idSet
		(int pid)
		throws IOException;

	/**
	 * Report a new player name
	 * 
	 * @param  pid    The player's id
	 * @param  pName  The player's name
	 */
	public void nameSet
		(int pid, String playerName)
		throws IOException;
	
	/**
	 * Set initial player scores
	 * 
	 * @param isMe   Is this player me?
	 * @param s      The score
	 * @param pName  Name of the player who scored
	 */
	public void scoreSet
		(boolean isMe,
		 int s,
		 int pid,
		 String playerName)
		 throws IOException;
	
	/**
	 * Report that markers were removed from a heap
	 *
	 * @param  h  Id of the heap removed from
	 * @param  m  The number of markers removed
	 */
	public void heapSet
		(int h,
		 int m)
		 throws IOException;
	
	/**
	 * Enables or disables the heaps based on who's turn it is
	 * 
	 * @param  pid  The id of whose turn it is
	 */
	public void turnSet(int pid)
		throws IOException;

	/**
	 * Reports a winner
	 * 
	 * @param  pid  Player Id of the winner
	 */
	public void winnerSet
		(int pid)
		throws IOException;
	
	/**
	 * Removes the winner text from the UI
	 */
	public void clearWinner();
	
	/**
	 * Reports a session is full and a new game can be started
	 */
	public void enableNewGame();
	
	/**
	 * Report that the New Game button was clicked
	 */
	public void newGameClicked();
	
	/**
	 * enables all heaps
	 */
	public void enableHeaps();
	
	/**
	 * disables all heaps
	 */
	public void disableHeaps();
	
	/**
	 * Shows winner text on the UI
	 * @param playerName  The name of the winner
	 */
	public void setWinner(String playerName);

	/**
	 * Tell clients to quit the session
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void gameQuit() 
		throws IOException;
}