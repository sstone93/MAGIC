package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controller.ServerController;
import utils.Config;

public class NetworkServer implements Runnable{

	int clientCount= 0;
	private Thread thread = null;
	private ServerSocket server = null;
	public ServerThread clients[] = new ServerThread[Config.MAX_CLIENTS];
	public ServerController controller = null;
	
	public NetworkServer(int port, ServerController control){

		//stores the parent ServerController
		this.controller = control;
		
		try{
			System.out.println("Binding to port " + port + ", please wait....");
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			start();
		} catch (IOException ioe) {
			//Trace.exception(ioe);
		}
	}
	
	public int getClientID(int pos){
		return this.clients[pos].getID();
	}
	
	@Override
	public void run() {
		while (thread != null) {
			try{
				System.out.println("Waiting for a client ... ");
				addThread(server.accept());
			} catch (IOException ie){
				System.out.println("Sever: Accepting Client Error");
				//Trace.exception(ie);
			}
		}
	}

	private void addThread(Socket socket){
		if (clientCount < clients.length){
			System.out.println("Client acceptied: " + socket);
			clients[clientCount] = new ServerThread(this, socket);
			
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe){
				System.out.println("Error opening threads: ");
				//Trace.exception(ioe);
			}
			
			//checks to see if the clientCount has been reached and the game should start
			//if(clientCount == Config.MAX_CLIENTS){
				//this.model = new ServerLogic(this);
			//}		
			
		} else {
			System.out.println("Client refused: maximum " + clients.length + " reached.");
		}
		
	}
	
	public synchronized void handle(int ID, Object input) {

		System.out.println("New Message Recieved: " + input);

		//handles random noise
		if (input == null)
			return;

		//CURRENTLY HERE SO I CAN RE-USE THIS CODE LATER
		
		/*if(input instanceof String){

			//this handles returing score error messages
			System.out.println("Sending String Message to " + ID);
			int pos = findClient(ID);
			if(pos != -1){
				clients[pos].send(input);
			}*/

		//send message to controller to be handled
		System.out.println("Controller Told to Handle");
		this.controller.handle(ID, input);
		
	}
	
	public synchronized void remove(int ID){
		int pos = findClient(ID);
		if (pos >= 0) {
			ServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);
			if(pos < clientCount -1){
				for (int i = pos + 1; i < clientCount; i++) {
					clients[i - 1] = clients[i];
				}
			}
			
			clientCount--;
			try{
				toTerminate.close();
				toTerminate = null;
			} catch (IOException ioe) {
				toTerminate = null;
				System.out.println("Error closing thread: " + ioe);
			}
		}
	}
	
	private int findClient(int ID) {
		for (int i=0; i< clientCount; i++) {
			if(clients[i].getID() == ID){
				return i;
			}
		}
		return -1;
	}
	
	public void start() {
		if (thread == null){
			thread = new Thread(this);
			thread.start();
			System.out.println("Server started: " + server + ": "+thread.getId());
		}
	}
	
	public void stop(){
		try{
			if (thread != null){
				thread.join();
				thread = null;
			}
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}
