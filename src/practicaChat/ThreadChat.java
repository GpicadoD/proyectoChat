package practicaChat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Clase Thread, que gestiona las acciones del Cliente con el que establece
 * conexión
 * 
 * @author GeancarlosPicado y Javier Oliván
 *
 */
public class ThreadChat extends Thread {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int id;
	private Socket cs;
	private PrivateKey privatekey;
	private PublicKey publickey;
	private Semaphore s1;
	private ArrayList<Sala> roomList;

	public ThreadChat(ObjectInputStream in, ObjectOutputStream out, int id, Socket cs, Semaphore s1,
			ArrayList<Sala> roomList) {
		this.in = in;
		this.out = out;
		this.id = id;
		this.cs = cs;
		this.s1 = s1;
		this.roomList = roomList;
	}

	/*
	 * Pre: ---
	 * Post: Método que crea la PublicKey y PrivateKey que se utiliza para
	 * comunicación con el cliente que se conecta.
	 */
	public void RSACipher() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair pair = generator.generateKeyPair();
			this.privatekey = pair.getPrivate();
			this.publickey = pair.getPublic();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Pre: --- 
	 * Post: Método que desencripta el mensaje que se recibe del cliente con la privateKey.
	 */
	public String decrypt(String mensaje) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		byte[] mensajeBytes = Base64.getDecoder().decode(mensaje);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, this.privatekey);
		return new String(cipher.doFinal(mensajeBytes));
	}

	/*
	 * Pre: --- 
	 * Post: Método que encripta el mensaje que se envía al usuario con su publicKey.
	 */
	public String encrypt(String mensaje, PublicKey clientPublicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
		byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	/*
	 * Pre: --- 
	 * Post: Genera un numero en aleatorio dentro de un rango.
	 */
	public static int numeroAleatorioEnRango(int minimo, int maximo) {
		return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
	}
	
	/*
	 * Pre: --- 
	 * Post: Genera un identificador aleatorio para las salas.
	 */
	public String randomCode() {
		String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		String cadena = "";
		int longitud = 5;
		for (int x = 0; x < longitud; x++) {
			int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
			char caracterAleatorio = banco.charAt(indiceAleatorio);
			cadena += caracterAleatorio;
		}
		return cadena;
	}

	/*
	 * Pre: --- 
	 * Post: Método que funciona como un estado, el cual se encarga de mandar los mensajes
	 * a los usuarios dentro de la sala y del resto de comandos de la misma.
	 */
	public void roomFunction(Sala room, Usuario user) throws Exception {
		this.out.writeObject(
				encrypt("te has unido a la sala " + room.getNombre(), user.getPublicKey()));
		while (true) {
			String mensaje = decrypt((String) in.readObject());
			if (mensaje.equalsIgnoreCase("exit")) {
				if (room.exitRoom(user) != null)
					room.getUsesrList().remove(user);
				this.out.writeObject(encrypt("Saliendo de la sala", user.getPublicKey()));
				break;
			} else {
				room.broadcastMessage(user, mensaje);
			}
		}

	}

	/*
	 * Pre: --- Post:
	 */
	public void run() {
		try {
			RSACipher();

			String publicKeyStr = Base64.getEncoder().encodeToString(publickey.getEncoded());
			PublicKey clientPublicKey = (PublicKey) (in.readObject());
			out.writeObject(this.publickey);

			while (true) {

				String mensaje = (String) in.readObject();
				String[] parsedMensaje;
				String decryptedMensaje;

				decryptedMensaje = decrypt(mensaje);
				parsedMensaje = decryptedMensaje.split(" ");

				if (parsedMensaje[0].equalsIgnoreCase("CREATE")) {

					if (parsedMensaje.length == 2) {

						Sala room = new Sala(parsedMensaje[1] + "#" + randomCode(), null,
								new ArrayList<Usuario>());
						s1.acquire();
						roomList.add(room);
						s1.release();
						out.writeObject(
								encrypt("Se ha creado una room con nombre: " + room.getNombre(),
										clientPublicKey));

					} else {
						Sala room = new Sala(parsedMensaje[1] + "#" + randomCode(),
								parsedMensaje[2], new ArrayList<Usuario>());
						roomList.add(room);
						out.writeObject(encrypt(
								"Se ha creado una room con nombre: " + room.getNombre()
										+ " con la contraseña " + room.getClave(),
								clientPublicKey));
					}

				} else if (parsedMensaje.length == 1 && parsedMensaje[0].equalsIgnoreCase("LIST")) {
					out.writeObject(encrypt("Lista de salas publicas: ", clientPublicKey));
					if (roomList.size() == 0) {
						out.writeObject(
								encrypt("No hay salas publicas disponibles", clientPublicKey));
					} else {
						String respuesta = "";
						for (Sala room : roomList) {
							if (room.getClave() == null) {
								respuesta = respuesta + ("- " + room.getNombre() + " users "
										+ room.getUsesrList().size() + "\n");
							}
						}
						if (respuesta.length() > 0)
							out.writeObject(encrypt(respuesta, clientPublicKey));
						else
							out.writeObject(
									encrypt("No hay salas publicas disponibles", clientPublicKey));

					}

					
					
				} else if (parsedMensaje[0].equalsIgnoreCase("JOIN")) {
					Usuario nuevoUsuario = new Usuario(out, clientPublicKey);
					Boolean inChat = false;
					
					
					if (roomList.size() == 0) {
						out.writeObject(encrypt("No hay salas para unirse", clientPublicKey));
					} else {
						
						if (parsedMensaje.length == 2) {
							for (Sala room : roomList) {
								if (room.getNombre().equals(parsedMensaje[1])) {
									inChat = true;
									room.getUsesrList().add(nuevoUsuario);
									roomFunction(room, nuevoUsuario);
									break;
								}
							}
							if (!inChat) {
								out.writeObject(encrypt("No existe una sala con esas condiciones",
										clientPublicKey));
							} else
								inChat = false;
						} else if (parsedMensaje.length == 3) {
							
							for (Sala room : roomList) {
								
								if (room.getNombre().equals(parsedMensaje[1])
										&& room.getClave().equals(parsedMensaje[2])) {
									inChat = true;
									room.getUsesrList().add(nuevoUsuario);
									roomFunction(room, nuevoUsuario);
									break;
								}
								if (!inChat) {System.out.println("sdsad");
									out.writeObject(encrypt("No existe una sala con esas condiciones",
											clientPublicKey));
									
								} else
									inChat = false;
								
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