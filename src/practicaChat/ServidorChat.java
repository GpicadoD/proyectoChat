package practicaChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;

/**
 * 
 * Clase que crea inicia el funcionamiento del servidor, esperando la conexión
 * de un cliente para crear un nuevo Thread encargado de la comunicación. Aquí
 * se crean todos las variables necesarias para el proceso de la información.
 * 
 * @author Javier Olivan y Geancarlos Picado
 *
 */
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
				ThreadChat tChat = new ThreadChat(inClienteChat, outClienteChat, id, cs, s1,
						roomList);
				tChat.start();
				id++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
