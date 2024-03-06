package practicaChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Cliente extends Conexion{

	public Cliente() throws IOException {
		super("cliente");
	}
	
	public void startClient() throws InterruptedException {
		try {
			Scanner entrada = new Scanner(System.in);
			DataInputStream in = new DataInputStream(cs.getInputStream());
			DataOutputStream out = new DataOutputStream(cs.getOutputStream());
			
			out.writeUTF("hola");
			String clave = in.readUTF();
			System.out.println(clave);
			
			while(true) {
				String mensaje;
				System.out.print("Comando >");
				mensaje = entrada.nextLine();
			}
			
		} catch (IOException e) {
			Thread.sleep(2000);
			startClient();
		}
	}
}
