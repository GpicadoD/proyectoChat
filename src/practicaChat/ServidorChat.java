package practicaChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class ServidorChat extends Conexion {

	public ServidorChat() throws IOException {
		super("servidorChat");
	}

	public void startServer() {
		try {
			int id = 1;
			Semaphore s1 = new Semaphore(1);
			ArrayList<Sala> roomList = new ArrayList<Sala>();
			while (true) {
				System.out.println("Esperando a conexion");
				cs = ss.accept();
				ObjectOutputStream outClienteChat = new ObjectOutputStream(cs.getOutputStream());
				ObjectInputStream inClienteChat = new ObjectInputStream(cs.getInputStream());
				ThreadChat tChat =  new ThreadChat(inClienteChat, outClienteChat, id, cs, s1, roomList);
				tChat.start();
				id++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
