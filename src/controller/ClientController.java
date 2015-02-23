package controller;

import networking.NetworkClient;
import model.ClientModel;
import utils.Utility.*;
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
	public GameState state;
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
		
		state = GameState.CHOOSE_PLAYS;
		//instantiate the view
		this.view = new View(this);
		view.setVisible(true);									//Activates the GUI
		System.out.println("View Successfully Created.");
		view.update();
		
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
		if(message instanceof String){
			String text = ((String) message );
			if(text.equalsIgnoreCase("NC CLOSED")){
				System.out.println("Your NetworkClient has CLOSED.");
			}
		}
	}
	
	/**
	 * Should send the selected plays to the server.
	 * @param p1 the player's first play
	 * @param p2 the player's second play
	 * @param p3 the player's third play
	 * @param p4 the player's fourth play
	 */
	public void handlePlaysRecorded(Actions p1, Actions p2, Actions p3, Actions p4){
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		this.view.update();
	}
	
	/**
	 * Should send the selected combat moves to the server
	 * @param a the player's attack
	 * @param d the player's defense
	 * @param m the player's maneuver
	 */
	public void handleCombatMoves(Attacks a, Defenses d, Maneuvers m){
		System.out.println(a);
		System.out.println(d);
		System.out.println(m);
		this.view.update();
	}
	
	/**
	 * Should parse the move and send it to the server
	 * @param location where the player wants to move, in the form "<TileName> clearing <clearingNumber>"
	 */
	public void handleMoveSelection(String location){
		System.out.println(location);
		this.view.update();
	}
	
	/**
	 * Should parse the weapon and send it to the server
	 * @param weapon the weapon to alert, in the form "<WeaponName> <active>"
	 */
	public void handleAlertSelection(String weapon){
		System.out.println(weapon);
		this.view.update();
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
