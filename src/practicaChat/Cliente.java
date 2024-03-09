package practicaChat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;
import java.security.*;
import javax.crypto.*;
import java.net.SocketException;


public class Cliente extends Conexion {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public Cliente() throws IOException {
		super("cliente");
	}
	
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
	
	
	
	public void startClient() throws ClassNotFoundException, SocketException{
		try {
			System.out.println("Asdad");
			ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());
			System.out.println("sadas");
			ObjectInputStream in = new ObjectInputStream(cs.getInputStream());
			System.out.println("21Sdas");
			
			System.out.println("Conexión establecida con el servidor");
			
			RSACipher();
			
            out.writeObject(this.publicKey);
            
            PublicKey serverPublicKey = (PublicKey) (in.readObject());


			ThreadEscritor threadW = new ThreadEscritor(out, cs, serverPublicKey);
			ThreadLector threadR = new ThreadLector(in, cs, privateKey);

			threadW.start();
			threadR.start();

		} catch (IOException e) {
			System.out.println("asdsad");
			System.out.println(e.getMessage());
		}
	}
}
