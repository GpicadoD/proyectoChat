package practicaChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServidorChat extends Conexion{

	public ServidorChat() throws IOException {
		super("servidorChat");
	}
	
	public void startServer() {
		try {
			while (true) {
				cs = ss.accept();
				DataInputStream inClienteChat = new DataInputStream(cs.getInputStream());
				DataOutputStream outClienteChat = new DataOutputStream(cs.getOutputStream());
				String clave = (inClienteChat.readUTF());
				
				System.out.println(clave);
				outClienteChat.writeUTF("1234");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
