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

	public ThreadChat(ObjectInputStream in, ObjectOutputStream out, int id, Socket cs,
			Semaphore s1, ArrayList<Sala> roomList) {
		this.in = in;
		this.out = out;
		this.id = id;
		this.cs = cs;
		this.s1 = s1;
		this.roomList = roomList;
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

	public static int numeroAleatorioEnRango(int minimo, int maximo) {
		return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
	}

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
	
	public void roomFunction(Sala room, Usuario user) throws Exception {
		this.out.writeObject(encrypt("te has unido a la room " + room.getNombre(), user.getPublicKey())) ;
		while(true) {
			String mensaje = decrypt((String) in.readObject()) ;
			if (mensaje.equalsIgnoreCase("exit")) {
				if (room.exitRoom(user) != null) room.getUsesrList().remove(user);
				System.out.println("Saliendo de la sala");
				break;
			}else {
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
						out.writeObject(encrypt("Se ha creado una room con nombre: " + room.getNombre(), clientPublicKey));

					} else {
						Sala room = new Sala(parsedMensaje[1] + "#" + randomCode(),
								parsedMensaje[2], new ArrayList<Usuario>());
						roomList.add(room);
						out.writeObject(encrypt("Se ha creado una room con nombre: " + room.getNombre()
						+ " con la contraseña " + room.getClave(),clientPublicKey)) ;
					}

				} else if (parsedMensaje.length == 1 && parsedMensaje[0].equalsIgnoreCase("LIST")) {
					out.writeObject(encrypt("Lista de salas publicas: ",clientPublicKey));
					if (roomList.size() == 0) {
						out.writeObject(encrypt("No hay salas publicas disponibles", clientPublicKey));
					} else {
						String respuesta = "";
						for (Sala room : roomList) {
							if (room.getClave() == null) {
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
								roomFunction(room, nuevoUsuario);
								break;
							}
						}
					} else {
						for (Sala room : roomList) {
							if (room.getNombre().equals(parsedMensaje[1])
									&& room.getClave().equals(parsedMensaje[2])) {
								room.getUsesrList().add(nuevoUsuario);
							} else {
								out.writeObject(encrypt("No se ha podido unir a la sala",clientPublicKey)); 
							}

							if (room.getNombre().equals(parsedMensaje[1])
									&& room.getClave().equals(parsedMensaje[2])) {
								room.getUsesrList().add(nuevoUsuario);
							}
							out.writeObject(encrypt("te has unido a la room " + parsedMensaje[1]
									+ " con la contraseña " + parsedMensaje[2],clientPublicKey)) ;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}