package practicaChat;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.*;
import java.util.Base64;
import java.util.concurrent.Semaphore;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ThreadLector extends Thread {

    private ObjectInputStream in;
    private Socket cs;
    private PrivateKey privateKey;
    private Semaphore s1;
    private SharedData sharedData;

    public ThreadLector(ObjectInputStream in, Socket cs, PrivateKey privateKey, Semaphore s1, SharedData sharedData) {
        super();
        this.in = in;
        this.cs = cs;
        this.privateKey = privateKey;
        this.s1 = s1;
        this.sharedData = sharedData;
    }

    public String decrypt(String mensaje) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] mensajeBytes = Base64.getDecoder().decode(mensaje);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        return new String(cipher.doFinal(mensajeBytes));
    }

    public void run() {
        try {
            while (true) {
                String mensaje = (String) in.readObject();
                mensaje = decrypt(mensaje);
                System.out.println(mensaje);
                if (mensaje.contains("te has unido a la sala") ) {
                	sharedData.setInChat(true);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
