package controller;

import utils.Config;
import networking.NetworkServer;
import model.ServerModel;

public class ServerController {
	
	public ServerModel model;
	public NetworkServer network;
	
	public ServerController(){

		//instanciate the network, gives the network object a reference to this controller
		this.network = new NetworkServer(Config.DEFAULT_PORT, this);

		//instanciate the model
		this.model = new ServerModel();
		
	}
	
	/**
	 * This is the method that 
	 * @param ID Is this needed? TBD.
	 * @param message
	 */
	public void handle(int ID, Object message){
		
	}
	
	public void run(){
		
	}
	
	public static void main(String args[]){
		
		//run this to startup the client side of the program
		
		//instanciate the client, enjoy the game
		ServerController control = new ServerController();
		control.run();

	}	

}
