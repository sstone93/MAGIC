package networking;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = -2288820887835157202L;
	private String type;
	private Object[] data;
	
	public Message(String type, Object[] data){
		this.type = type;
		this.data = data;
	}
	
	public String getType(){
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
