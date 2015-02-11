package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ServerThread extends Thread implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8719452824623970769L;
	private int ID = -1;
	private Socket socket = null;
	private Server server = null;
	private ObjectInputStream streamIn = null;
	private ObjectOutputStream streamOut = null;
	
	private boolean done = false;
	
	public ServerThread(Server server, Socket socket){
		super();
		this.server = server;
		this.socket = socket;
		this.ID = socket.getPort();
	}
	
	public int getID(){
		return this.ID;
	}
	
	public void send(Object o){
		try{
			streamOut.writeObject(o);
			streamOut.flush();
			System.out.println("Sent Message: "+o);
		} catch (IOException ioe) {
			System.out.println(ID + "ERROR sending Message: ");
			server.remove(ID);
		}
	}
	
	//@Override
	public void run() {
		System.out.println("Server Thread " + ID + " running.");
		while (!done) {
			try{
				//gets object, tells the server to handle it.
				server.handle(ID, streamIn.readObject());
		
				
			} catch (IOException ioe) {
				System.out.println(ID + ": ERROR reading input:");
				
				ioe.printStackTrace();
				
				//TEMPORARILY DISABLED!!!!!!!!!!!
				//server.remove(ID);
				
				break;
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void open() throws IOException {
		System.out.println(ID + ": Opening buffer streams");
		streamOut = new ObjectOutputStream(socket.getOutputStream());
		streamOut.flush();
		streamIn = new ObjectInputStream(socket.getInputStream());
	}

	public void close() throws IOException {
		this.done = true;
		if(socket != null)
			socket.close();
		if(streamIn != null)
			streamIn.close();
	}
}
