package practicaChat;

import java.io.IOException;

public class MainCliente {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		System.out.println("CLIENTE");
		System.out.println("*******************************");
		Cliente cli = new Cliente();
		System.out.println("Iniciando cliente\n");
		cli.startClient(); 
	}
}
