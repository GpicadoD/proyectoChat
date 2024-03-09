package practicaChat;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

public class ThreadEscritor extends Thread {

	private ObjectOutputStream out;
	private Socket cs;
	private PublicKey clave;

	public ThreadEscritor(ObjectOutputStream out, Socket cs, PublicKey clave) {
		super();
		this.out = out;
		this.cs = cs;
		this.clave = clave;
	}

	public String encrypt(String mensaje, PublicKey clave) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, clave);
		byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public void run() {
		try {
			Scanner entrada = new Scanner(System.in);
			String mensaje;
			String parsedMensaje[];
			String encyptMensaje;
//			System.out.println("clave en el thread " + clave);

			while (true) {

				System.out.print("Comando > ");
				mensaje = entrada.nextLine();
				parsedMensaje = mensaje.split(" ");
				if (parsedMensaje.length < 4) {
					if (mensaje.contains("CREATE") || mensaje.contains("LIST") || mensaje.contains("JOIN")) {

						if (parsedMensaje[0].equalsIgnoreCase("LIST")) {
							encyptMensaje = encrypt(mensaje, clave);
							out.writeObject(encyptMensaje);

						} else if (parsedMensaje[0].equalsIgnoreCase("JOIN")) {
							encyptMensaje = encrypt(mensaje, clave);
							out.writeObject(encyptMensaje);

						} else if (parsedMensaje[0].equalsIgnoreCase("CREATE")) {

							if (parsedMensaje.length > 1 && parsedMensaje.length < 4) {
								if (!parsedMensaje[1].contains("#")) {
									encyptMensaje = encrypt(mensaje, clave);
									out.writeObject(encyptMensaje);
								}
							}
						}
					}
				} else {
					System.out.println("El mensaje no se reconoce");
				}
			}

		} catch (Exception e) {

		}

	}

}
