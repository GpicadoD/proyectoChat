package practicaChat;

import java.io.IOException;


public class MainServidorChat {
	public static void main(String[] args) throws IOException {
		System.out.println("SERVER CHAT");
		System.out.println("*******************************");
		ServidorChat serv = new ServidorChat(); // Se crea el servidor
		System.out.println("Iniciando servidor\n");
		serv.startServer(); // Se inicia el servidor
	}
}
