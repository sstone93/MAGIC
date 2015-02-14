package controller;

/**
 * Abstract Class used to make other code simplifications work
 * @author Nick
 */
public abstract class Handler {
	
	//TODO Make it so that the controller MUST overwrite this method
	/**
	 * Generic handle method, THIS IS INTENDED TO BE OVERWRITTEN BY THE CONTROLLER
	 * @param ID
	 * @param message
	 */
	public void handle(int ID, Object message){}

}
