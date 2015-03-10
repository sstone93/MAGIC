package controller;

import java.util.ArrayList;

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
	public GameState state = GameState.NULL;
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
		state = GameState.CHOOSE_CHARACTER;
		view.setVisible(true);									//Activates the GUI
		
		System.out.println("View Successfully Created.");
		view.update();//can maybe change this to view.updateNotBoard
	}
	
	/**
	 * (If actually needed, this will actually start up the client, bring it to life.
	 */
	private void run(){}
	
	/**
	 * 
	 */
	public void handle(int ID, Object message){

		boolean needsUpdate = true;
		
		if(message instanceof String){
			String text = ((String) message );
			if(text.equalsIgnoreCase("SEND MOVES")){
				state = GameState.CHOOSE_PLAYS;
				model.addMessage("Please Select Your Moves");
			}else if(text.equalsIgnoreCase("SEND COMBAT")){
				state = GameState.CHOOSE_COMBATTARGET;
				model.addMessage("Please Select Combat TARGET");
			}else if(text.equalsIgnoreCase("SEND COMBATMOVES")){
				state = GameState.CHOOSE_COMBATMOVES;
				model.addMessage("Please Select Combat Actions");
			}else if(text.equalsIgnoreCase("CHARACTER SELECT")){
				state = GameState.CHOOSE_CHARACTER;
				model.addMessage("START CHARACTER SELECT");
			}else if(text.equalsIgnoreCase("NOT ACCEPTING CHARACTER SELECT ATM")){
				state = GameState.CHOOSE_CHARACTER;
				model.addMessage("RE-START CHARACTER SELECT");
			}else{
				model.addMessage((String) message);
				view.updateMessageBox();
				needsUpdate = false; //needs to NOT trigger the update
			}
		}else if(message instanceof Board){
			model.setBoard((Board) message);
			model.addMessage("New Board Recieved");
		}else if(message instanceof Player){
			model.setPlayer((Player) message);
			model.addMessage("New Player Recieved");
		}
		
		if(view != null && needsUpdate){
			view.update();//is this needed? all of the above operations will call it?????
		}
	}
	
	/**
	 * Should send the selected plays to the server.
	 * @param p1 the player's first play
	 * @param l1 location where the player wants to move, in the form "<TileName> clearing <clearingNumber>"
	 * Assumes locations will be ignored by server if not move action
	 */
	public void handlePlaysRecorded(ActivityType t, PhaseType p, ArrayList<Object> data){
		ArrayList<Object> mes = new ArrayList<Object>();
		mes.add(new Phase()
		network.send(new Message(MessageType.ACTIVITIES, mes));
		model.addMessage("Sent activity");
		state = GameState.NULL;
		view.updateNonBoardGUI(null);//sending null rather than the player object, null checks make this safe
	}

	/**
	 * Should send the selected combat moves to the server
	 * @param a the player's attack
	 * @param d the player's defense
	 * @param m the player's maneuver
	 * @param aF the player's attack fatigue
	 * @param mF the player's maneuver fatigue
	 */
	public void handleCombatMoves(Attacks a, Defenses d, Maneuvers m, int aF, int mF){
		CombatMoves temp = new CombatMoves(a, aF, m, mF, d);
		ArrayList<Object> mes = new ArrayList<Object>();
		mes.add(temp);
		network.send(new Message(MessageType.COMBAT_MOVES, mes));
		model.addMessage("Sent COMBATMOVES");
		state = GameState.NULL;
		view.updateNonBoardGUI(null);//sending null rather than the player object, null checks make this safe
	}
	
	/**
	 * Should verify that character is available and then set character
	 * @param name the name of the selected character
	 */
	public void handleCharacterSelection(CharacterName name){
		ArrayList<Object> mes = new ArrayList<Object>();
		mes.add(name);
		
		//TODO MAKE THIS ATTACHED TO THE UI, SO THE USER CAN CHOOSE THE STARTING LOCATIION
		mes.add(GarrisonName.INN);
		
		network.send(new Message(MessageType.CHARACTER_SELECT, mes));
		model.addMessage("Sent character select");
		state = GameState.NULL;
		view.updateNonBoardGUI(null);//sending null rather than the player object, null checks make this safe
	}
	
	/**
	 * Handles the selection of a target for combat
	 * @param name the CharacterName of the player to target
	 */
	public void handleTargetSelection(CharacterName name){
		ArrayList<Object> mes = new ArrayList<Object>();
		mes.add(name);
		network.send(new Message(MessageType.COMBAT_TARGET, mes));
		model.addMessage("Sent target selection");
		System.out.println(name);
		state = GameState.NULL;
		view.updateNonBoardGUI(null);//sending null rather than the player object, null checks make this safe
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
