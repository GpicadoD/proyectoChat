package practicaChat;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.crypto.Cipher;

/**
 * 
 * Clase que se encarga de transmitir los mensajes encriptados del cliente al servidor.
 * Además se encarga de confirmar que los comandos recibidos son correctos.
 * 
 * @author Javier Olivan y Geancarlos Picado
 *
 */
public class ThreadEscritor extends Thread {

	private ObjectOutputStream out;
	private Socket cs;
	private PublicKey serverPublicKey;
	private Semaphore s1;
	private SharedData sharedData;

	public ThreadEscritor(ObjectOutputStream out, Socket cs, PublicKey serverPublicKey,
			Semaphore s1, SharedData sharedData) {
		super();
		this.out = out;
		this.cs = cs;
		this.serverPublicKey = serverPublicKey;
		this.s1 = s1;
		this.sharedData = sharedData;
	}
	
	/*
	 * Pre: --
	 * Post: Método que encripta el mensaje con la publicKey del servidor.
	 */
	public String encrypt(String mensaje, PublicKey serverPublicKey) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
		byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	
	public void run() {
		try {
			Scanner entrada = new Scanner(System.in);
			String mensaje;
			String parsedMensaje[];
			String encyptMensaje;

			while (true) {
				if (s1.availablePermits() > 0) {
					if (sharedData.getInChat()) {
						System.out.println("Mensaje: ");
						mensaje = entrada.nextLine();
						out.writeObject(encrypt(mensaje, serverPublicKey));
						if (mensaje.equalsIgnoreCase("EXIT")) {
							s1.acquire();
							continue;
						}
					} else {
						System.out.print("Comando > ");
						mensaje = entrada.nextLine();
						if (mensaje.equalsIgnoreCase("EXIT")) {

							System.out.println("Saliendo del programa...");
							break;
						} else {
							parsedMensaje = mensaje.split(" ");
							if (parsedMensaje.length < 4) {
								if (mensaje.contains("CREATE") || mensaje.contains("LIST")
										|| mensaje.contains("JOIN")) {

									if (parsedMensaje[0].equalsIgnoreCase("LIST")) {
										encyptMensaje = encrypt(mensaje, serverPublicKey);
										out.writeObject(encyptMensaje);

									} else if (parsedMensaje[0].equalsIgnoreCase("JOIN")) {
										s1.acquire();
										encyptMensaje = encrypt(mensaje, serverPublicKey);
										out.writeObject(encyptMensaje);

									} else if (parsedMensaje[0].equalsIgnoreCase("CREATE")) {

										if (parsedMensaje.length > 1 && parsedMensaje.length < 4) {
											if (!parsedMensaje[1].contains("#")) {
												encyptMensaje = encrypt(mensaje, serverPublicKey);
												out.writeObject(encyptMensaje);
											}
										}
									}
								}
							} else {
								System.out.println("El mensaje no se reconoce");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
