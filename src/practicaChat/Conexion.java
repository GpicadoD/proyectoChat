package practicaChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion {
	private final int PUERTO_SERVER_LINDA = 1234; // Puerto para la conexión
	private final int PUERTO_SERVER_A = 1235; // Puerto para la conexión
	private final int PUERTO_SERVER_B = 1236; // Puerto para la conexión
	private final int PUERTO_SERVER_C = 1237; // Puerto para la conexión
	private final int PUERTO_SERVER_AR = 1238; // Puerto para la conexión
	private final String HOST = "localhost"; // Host para la conexión
	protected ServerSocket ss; // Socket del servidor
	protected Socket cs; // Socket del cliente

	public Conexion(String tipo) throws IOException {// Constructor
		if (tipo.equalsIgnoreCase("servidorLinda")) {
			ss = new ServerSocket(PUERTO_SERVER_LINDA);// Se crea el socket para el servidor en puerto 1234
		}  else if (tipo.equalsIgnoreCase("clienteLinda")) {
			cs = new Socket(HOST, PUERTO_SERVER_LINDA); // Socket para el cliente en localhost en puerto 1234
		} 
	}
}
