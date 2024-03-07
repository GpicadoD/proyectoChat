package practicaChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServidorChat extends Conexion {

	public ServidorChat() throws IOException {
		super("servidorChat");
	}

	public void startServer() {
		try {
			int id = 1;
			while (true) {
				System.out.println("Esperando");
				cs = ss.accept();
				ObjectOutputStream outClienteChat = new ObjectOutputStream(cs.getOutputStream());
				ObjectInputStream inClienteChat = new ObjectInputStream(cs.getInputStream());
				ThreadChat tChat =  new ThreadChat(inClienteChat, outClienteChat, id, cs);
				tChat.run();
				id++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
