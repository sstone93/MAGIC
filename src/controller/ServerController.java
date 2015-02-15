package controller;

import networking.NetworkServer;
import model.ServerModel;

/**
 * Main controller class for the server
 * @author Nick
 */
public class ServerController extends Handler{
	
	public ServerModel model;
	public NetworkServer network;
	
	/**
	 * Constructor for a ServerController
	 */
	public ServerController(){

		//instanciate the network, gives the network object a reference to this controller
		this.network = new NetworkServer(this);
		System.out.println("Network Server Created.");

		//instanciate the model
		this.model = new ServerModel();
		System.out.println("Server Model Created.");
	}
	
	/**
	 * This is the method that handles incomming messages from the networking components
	 * @param ID The ID of the client sending the message
	 * @param message the contents of the message
	 */
	public void handle(int ID, Object message){
		if(message instanceof String){
			String text = ((String) message );
			if(text.equalsIgnoreCase("START GAME")){
				System.out.println("Server Controller told to START THE GAME.");
			}
		}
	}
	
	/**
	 * (If actually needed, this will actually start up the client, bring it to life.
	 */
	public void run(){
		//start the network up
		//populate the model as needed
	}
	
	public void startGame(){
		
	}
	
	/**
	 * Running this method will trigger the process of creating a MagicRealm server.
	 * @param args Command line arguments, likely to remain unused
	 */
	public static void main(String args[]){
		ServerController control = new ServerController();		//instanciate the controller
		control.run();											//start the controller
	}	

}
