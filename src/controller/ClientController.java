package controller;

import networking.Message;
import networking.NetworkClient;
import model.Board;
import model.ClientModel;
import model.CombatMoves;
import model.Player;
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
		
		state = GameState.CHOOSE_CHARACTER;
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
		
		if(message == null){
			
		}
		
		if(message instanceof String){
			String text = ((String) message );
			if(text.equalsIgnoreCase("NC CLOSED")){
				model.addMessage("Network Client Has Closed");
			
				//TODO SHUT THE CLIENT DOWN!!!!
				
				
				//System.out.println("Your NetworkClient has CLOSED.");
			}
			if(text.equalsIgnoreCase("SEND MOVES")){
				state = GameState.CHOOSE_PLAYS;
				model.addMessage("Please Select Your Moves");
				//System.out.println("Your NetworkClient has CLOSED.");
			}
			if(text.equalsIgnoreCase("SEND COMBAT")){
				state = GameState.CHOOSE_COMBATTARGET;
				model.addMessage("Please Select Combat TARGET");
				//System.out.println("Your NetworkClient has CLOSED.");
			}
			if(text.equalsIgnoreCase("SEND COMBATMOVES")){
				state = GameState.CHOOSE_COMBATMOVES;
				model.addMessage("Please Select Combat Actions");
				//System.out.println("Your NetworkClient has CLOSED.");
			}
			if(text.equalsIgnoreCase("CHARACTER SELECT")){
				state = GameState.CHOOSE_CHARACTER;
				model.addMessage("START CHARACTER SELECT");
				//System.out.println("Your NetworkClient has CLOSED.");
			}else{
				model.addMessage((String) message);
			}
		}
		if(message instanceof Board){
			model.setBoard((Board) message);
			model.addMessage("New Board Recieved");
		}
		if(message instanceof Player){
			model.setPlayer((Player) message);
			model.addMessage("New Player Recieved");
		}
		
		if(view != null){
			view.update();
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
		Object[] mes = {p1, p2, p3, p4};
		network.send(new Message(MessageType.ACTIVITIES, mes));
		System.out.println("Sent "+p1+p2+p3+p4);
		this.view.update();
	}
	
	/**
	 * Should send the selected combat moves to the server
	 * @param a the player's attack
	 * @param d the player's defense
	 * @param m the player's maneuver
	 * @param aF the player's attack fatigue
	 * @param mF the player's maneuver fatigue
	 */
	//TODO CREATES COMBAT MOVE OBJECT, SENDS IT IN THE MESSAGE
	public void handleCombatMoves(Attacks a, Defenses d, Maneuvers m, int aF, int mF){
		Object[] mes = new Object[0];
		mes[0] = new CombatMoves(a, aF, m, mF, d);
		network.send(new Message(MessageType.COMBAT_MOVES, mes));
		System.out.println("Sent COMBATMOVES");
		this.view.update();
	}
	
	/**
	 * Should parse the move and send it to the server
	 * @param location where the player wants to move, in the form "<TileName> clearing <clearingNumber>"
	 */
	
	//TODO NEEDS TO BE CHANGED TO A BETTER FORMAT?
	
	public void handleMoveSelection(String location){
		Object[] mes = {location};
		network.send(new Message(MessageType.ACTIVITIES, mes));
		System.out.println("Sent "+location);
		this.view.update();
	}
	
	/**
	 * Should parse the weapon and send it to the server
	 * @param weapon the weapon to alert, in the form "<WeaponName> <active>"
	 */
	public void handleAlertSelection(String weapon){
		Object[] mes = {weapon};
		network.send(new Message(MessageType.ACTIVITIES, mes));
		System.out.println(weapon);
		this.view.update();
	}
	
	/**
	 * Should verify that character is available and then set character
	 * @param name the name of the selected character
	 */
	public void handleCharacterSelection(CharacterName name){
		Object[] mes = {name};
		network.send(new Message(MessageType.CHARACTER_SELECT, mes));
		System.out.println("Sent character select");
		this.view.update();
	}
	
	/**
	 * Handles the selection of a target for combat
	 * @param name the CharacterName of the player to target
	 */
	public void handleTargetSelection(CharacterName name){
		Object[] mes = new Character[0];
		mes[0] = name;
		network.send(new Message(MessageType.COMBAT_TARGET, mes));
		System.out.println("Sent target selection");
		this.view.update();
	}
	
	/**
	 * Running this method will trigger the process of creating a MagicRealm client.
	 * @param args Command line arguments, likely to remain unused
	 */
	public static void main(String args[]){
		ClientController control = new ClientController();		//Instantiate the controller
		control.run();											//start the controller
	}	

}
