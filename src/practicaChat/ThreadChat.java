package practicaChat;

import java.io.IOException;
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

	public ThreadChat(ObjectInputStream in, ObjectOutputStream out, int id, Socket cs,
			Semaphore s1) {
		this.in = in;
		this.out = out;
		this.id = id;
		this.cs = cs;
		this.s1 = s1;
	}

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

	public static PublicKey bytesToPublicKey(byte[] keyBytes)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // O el algoritmo correspondiente

		// Construye la especificación de la clientPublicKey a partir de los bytes
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// Genera la clientPublicKey pública a partir de la especificación de la
		// clientPublicKey
		return keyFactory.generatePublic(keySpec);
	}

	public String decrypt(String mensaje) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		byte[] mensajeBytes = Base64.getDecoder().decode(mensaje);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, this.privatekey);
		return new String(cipher.doFinal(mensajeBytes));
	}

	public String encrypt(String mensaje, PublicKey clientPublicKey) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
		byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public String randomCode() {
		return "";
	}

	/*
	 * Pre: --- Post:
	 */
	public void run() {
		try {

			RSACipher();

			ArrayList<Sala> roomList = new ArrayList<Sala>();

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
						out.writeObject(encrypt("Se ha creado una room con nombre: " + room.getNombre(), clientPublicKey));

					} else {
						Sala room = new Sala(parsedMensaje[1] + "#" + randomCode(),
								parsedMensaje[2], new ArrayList<Usuario>());
						roomList.add(room);
						System.out.println("Se ha creado una room con nombre: " + room.getNombre()
								+ " con la contraseña " + room.getClave());
					}

				} else if (parsedMensaje.length == 1 && parsedMensaje[0].equalsIgnoreCase("LIST")) {
					System.out.println("Lista de salas publicas: ");
					if (roomList.size() == 0) {
						out.writeObject(encrypt("No hay salas publicas disponibles", clientPublicKey));
					} else {
						String respuesta = "";
						for (Sala room : roomList) {
							if (room.getClave() != null) {
								respuesta =  respuesta + ("- " + room.getNombre() + " users "
										+ room.getUsesrList().size() + "\n");
							}
						}
						if (respuesta.length()>0) out.writeObject(encrypt(respuesta, clientPublicKey));
						else out.writeObject(encrypt("No hay salas publicas disponibles", clientPublicKey));

					}

				} else if (parsedMensaje[0].equalsIgnoreCase("JOIN")) {
					Usuario nuevoUsuario = new Usuario(out, clientPublicKey);

					if (parsedMensaje.length == 2) {
						for (Sala room : roomList) {

							if (room.getNombre().equals(parsedMensaje[1])) {

								room.getUsesrList().add(nuevoUsuario);

							}
						}
						System.out.println("te has unido a la room " + parsedMensaje[1]);
					} else {
						for (Sala room : roomList) {
							if (room.getNombre().equals(parsedMensaje[1])
									&& room.getClave().equals(parsedMensaje[2])) {
								room.getUsesrList().add(nuevoUsuario);
							} else {
								System.out.println("No se ha podido unir a la sala");
								out.writeObject(
										encrypt("No se ha podido unir a la sala", clientPublicKey));
							}

							if (room.getNombre().equals(parsedMensaje[1])
									&& room.getClave().equals(parsedMensaje[2])) {
								room.getUsesrList().add(nuevoUsuario);
							}
							System.out.println("te has unido a la room " + parsedMensaje[1]
									+ " con la contraseña " + parsedMensaje[2]);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}