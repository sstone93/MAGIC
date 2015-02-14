package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controller.ServerController;
import utils.Config;

/**
 * This class is the server side for a game of MagicRealm.
 * @author Nick
 * The NetworkEntity extension is to allow NetworkThreads to work
 */
public class NetworkServer extends NetworkEntity implements Runnable{

	private ServerSocket server = null;
	//private ServerController controller = null;
	
	/**
	 * Contructor for a NetworkServer
	 * @param control The controller that created this Server.
	 */
	public NetworkServer(ServerController control){
		this.controller = control;		//stores the parent ServerController
		try{
			System.out.println("Server is binding to port " + Config.DEFAULT_PORT + ", please wait....");
			server = new ServerSocket(Config.DEFAULT_PORT);			//Server Port Hard Coded
			server.setReuseAddress(true);							//stops painfull testing issues
			start();
		} catch (IOException ioe) {
			System.out.println("IO Exception in NetworkServerCreation.");
		}
	}

	/**
	 * This function adds a new thread to the list of threads
	 * @param socket The client socket being communicated with
	 */
	private void addThread(Socket socket){
		if (clientCount < clients.length){							//if there is room for another client...
			System.out.println("Client acceptied: " + socket);
			clients[clientCount] = new NetworkThread(this, socket);
			try {
				clients[clientCount].open();						//creates a new thread to deal with the new client
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe){
				System.out.println("Server Error adding new thread: ");
			}
			//checks to see if the clientCount has been reached and the game should start
			//TODO HARD CODING TO TEST PROPER CLOSEDOWN OF NETWORK
			if(clientCount == Config.MAX_CLIENTS){
				//-----------------
				for (int i=0; i< clientCount; i++) {	//closes all threads individually, not sure if needed
					this.remove(clients[i].getID());
				}
				//----------------
				this.stop();
			}		
		} else {
			System.out.println("Client refused: maximum " + clients.length + " reached.");
		}
	}
	
	/**
	 * Running this causes the server to accept incomming requests. and handle them via. the addThread method
	 */
	public void run() {
		while (thread != null) {				//runs until the stop method is run
			try{
				System.out.println("Waiting for clients ... ");
				addThread(server.accept());		//spam accept requests, handles them via add thread
			} catch (IOException ie){
				System.out.println("Sever: IO Exception in NetworkServer run method");
			}
		}
	}
	
	//TODO UNDERSTAND WHY WE NEED TO THREAD LIKE THIS
	/**
	 * This method is called to start up the server.
	 */
	public void start() {
		if (thread == null){
			thread = new Thread(this);
			thread.start();
			System.out.println("Server started: " + server + ": "+thread.getId());
		}
	}
	
	//TODO Understand this
	/**
	 * This is used to stop the Network Server
	 */
	public void stop(){
		try{
			if (thread != null){		//if the thread exists, merge it back
				thread.join();
				thread = null;			//let it be garbage collected?
			}
			System.out.println("NetworkServer was stopped.");
		} catch (InterruptedException e){
			System.out.println("Interrupted Exception in NetworkServer Stop Method");
		}
	}
}
