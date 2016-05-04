//******************************************************************************
//
// File:    ViewListener.java
// Package: ---
// Unit:    Interface ViewListener.java
//
//******************************************************************************

import java.io.IOException;

/**
 *  Interface for an object triggered by the NimUI view object
 *
 * @author  Kyle Blyth
 * @version 02-Dec-2015
 */
public interface ViewListener
	{

	/**
	 * Join the given session
	 *
	 * @param  playerName  The name of the player joining
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void join
		(ViewProxy viewProxy,
		 String playerName)
		throws IOException;

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
		throws IOException;

	/**
	 * Start a new game
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void newGame()
		throws IOException;

	/**
	 * Quit the session
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred
	 */
	public void quit()
		throws IOException;
}