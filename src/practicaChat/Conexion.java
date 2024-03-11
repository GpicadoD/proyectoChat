package practicaChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * Clase que se encarga de asignar a cada clase parámetros de conexión.
 * 
 * @author Javier Oliván y Geancarlos Picado
 *
 */

public class Conexion {
	private final int PUERTO_SERVER = 1234; // Puerto para la conexión

	private final String HOST = "192.168.43.160"; // Host para la conexión
	protected ServerSocket ss; // Socket del servidor
	protected Socket cs; // Socket del cliente

	public Conexion(String tipo) throws IOException {// Constructor
		if (tipo.equalsIgnoreCase("servidorChat")) {
			ss = new ServerSocket(PUERTO_SERVER);// Se crea el socket para el servidor en puerto 1234
		}  else if (tipo.equalsIgnoreCase("cliente")) {
			cs = new Socket(HOST, PUERTO_SERVER); // Socket para el cliente en localhost en puerto 1234
		} 
	}
}
