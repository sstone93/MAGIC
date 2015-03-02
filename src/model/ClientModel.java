package model;

public class ClientModel {
	
	//I am dumb. I do not know anything about any other part of the architecture.
	//My methods are called by the ClientController who owns me. I act accordingly.
	
	//in theory, I will hold:
		//- A decently up to date copy of the board
		//- Up to date information about my character
	
	private Board board;
	private Player player;
	private String messages;
	private int    messageCount;
	private int    numPlayers = 2; //just temporary, should be set by the server.
	
	public ClientModel(){
		//Player[] players = new Player[1];
		//player = new Player(new Swordsman());
		//players[0] = player;
		//board = new Board(players);
	}
	
	public Board getBoard(){
		return board;
	}
	
	public void setBoard(Board b){
		this.board = b;
	}
	
	public void setPlayer(Player p){
		this.player = p;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void addMessage(String m){
		messageCount++;
		if (messageCount == 10) {
			this.messages = m+"\n";
			messageCount = 0;
		}
		else
			this.messages += m+"\n";
	}
	
	/**
	 * CURRENT MESSAGES
	 * @return
	 */
	public String getMessages(){
		return messages;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

}
