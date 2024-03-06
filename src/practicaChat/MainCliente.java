package practicaChat;

import java.io.IOException;

public class MainCliente {
	
	public static void main(String[] args) throws IOException {
		System.out.println("CLIENTE");
		System.out.println("*******************************");
		Cliente cli = new Cliente(); // Se crea el cliente
		System.out.println("Iniciando cliente\n");
		try {
			cli.startClient();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
}
