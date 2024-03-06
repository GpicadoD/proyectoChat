package practicaChat;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ThreadEscritor extends Thread{
	
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
			Scanner entrada = new Scanner(System.in);
			String mensaje;
			System.out.println("clave en el thread " + clave);
			System.out.print("Comando > ");
			mensaje = entrada.nextLine();
			
			out.writeUTF(mensaje);
			
		} catch (Exception e) {
			
			
		}
		
		
	}
	
	
}
