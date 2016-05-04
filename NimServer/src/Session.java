//******************************************************************************
//
// File:    Session.java
// Package: ---
// Unit:    Class ModelProxy.java
//
//******************************************************************************

/**
 * Class Session models a session on the Nim server. Each session knows whether
 * it is full or not, and contains a model associated with itself.
 * 
 * @author  Kyle Blyth
 * @version 01-Dec-2015
 */
public class Session {
	private boolean isFull; // if the session has two players or not
	private NimModel model;
	
    /**
     * Returns whether or not this session contains two players
     * 
     * @return  isFull  true if session is full, else false
     */
	public boolean isFull() { return isFull; }
	
	/**
	 * Returns the model this session is using
	 * 
	 * @return  model  the model associated with this session
	 */
	public NimModel getModel() { return model; }
	
	/**
	 * Marks this session as full
	 */
	public void setFull() { isFull = true; }
	
	/**
	 * Constructor
	 * 
	 * @param  model  the model associated with this session
	 */
	public Session(NimModel model) {
		this.model = model;
	}
}
