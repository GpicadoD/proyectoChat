package practicaChat;

import java.io.IOException;

public class MainCliente {

	public static void main(String[] args) throws InterruptedException {
		try {
			System.out.println("CLIENTE");
			System.out.println("*******************************");
			Cliente cli = new Cliente();
			System.out.println("Iniciando cliente\n");
			cli.startClient();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Ha ocurrido un fallo en la conexion");
			Thread.sleep(2000);
			MainCliente.main(args);
		}
	}
}
