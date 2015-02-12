package controller;

import java.io.IOException;

import networking.NetworkClient;
import model.ClientModel;
import utils.Config;
import view.View;

public class ClientController {
	
	public View view;
	public ClientModel model;
	public NetworkClient network;
	
	public ClientController(){
		
		//run this to SETUP the client side of the program
		
		//instantiate the view
			//give the view a reference to this controller
		
		//this.view = new View(ClientController this);
		
		
				//view becomes the user interface?
				
		//instanciate the networking
				
		//need to give the networkclient a reference to this controller
		network = null;

		try{
			this.network = new NetworkClient(Config.DEFAULT_HOST, Config.DEFAULT_PORT);
		} catch (IOException e){
			//System.exit(0);
			System.out.println("Failed to create NetworkClient");
		}
				
		//instantiate the model
		this.model = new ClientModel();
		
	}
	
	private void run(){
		
		//this will actually start up the client, bring it to life.
		
		//statup the view (all information/errors now displayed there)
		//start the network up
			//populate the model as needed
		
	}
	
	public void handle(Object message){
		
	}
	
	public static void main(String args[]){
		
		//instanciate the client, enjoy the game
		ClientController control = new ClientController();
		control.run();

	}	

}
