package networking;

import java.io.Serializable;
import utils.Utility.MessageType;

public class Message implements Serializable{

	private static final long serialVersionUID = -2288820887835157202L;
	private MessageType type;
	private Object[] data;
	
	public Message(MessageType type, Object[] data){
		this.type = type;
		this.data = data;
	}
	
	public MessageType getType(){
		return this.type;
	}
	
	public Object[] getData(){
		return this.data;
	}
	
	public String toString()
	{
		return this.type + " Message: " + this.data;
	}
}
