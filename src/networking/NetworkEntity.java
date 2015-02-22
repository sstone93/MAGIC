package networking;

import java.io.IOException;

import utils.Config;
import controller.Handler;

public abstract class NetworkEntity {
	
	protected int clientCount= 0;
	protected Thread thread = null;
	protected Handler controller = null;	//ensures the handle method is avilable
	protected NetworkThread clients[] = new NetworkThread[Config.MAX_CLIENTS];	//list of all threads owned by this entity
	
	/**
	 * Generic Handle method used by both clients and servers
	 * @param ID the ID of the thread transmitting the mesage
	 * @param input the message being transmitted
	 */
	public synchronized void handle(int ID, Object input) {
		System.out.println("New Message Recieved: " + input);
		if (input == null)		//handles random noise
			return;
		this.controller.handle(ID, input);	//send message to controller to be handled
		System.out.println("Controller Told to Handle");
	}
	
	/**
	 * Porvides the controller with the ID (port number) of clients to distinguish them
	 * @return the ID of every client in the game
	 */
	public int[] getIDs(){
		int[] id = new int[Config.MAX_CLIENTS];
		for(int i=0; i< Config.MAX_CLIENTS;i++){
			id[i] = clients[i].getID();
		}
		return id;
	}
	
	/**
	 * This function finds the position of a thread in the threads array based on it's ID
	 * @param ID ID# of the thread you are looking for
	 * @return Position of the thread in the array
	 */
	private int findClient(int ID) {
		for (int i=0; i < clientCount; i++) {	//cycles the list, looking for an ID match
			if(clients[i].getID() == ID){
				return i;
			}
		}
		return -1;						//if no client was found with that ID, return -1
	}
	
	/**
	 * This removes a thread from the list and properly shuts it down
	 * @param ID The ID# of the thread being shutdown.
	 */
	public synchronized void remove(int ID){
		int pos = findClient(ID);		//converts the ID into a position in the array
		if (pos >= 0) {					//ensure the given position is actually valid
			NetworkThread toTerminate = clients[pos];			//grabs the thread being shutdown
			System.out.println("Removing client thread " + ID + " at " + pos);
			for (int i = pos + 1; i < clientCount; i++) {		//resorts the array of threads so that
				clients[i - 1] = clients[i];					//there are no open spaces in the middle
			}
			clientCount--;						//Update the current number of threads
			try{
				toTerminate.close();			//This SHOULD properly terminate the thread being shut down.
				toTerminate = null;				//allows the garbage collector to have it???
			} catch (IOException ioe) {
				toTerminate = null;				//allows the garbage collector to have it???
				System.out.println("Error closing thread: " + ioe);
			}
		}
	}

}
