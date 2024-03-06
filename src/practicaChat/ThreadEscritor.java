package practicaChat;

import java.io.DataOutputStream;
import java.net.Socket;

public class ThreadEscritor {
	
	private DataOutputStream out;
	private Socket cs;
	private String clave;
	
	public ThreadEscritor(DataOutputStream out, Socket cs, String clave) {
		super();
		this.out = out;
		this.cs = cs;
		this.clave = clave;
	}
	
	public void run() {
		try {
			String comando;
			out.writeUTF(clave);
			
			
		} catch (Exception e) {
			
			
		}
		
		
	}
	
	
}
