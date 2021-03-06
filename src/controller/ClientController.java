package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import networking.Message;
import networking.NetworkClient;
import model.Board;
import model.ClientModel;
import model.CombatMoves;
import model.Phase;
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
	public boolean deadstate = false;
	public boolean blockState = false;
	
	/**
	 * Constructor for a ClientController
	 */
	public ClientController(){

		String serverIP = parseSettings();
		
		//instanciate the networking
		this.network = new NetworkClient(this, serverIP);
		//System.out.println("NetworkClient Successfully Created.");
				
		//instantiate the model
		this.model = new ClientModel();
		//System.out.println("Client Model Successfully Created.");
		
		//instantiate the view
		this.view = new View(this);
		state = GameState.CHOOSE_CHARACTER;
		view.setVisible(true);									//Activates the GUI
		
		//System.out.println("View Successfully Created.");
		view.update();//can maybe change this to view.updateNotBoard
	}
	
	private String parseSettings(){
		//setup importing from config file
		Scanner ourScanner;
		String ip = "";
		
		try {
			ourScanner = new Scanner(new File("settings.txt"));
			ip = ourScanner.nextLine();
			//System.out.println("Using Server IP: "+ip);
			ourScanner.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return ip;
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
				blockState = true;
				model.addMessage("Please Select Your Moves");
			}else if(text.equalsIgnoreCase("SEND COMBAT")){
				state = GameState.CHOOSE_COMBATTARGET;
				blockState = false;
				model.addMessage("Please Select Combat TARGET");
			}else if(text.equalsIgnoreCase("SEND COMBATMOVES")){
				state = GameState.CHOOSE_COMBATMOVES;
				model.addMessage("Please Select Combat Actions");
			}else if(text.equalsIgnoreCase("CHARACTER SELECT")){
				state = GameState.CHOOSE_CHARACTER;
				model.addMessage("START CHARACTER SELECT");
			}else if(text.equalsIgnoreCase("NOT ACCEPTING CHARACTER SELECT YET")){
				state = GameState.CHOOSE_CHARACTER;
				model.addMessage(text);
			}else if(text.equalsIgnoreCase("THAT CHARACTER HAS ALREADY BEEN CHOSEN")){
				state = GameState.CHOOSE_CHARACTER;
				model.addMessage(text);
			}else if(text.equalsIgnoreCase("NO PHASES LEFT")){
				state = GameState.NULL;
				model.addMessage("No phases remaining");
			}else if(text.equalsIgnoreCase("YOU WERE KILLED")){
				deadstate = true;
				state = GameState.NULL;
				model.addMessage("YOU WERE KILLED");
			}else{
				model.addMessage((String) message);
				view.updateMessageBox();
				needsUpdate = false; //needs to NOT trigger the update
			}
		}else if(message instanceof Board){
			model.setBoard((Board) message);
			//model.addMessage("New Board Recieved");
		}else if(message instanceof Player){
			model.setPlayer((Player) message);
			//model.addMessage("New Player Recieved");
		}
		
		if(view != null && needsUpdate){
			view.update();//is this needed? all of the above operations will call it?????
		}
	}
	
	/**
	 * This is where each play will be sent to the server
	 * @param p the phase that the player is using
	 * @param a the action that the player would like to take
	 * @param extraInfo additional info needed (ex. the clearing to move to)
	 */
	public void handlePlaySubmit(PhaseType p, Actions[] a, Object extraInfo){
		ArrayList<Object> mes = new ArrayList<Object>();
		
		//NEED TO CREATE NEW OBJECT AND ADD IT TO MESSAGE
		mes.add(new Phase(p, a, extraInfo));
		
		network.send(new Message(MessageType.ACTIVITIES, mes));
		model.addMessage("Sent activity");
		
		//state = GameState.NULL;
		view.updateNonBoardGUI();
	}
	
	public void handleBlockSubmit(CharacterName c){
		ArrayList<Object> mes = new ArrayList<Object>();
		mes.add(c);
		
		network.send(new Message(MessageType.BLOCK, mes));
		model.addMessage("Sent block");

		state = GameState.NULL;
		view.updateNonBoardGUI();//sending null rather than the player object, null checks make this safe
		
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
		view.updateNonBoardGUI();
	}
	
	/**
	 * Should verify that character is available and then set character
	 * @param name the name of the selected character
	 */
	public void handleCharacterSelection(CharacterName name, GarrisonName starting){
		ArrayList<Object> mes = new ArrayList<Object>();
		mes.add(name);
		mes.add(starting);
		
		network.send(new Message(MessageType.CHARACTER_SELECT, mes));
		model.addMessage("Sent character select");
		state = GameState.NULL;
		view.updateNonBoardGUI();
	}
	
	/**
	 * Handles the selection of a target for combat
	 * @param name the CharacterName of the player to target
	 */
	public void handleTargetSelection(ArrayList<CharacterName> chars, ArrayList<MonsterName> monsters){
		ArrayList<Object> mes = new ArrayList<Object>();
		mes.add(chars);
		mes.add(monsters);
		network.send(new Message(MessageType.COMBAT_TARGET, mes));
		model.addMessage("Sent target selection");
		state = GameState.NULL;
		view.updateNonBoardGUI();
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
