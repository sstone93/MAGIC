package networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkClient implements Runnable{
	
	private int ID = 0;
	private Socket socket = null;
	private Thread thread = null;
	public ClientThread client = null;
	
	public Object model;
	
	public NetworkClient(String serverName, int serverPort) throws UnknownHostException, IOException {
			
		System.out.println(ID + ": Establishing connection. Please wait...");
		
		try {
			this.socket = new Socket(serverName, serverPort);
			this.ID = socket.getLocalPort();
			System.out.println(ID + ": Connected to server: " + socket.getInetAddress());
			System.out.println(ID + ": Connected to portid: " + socket.getLocalPort());
			this.start();
		} catch (UnknownHostException uhe) {
			System.err.println(ID + ": Unknown Host");
			throw uhe;
		} catch (IOException ioe) {
			System.out.println(ID + ": Unexpected exception");
			throw ioe;
		}
	}
	
	public int getID(){
		return this.ID;
	}
	
	public void run(){
		
	}
	
	/*@Override
	public void run(){
		System.out.println(ID + ": Client Started...");
		while (streamOut != null) {
			try {
				if (thread != null) {
					streamOut.flush();
				} else {
					System.out.println(ID + ": Stream Closed");
					
					//if the server kicked you out, stop whining and shut down
					//client.close();
					//this.stop();
				}
			} catch (IOException e) {
				System.out.println(ID + ": Sending error");
				stop();
			}
		}
		System.out.println(ID + ": Client Stopped...");
	}*/
	
	public void handle(Object o) {
		//model.handle(o);
	}
	
	public void start() throws IOException {
		if(thread == null){
			client = new ClientThread(this, socket);
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stop(){
		try {
			if (thread != null){
				thread = null;
			}
			if(socket != null)
				socket.close();
			socket = null;
			
		} catch (IOException ioe){
			System.out.println(ID + ":Error closing connection ...");
		}
		client.close();
	}
}
