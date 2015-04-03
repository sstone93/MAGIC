package networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import controller.ClientController;
import utils.Config;

/**
 * This class is the client side networking for a game of MagicRealm.
 * @author Nick
 * The NetworkEntity extension is to allow NetworkThreads to work
 */
public class NetworkClient extends NetworkEntity  implements Runnable{
	
	private Socket socket = null;
	
	/**
	 * Constructor for a NetworkClient
	 * @param control This is a reference to the controller who created this object.
	 */
	public NetworkClient(ClientController control, String defaultHost){
		this.controller = control;		//stores the parent ServerController
		
		System.out.println("Client is Establishing connection. Please wait...");
		
		try {
			this.socket = new Socket(defaultHost, Config.DEFAULT_PORT);
			int ID = socket.getLocalPort();
			System.out.println(ID + ": Connected to server: " + socket.getInetAddress());
			System.out.println(ID + ": Connected to portid: " + socket.getLocalPort());
			this.start();
		} catch (UnknownHostException uhe) {
			System.out.println("UnknownHost Exception in creation of NetworkClient");
		} catch (IOException ioe) {
			System.out.println("IO Exception in creation of NetworkClient");
		}
	}
	
	/**
	 * This needs to be here to enable the threading, but it doesnt actually do anything, all action is taken during the constructor
	 */
	public void run(){
	}
	
	public void send(Object o){
		clients[0].send(o);
	}
	
	/**
	 * This method is called to start up the client.
	 */
	public void start() {
		clients[0] = new NetworkThread(this, socket, socket.getLocalPort());
		try {
			clients[0].open();
			clients[0].start();
		} catch (IOException e) {
			System.out.println("IO Error in NetworkClient start");
		}
		if (thread == null){
			thread = new Thread(this);
			thread.start();
			System.out.println("Client started: " + socket + ": "+thread.getId());
		}
	}
	
	/**
	 * Overrided remove method. This removes a thread from the list and properly shuts it down (Client Specific).
	 * @param ID The ID# of the thread being shutdown.
	 */
	public synchronized void remove(int ID){
		this.stop();
	}
	
	/**
	 * This is used to stop the Network Server
	 */
	public void stop(){
		System.out.println("Client: Begin Shutdown.");
		NetworkThread toTerminate = clients[0];
		
		if(toTerminate != null){
			try{
				toTerminate.close();	//This SHOULD properly terminate the thread being shut down.
				toTerminate = null;		//allows the garbage collector to have it???
			} catch (IOException ioe) {
				toTerminate = null;		//allows the garbage collector to have it???
				System.out.println("Error closing thread: " + ioe);
			}
		}

		System.out.println("Client: Connection Closed.");
		if (thread != null){									//stops the endless loop
			thread = null;
		}
		System.out.println("Client: Loop Thread Destroyed. See ya.");
		controller.handle(0, "NetworkClient has Closed!");		//due to random null pointer shit, dont actually use a valid port here
	}
}
