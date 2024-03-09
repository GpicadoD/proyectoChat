package practicaChat;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.*;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ThreadLector extends Thread {

    private ObjectInputStream in;
    private Socket cs;
    private PrivateKey privateKey;

    public ThreadLector(ObjectInputStream in, Socket cs, PrivateKey privateKey) {
        super();
        this.in = in;
        this.cs = cs;
        this.privateKey = privateKey;
    }

    public String decrypt(String mensaje) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] mensajeBytes = Base64.getDecoder().decode(mensaje);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
//        System.out.println("PRIVATE : " + this.privateKey);
        return new String(cipher.doFinal(mensajeBytes));
    }

    public void run() {
        try {
            while (true) {
                String mensaje = (String) in.readObject();
                System.out.println("MENSAJE: " + mensaje);
                System.out.println(decrypt(mensaje)); 
//                System.out.println("MENSAJE DESCIFRADO: " + parsedMensaje);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
