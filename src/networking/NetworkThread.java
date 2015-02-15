package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * General NetworkingThread class, used on both clients and servers
 * @author Nicholas Laws
 */
public class NetworkThread extends Thread implements Serializable{
	
	private static final long serialVersionUID = -4436892377169386035L;
	private int ID = -1;
	private Socket socket = null;
	private NetworkEntity parent = null;
	private ObjectInputStream streamIn = null;
	private ObjectOutputStream streamOut = null;
	private boolean done = false;
	
	/**
	 * Constructor for an instance of a NetworkThread
	 * @param parent A reference to the NetworkEntity who spawned this thread
	 * @param socket The socket that this thread will be operating on.
	 */
	public NetworkThread(NetworkEntity parent, Socket socket, int ID){
		super();
		this.parent = parent;
		this.socket = socket;
		this.ID = ID;
	}
	
	/**
	 * Getter to obtain the Thread's ID Number
	 * @return The ID# of this thread (AKA the port# it is operating on)
	 */
	public int getID(){
		return this.ID;
	}
	
	/**
	 * Sends an object through the output stream to the connected NetworkEntity
	 * @param o The object being sent to the entity
	 */
	public void send(Object o){
		try{
			streamOut.writeObject(o);
			streamOut.flush();
			System.out.println("Sent Message: "+o);
		} catch (IOException ioe) {
			System.out.println("	"+ID + "ERROR sending Message: ");
			parent.remove(ID);										//Parent properly handles killing off this thread
		}
	}
	
	/**
	 * Main loop of the thread, runs until .close() is called
	 */
	public void run() {
		System.out.println("	"+ID + ": Now Running.");
		while (!done) {												
			try{						//Trys to read in a new object, if it does, send it to the NetworkEntity to be handled
				parent.handle(ID, streamIn.readObject());
			} catch (IOException ioe) {
				System.out.println("	"+ID + ": ERROR reading input (Socket Closed?!?):");
				parent.remove(ID);									//Parent properly handles killing off this thread
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * Opens the OutputStream and the InputStream (along with proper flushing)
	 * @throws IOException
	 */
	public void open() throws IOException {
		System.out.println("	"+ID + ": OPENING buffer streams");
		streamOut = new ObjectOutputStream(socket.getOutputStream());
		streamOut.flush();
		System.out.println("	"+ID + ": Opened OUTPUT Stream OK");
		streamIn = new ObjectInputStream(socket.getInputStream());
		System.out.println("	"+ID + ": BOTH Buffer Streams OPENED OK");
	}

	/**
	 * Closes the Socket, InputStream and OutputStream.
	 * @throws IOException Error in closing 
	 */
	public void close() throws IOException {
		System.out.println("	"+ID + ": CLOSING buffer streams");
		this.done = true;		//tells the main loop to stop repeating.
		if(socket != null)
			socket.close();
		if(streamIn != null)
			streamIn.close();
		if(streamOut != null)
			streamOut.close();
		System.out.println("	"+ID + ": Buffer Streams CLOSED OK");
	}
}
