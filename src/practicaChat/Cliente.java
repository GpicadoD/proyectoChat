package practicaChat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;
import java.security.*;
import javax.crypto.*;
import java.net.SocketException;
import java.util.concurrent.Semaphore;
 /**
  * 
  * Clase que se encarga de crear todas las variables e inicia la conexión
  * con el servidor. Esta clase crea dos threads para separar el apartado de
  * escritura y lectura con variables compartidas (semáforo y sharedData)
  * 
  * @author Javier Oliván y Geancarlos Picado
  *
  */

public class Cliente extends Conexion {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public Cliente() throws IOException {
		super("cliente");
	}
	
	/*
	 * Pre: ---
	 * Post: Función que crea los objetos PublicKey y PrivateKey.
	 */
	public void RSACipher() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair pair = generator.generateKeyPair();
			this.privateKey = pair.getPrivate();
			this.publicKey = pair.getPublic();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}	
	}
	
	/*
	 * Pre: ---
	 * Post: Función que crea los objetos PublicKey y PrivateKey
	 */
	public void startClient() throws ClassNotFoundException, SocketException{
		Semaphore s1 = new Semaphore (1);
		SharedData sharedData = new SharedData(false);
		try {
			
			ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());
			
			ObjectInputStream in = new ObjectInputStream(cs.getInputStream());
		
			
			System.out.println("Conexión establecida con el servidor");
			
			RSACipher();
			
            out.writeObject(this.publicKey);
            
            PublicKey serverPublicKey = (PublicKey) (in.readObject());


			ThreadEscritor threadW = new ThreadEscritor(out, cs, serverPublicKey, s1, sharedData);
			ThreadLector threadR = new ThreadLector(in, cs, privateKey, s1, sharedData);

			threadW.start();
			threadR.start();

		} catch (IOException e) {
			System.out.println("asdsad");
			System.out.println(e.getMessage());
		}
	}
}
