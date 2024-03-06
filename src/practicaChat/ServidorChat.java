package practicaChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServidorChat extends Conexion {

	public ServidorChat() throws IOException {
		super("servidorChat");
	}

	public void startServer() {
		try {
			int id = 1;
			while (true) {
				cs = ss.accept();
				DataInputStream inClienteChat = new DataInputStream(cs.getInputStream());
				DataOutputStream outClienteChat = new DataOutputStream(cs.getOutputStream());
				ThreadChat tChat =  new ThreadChat(inClienteChat, outClienteChat, id, cs);
				tChat.run();
				id++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
