package practicaChat;

import java.io.DataInputStream;
import java.net.Socket;

public class ThreadEscritor {
	
	private DataInputStream out;
	private Socket cs;
	private String mensaje;
	
	public ThreadEscritor(DataInputStream out, Socket cs, String mensaje) {
		super();
		this.out = out;
		this.cs = cs;
		this.mensaje = mensaje;
	}
	
	
	
	
}
