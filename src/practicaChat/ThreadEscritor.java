package practicaChat;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ThreadEscritor extends Thread {

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
			String parsedMensaje[];
			System.out.println("clave en el thread " + clave);
			while(true) {
				System.out.print("Comando > ");
				mensaje = entrada.nextLine();
				parsedMensaje = mensaje.split(" ");

				if (mensaje.contains("CREATE") || mensaje.contains("LIST") || mensaje.contains("JOIN")) {
					
					if (parsedMensaje[0].equalsIgnoreCase("LIST")) {
						
						out.writeUTF(mensaje);
						
					} else if (parsedMensaje[0].equalsIgnoreCase("JOIN")) {
						
						out.writeUTF(mensaje);
						
					} else if (parsedMensaje[0].equalsIgnoreCase("CREATE")) {
						
						if (parsedMensaje.length > 1 && parsedMensaje.length < 3) {
							if(!parsedMensaje[1].contains("#")) {
								out.writeUTF(mensaje);
							}
						}else {
							System.out.println("El mensaje no se reconoce");
						}
					}
				} else {
					System.out.println("El mensaje no se reconoce");
				}
			}
			

		} catch (Exception e) {

		}

	}

}
