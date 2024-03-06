package practicaChat;

import java.io.IOException;


public class MainServidorLinda {
	public static void main(String[] args) throws IOException {
		System.out.println("SERVER CHAT");
		System.out.println("*******************************");
		ServidorChat serv = new ServidorChat(); // Se crea el servidor
		System.out.println("Iniciando servidor Linda\n");
		serv.startServer(); // Se inicia el servidor
	}
}
