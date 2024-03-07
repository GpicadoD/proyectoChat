package practicaChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

import javax.crypto.Cipher;

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
			String publicKeyStr = Base64.getEncoder().encodeToString(publickey.getEncoded());
			String clave = in.readUTF();
//			PublicKey clave = (PublicKey) (in.readObject());
			System.out.println("Recibida Clave");

			System.out.println("Public key desde el server: ");
			out.writeUTF(publicKeyStr);

			while (true) {
				String mensaje = in.readUTF();
				String encyptMensaje;
				System.out.println(mensaje);
				
//				encyptMensaje = encrypt(mensaje,clave);
				System.out.println("Mensaje enciptado : " + mensaje); 
				out.writeUTF(mensaje);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}