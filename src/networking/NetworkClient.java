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
		client = new ClientThread(this, socket);//currently lost
		client.close();//also lost
	}
	
	//TODO UNDERSTAND WHY WE NEED TO THREAD LIKE THIS
	/**
	 * This method is called to start up the client.
	 */
	public void start() {
		if (thread == null){
			thread = new Thread(this);
			thread.start();
			System.out.println("Client started: " + socket + ": "+thread.getId());
		}
	}
	
	//TODO Understand this
	/**
	 * This is used to stop the Network Server
	 */
	public void stop(){
		try {
			if (thread != null){
				thread = null;
			}
			if(socket != null)
				socket.close();
			socket = null;
			System.out.println("NetworkServer was stopped.");
		} catch (IOException ioe){
			System.out.println("IO Exception in NetworkClient Stop Method");
		}
	}
}
