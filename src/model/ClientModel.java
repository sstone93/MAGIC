package model;

public class ClientModel {
	
	//I am dumb. I do not know anything about any other part of the architecture.
	//My methods are called by the ClientController who owns me. I act accordingly.
	
	//in theory, I will hold:
		//- A decently up to date copy of the board
		//- Up to date information about my character
	
	private Board board;
	private Player player;
	
	public ClientModel(){
		board = new Board();
		player = new Player(null);//TODO just testing, shouldn't really be null
	}
	
	public Board getBoard(){
		return board;
	}
	
	public Player getPlayer(){
		return player;
	}

}
