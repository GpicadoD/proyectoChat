package practicaChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
 * @author GeancarlosPicado
 *
 */
public class ThreadChat extends Thread {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int id;
	private Socket cs;
	private PrivateKey privatekey;
	private PublicKey publickey;

	public ThreadChat(ObjectInputStream in, ObjectOutputStream out, int id, Socket cs) {
		this.in = in;
		this.out = out;
		this.id = id;
		this.cs = cs;
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

	public static PublicKey bytesToPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // O el algoritmo correspondiente

		// Construye la especificación de la clave a partir de los bytes
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// Genera la clave pública a partir de la especificación de la clave
		return keyFactory.generatePublic(keySpec);
	}

	public String decrypt(String mensaje) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {

		byte[] mensajeBytes = Base64.getDecoder().decode(mensaje);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, this.privatekey);
		return new String(cipher.doFinal(mensajeBytes));
	}

	public String encrypt(String mensaje, PublicKey clave) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, clave);
		byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	/*
	 * Pre: --- Post:
	 */
	public void run() {
		try {

			RSACipher();
			ArrayList<Sala> salas = new ArrayList<Sala>();
			ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

			String publicKeyStr = Base64.getEncoder().encodeToString(publickey.getEncoded());

//			String clave = (String) in.readObject();
			PublicKey clave = (PublicKey) (in.readObject());
//			System.out.println("Recibida Clave: " + clave);

//			System.out.println("Public key desde el server: " + this.publickey);
			out.writeObject(this.publickey);

			while (true) {

				String mensaje = (String) in.readObject();
				String[] parsedMensaje;
				String decryptedMensaje;

//				System.out.println("MENSAJE ENCR: " + mensaje);
				decryptedMensaje = decrypt(mensaje);
				parsedMensaje = decryptedMensaje.split(" ");

				if (parsedMensaje[0].equalsIgnoreCase("CREATE")) {

					if (parsedMensaje.length == 2) {

						salas.add(new Sala(parsedMensaje[1], null, usuarios));
						System.out.println("Se ha creado una sala con nombre: " + parsedMensaje[1]);

					} else {
						salas.add(new Sala(parsedMensaje[1], parsedMensaje[2], usuarios));
						System.out.println("Se ha creado una sala con nombre: " + parsedMensaje[1]
								+ " con la contraseña " + parsedMensaje[2]);
					}

				} else if (parsedMensaje.length == 1 && parsedMensaje[0].equalsIgnoreCase("LIST")) {

					for (Sala contenido : salas) {
						System.out.println("- " + contenido.getNombre() + " users " + contenido.getUsesrList());
					}
					System.out.println("Lista de todas las salas");

				} else if (parsedMensaje[0].equalsIgnoreCase("JOIN") && parsedMensaje.length < 4) {
					Usuario nuevoUsuario = new Usuario(out, publicKeyStr);
					usuarios.add(nuevoUsuario);

					if (parsedMensaje.length == 2) {
						for (Sala contenido : salas) {

							if (contenido.getNombre().equals(parsedMensaje[1])) {
								contenido.getUsesrList().add(nuevoUsuario);
							}
						}
						System.out.println("te has unido a la sala " + parsedMensaje[1]);
					} else {

						for (Sala contenido : salas) {
							if (contenido.getClave() == null) {
								System.out.println("No existe una sala con esa contraseña");
							} else {
								if (contenido.getNombre().equals(parsedMensaje[1])
										&& contenido.getClave().equals(parsedMensaje[2])) {
									contenido.getUsesrList().add(nuevoUsuario);
								}
								System.out.println("te has unido a la sala " + parsedMensaje[1] + " con la contraseña "
										+ parsedMensaje[2]);
							}
						}
					}
				}
//				encyptMensaje = encrypt(mensaje, clave);

//				encyptMensaje = decrypt(mensaje);

//				for (String e : parsedMensaje) {
//					System.out.println(e);
//				}
//				

//				System.out.println("Mensaje enciptado : " + encyptMensaje);
				out.writeObject("hola");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}