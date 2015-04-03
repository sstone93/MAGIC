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
	
	/**
	 * Contructor for a NetworkServer
	 * @param control The controller that created this Server.
	 */
	public NetworkServer(ServerController control){
		this.controller = control;		//stores the parent ServerController
		try{
			System.out.println("Server is binding to port " + Config.DEFAULT_PORT + ", please wait....");
			server = new ServerSocket(Config.DEFAULT_PORT);			//Server Port Hard Coded
			System.out.println(server);
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
			clients[clientCount] = new NetworkThread(this, socket, socket.getPort());
			try {
				clients[clientCount].open();						//creates a new thread to deal with the new client
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe){
				System.out.println("Server Error adding new thread: ");
			}
			if(clientCount == Config.MAX_CLIENTS){						//checks to see if the clientCount has been reached and the game should start
				controller.handle(Config.DEFAULT_PORT, "START GAME");
			}		
		} else {
			System.out.println("Client refused: Game in progress");
		}
	}
	
	/**
	 * Send a message to all client threads
	 */
	public void broadCast(Object message){
		for(int i=0; i<clientCount; i++){
			clients[i].send(message);
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
	
	/**
	 * This is used to stop the Network Server
	 */
	public void stop(){
		System.out.println("Server: Begin Shutdown.");
		System.out.println("Not Busy");
		while (clientCount > 0){
			this.remove(clients[0].getID());	//Close all threads 1 by 1
		}
		System.out.println("Server: 	All Threads Closed.");
		if (thread != null){		//if the thread is currently exceduting
			thread = null;			//stop the thing you just waited for to stop on its own???
		}
		System.out.println("Server: Loop Thread Destroyed. Goodnight.");
	}
}
