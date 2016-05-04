//******************************************************************************
//
// File:    NimUI.java
// Package: ---
// Unit:    Class NimUI.java
//
// This Java source file is copyright (C) 2015 by Alan Kaminsky. All rights
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Class NimUI provides the user interface for the Nim game
 *
 * @author  Alan Kaminsky
 * @author  Kyle Blyth
 * @version 02-Dec-2015
 */
public class NimUI implements ModelListener {

// Hidden data members.

	private static final int NUMHEAPS = 3;
	private static final int NUMOBJECTS = 5;
	private static final int GAP = 10;
	private static final int COL = 10;

	private JFrame frame;
	private HeapPanel[] heapPanel;
	private JTextField myNameField;
	private JTextField theirNameField;
	private JTextField whoWonField;
	private JButton newGameButton;
	
	ViewListener viewListener;

// Hidden constructors.

	/**
	 * Construct a new Nim UI.
	 */
	private NimUI
		(String name)
		{
		frame = new JFrame ("Nim -- " + name);
		JPanel panel = new JPanel();
		panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		frame.add (panel);
		panel.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));

		heapPanel = new HeapPanel [NUMHEAPS];
		for (int h = 0; h < NUMHEAPS; ++ h)
			{
			panel.add (heapPanel[h] = new HeapPanel (h, NUMOBJECTS));
			panel.add (Box.createHorizontalStrut (GAP));
			}

		// set Listeners
		for(int h = 0; h < NUMHEAPS; h++) {
			heapPanel[h].setListener(new HeapListener() {
				public void removeObjects(int id, int numRemoved) {
					try {
						doTake(id, numRemoved);
					} catch (IOException e) {}
				}
			});
		}
		
		// set initial heap counts
		heapPanel[0].setCount(3);
		heapPanel[1].setCount(4);
		heapPanel[2].setCount(5);

		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout (new BoxLayout (fieldPanel, BoxLayout.Y_AXIS));
		panel.add (fieldPanel);

		myNameField = new JTextField (COL);
		myNameField.setEditable (false);
		myNameField.setHorizontalAlignment (JTextField.CENTER);
		myNameField.setAlignmentX (0.5f);
		fieldPanel.add (myNameField);
		fieldPanel.add (Box.createVerticalStrut (GAP));

		theirNameField = new JTextField (COL);
		theirNameField.setEditable (false);
		theirNameField.setHorizontalAlignment (JTextField.CENTER);
		theirNameField.setAlignmentX (0.5f);
		fieldPanel.add (theirNameField);
		fieldPanel.add (Box.createVerticalStrut (GAP));

		whoWonField = new JTextField (COL);
		whoWonField.setEditable (false);
		whoWonField.setHorizontalAlignment (JTextField.CENTER);
		whoWonField.setAlignmentX (0.5f);
		fieldPanel.add (whoWonField);
		fieldPanel.add (Box.createVerticalStrut (GAP));

		newGameButton = new JButton ("New Game");
		newGameButton.setAlignmentX (0.5f);
		newGameButton.setFocusable (false);
		newGameButton.setEnabled(false);
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					doNewGame();
				} catch (IOException e1) {}
			}
				
		});
		fieldPanel.add (newGameButton);
		
		// Closing the window exits the client.
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setVisible (true);
	} /* end constructor */

// Exported operations.

	/**
	 * An object holding a reference to a Nim UI.
	 */
	private static class UIRef
		{
		public NimUI ui;
		}

	/**
	 * Construct a new Nim UI.
	 */
	public static NimUI create
		(String name)
		{
		final UIRef ref = new UIRef();
		onSwingThreadDo (new Runnable()
			{
			public void run()
				{
				ref.ui = new NimUI (name);
				}
			});
		return ref.ui;
		}
	
	/**
	 * Set the view listener for this Nim UI.
	 *
	 * @param  viewListener  View listener.
	 */
	public synchronized void setViewListener
		(ViewListener viewListener) 
		{
		this.viewListener = viewListener;
	}

	/**
	 * Disables the heaps on initial join
	 * 
	 * @throws IOException
	 *     thrown if an IOException occurs
	 */
	public synchronized  void idSet(int pid) {
		onSwingThreadDo (new Runnable() {
			public void run() {
				for(HeapPanel p : heapPanel)
					p.setEnabled (false);
			}
		});
	}
	
	/**
	 * Report a new player name
	 * 
	 * @param  pid    The player's id
	 * @param  pName  The player's name
	 */
	public synchronized  void nameSet(int pid, String pName) {
		onSwingThreadDo (new Runnable() {
			public void run() {
				if(myNameField.getText().length() == 0) { // if our name field is empty
					myNameField.setText(pName + " = 0");
				} else {
					theirNameField.setText(pName + " = 0");
				}
			}
		});
	}
	
	/**
	 * Set initial player scores
	 * 
	 * @param isMe   Is this player me?
	 * @param s      The score
	 * @param pName  Name of the player who scored
	 */
	public synchronized  void scoreSet(boolean isMe, int s, int pid, String pName) {
		onSwingThreadDo (new Runnable() {
			public void run() {
				if(isMe) {
					myNameField.setText(pName + " = " + s);
				} else {
					theirNameField.setText(pName + " = " + s);
				}
			}
		});
	}
	
	/**
	 * Report that markers were removed from a heap
	 *
	 * @param  h  Id of the heap removed from
	 * @param  m  The number of markers removed
	 */
	public synchronized  void heapSet (int h, int m) {
		onSwingThreadDo (new Runnable() {
			public void run() {
				heapPanel[h].setCount(heapPanel[h].getCount() - m);
				for(HeapPanel heap : heapPanel) heap.repaint();
				}
		});
		
	}
	
	/**
	 * Enables or disables the heaps based on who's turn it is
	 * 
	 * @param  pid  The id of the player whose turn it is
	 */
	public synchronized  void turnSet(int pid) {}
	
	/**
	 * enables all heaps
	 */
	public synchronized  void enableHeaps() {
		onSwingThreadDo (new Runnable() {
			public void run() {
				for(HeapPanel p : heapPanel)
					p.setEnabled (true);
			}
		});
	
	}
	
	/**
	 * disables all heaps
	 */
	public synchronized  void disableHeaps() {
		onSwingThreadDo (new Runnable() {
			public void run() {
				for(HeapPanel p : heapPanel)
					p.setEnabled (false);
			}
		});
	}
	
	/**
	 * Reports that a winner has been set
	 * 
	 * @param id The id of the winner
	 */
	public synchronized  void winnerSet
		(int pid) {}

	/**
	 * Sets the winner text
	 * 
	 * @param playerName The name of the winner
	 */
	public synchronized  void setWinner(String playerName) {
		onSwingThreadDo (new Runnable() {
			public void run() {
				whoWonField.setText(playerName + " wins!");
			}
		});
	}
	
	/**
	 * Removes the winner text from the UI
	 */
	public synchronized  void clearWinner() {
		onSwingThreadDo (new Runnable() {
			public void run() {
				whoWonField.setText("");
			}
		});
	}
	
	/**
	 * Reports a session is full and a new game can be started
	 */
	public synchronized  void enableNewGame() {
		onSwingThreadDo (new Runnable() {
			public void run() {
				newGameButton.setEnabled(true);
			}
		});
	}
	
	/**
	 * Report that the New Game button was clicked
	 */
	public synchronized  void newGameClicked() {
		// reset heaps
		onSwingThreadDo (new Runnable() {
			public void run() {
				for(int i = 0; i < 3; i++) {
					heapPanel[i].setCount(i + 3);
				}
				for(HeapPanel heap : heapPanel) heap.repaint();
			}
		});
	}
	
	/**
	 * Tell clients to quit the session
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void gameQuit() 
		throws IOException {}
	
// Hidden operations
	
	/**
	 * Handle heap panels
	 * 
	 * @param id  Id of the heap being taken from
	 * @param numRemoved  The number of markers being removed
	 * 
	 * @throws IOException
	 *     Thrown if an I/O error occurs
	 */
	private synchronized void doTake(int id, int numRemoved) 
		throws IOException {
		viewListener.removeMarker(id, numRemoved);
	}
	
	/**
	 * Handle new game button
	 * @throws IOException
	 */
	private synchronized void doNewGame() 
		throws IOException {
		viewListener.newGame();
	}

	/**
	 * Execute the given runnable object on the Swing thread.
	 */
	private static void onSwingThreadDo
		(Runnable task)
		{
		try
			{
			SwingUtilities.invokeAndWait (task);
			}
		catch (Throwable exc)
			{
			exc.printStackTrace (System.err);
			System.exit (1);
			}
		}
}