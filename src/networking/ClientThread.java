package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ClientThread extends Thread implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1937931839925290872L;
	private Socket socket = null;
	private NetworkClient client = null;
	private ObjectInputStream streamIn = null;
	private ObjectOutputStream streamOut = null;
	
	private boolean done = false;
	
	public ClientThread(NetworkClient client, Socket socket){
		this.client = client;
		this.socket = socket;
		this.open();
		this.start();
	}
	
	//ERROR IN HERE
	
	public void open(){
		try{
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			streamOut.flush();
			streamIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ioe){
			System.out.println("Error getting input stream");
			//Trace.exception(ioe, this);
			client.stop();
		}
	}
	
	public void send(Object o){
		try{
			streamOut.writeObject(o);
			streamOut.flush();
			System.out.println("Sent Message: "+o);
		} catch (IOException ioe) {
			System.out.println("ERROR sending Message: ");
		}
	}
	
	public void close(){
		done = true;
		try{
			if(streamIn != null)
				streamIn.close();
		} catch (IOException ioe){
			System.out.println("Error closing input stream");
			//Trace.exception(ioe, this);
		}
		
	}

	@Override
	public void run() {
		System.out.println("Client Thread "+ socket.getLocalPort()+ " running.");
		while(!done){
			try{
				client.handle(streamIn.readObject());
			} catch (IOException ioe){
				System.out.println("ERROR reading input:");
				ioe.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
