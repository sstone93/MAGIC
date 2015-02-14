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
	
	//private ClientController controller = null;
	private Socket socket = null;
	
	/**
	 * 
	 * @param control
	 */
	public NetworkClient(ClientController control){
		this.controller = control;		//stores the parent ServerController
		
		System.out.println("Client is Establishing connection. Please wait...");
		
		try {
			this.socket = new Socket(Config.DEFAULT_HOST, Config.DEFAULT_PORT);
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
	 * 
	 */
	public void run(){
	}
	
	//TODO UNDERSTAND WHY WE NEED TO THREAD LIKE THIS
	/**
	 * This method is called to start up the client.
	 */
	public void start() {
		clients[0] = new NetworkThread(this, socket);		//TODO
		try {
			clients[0].open();
			clients[0].start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (thread == null){
			thread = new Thread(this);
			thread.start();
			System.out.println("Client started: " + socket + ": "+thread.getId());
		}
	}
	
	//try to detect if it is a client who was removing their only thread (commiting suicide)
	//TODO THE BELOW STOP METHOD IS NEVER CALLED!
	//TODO Run is still blank?????
	//TODO THE ID NUMBERS DONT LINE UP, WHY? DEFUALT PORT???
	
	/**
	 * This removes a thread from the list and properly shuts it down
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
		try{
			toTerminate.close();	//TODO This SHOULD properly terminate the thread being shut down.
			toTerminate = null;		//allows the garbage collector to have it???
		} catch (IOException ioe) {
			toTerminate = null;		//allows the garbage collector to have it???
			System.out.println("Error closing thread: " + ioe);
		}

		System.out.println("Client: Connection Closed.");
		if (thread != null){									//stops the endless loop
			thread = null;
		}
		System.out.println("Client: Loop Thread Destroyed. See ya.");
	}
}
