package controller;

import networking.NetworkClient;
import model.ClientModel;
import view.View;

/**
 * Main controller class for the client
 * @author Nick
 *
 */
public class ClientController extends Handler{
	
	public View view;
	public ClientModel model;
	public NetworkClient network;
	
	/**
	 * Constructor for a ClientController
	 */
	public ClientController(){

		//instanciate the networking
		this.network = new NetworkClient(this);
		System.out.println("NetworkClient Successfully Created.");
				
		//instantiate the model
		this.model = new ClientModel();
		System.out.println("Client Model Successfully Created.");
		
		//instantiate the view
		this.view = new View(this);
		view.setVisible(true);									//Activates the GUI
		System.out.println("View Successfully Created.");
		
	}
	
	/**
	 * (If actually needed, this will actually start up the client, bring it to life.
	 */
	private void run(){
		//statup the view (all information/errors now displayed there)
		//start the network up
		//populate the model as needed
	}
	
	/**
	 * 
	 */
	public void handle(int ID, Object message){
		
	}
	
	/**
	 * Running this method will trigger the process of creating a MagicRealm client.
	 * @param args Command line arguments, likely to remain unused
	 */
	public static void main(String args[]){
		ClientController control = new ClientController();		//instanciate the controller
		control.run();											//start the controller
	}	

}
