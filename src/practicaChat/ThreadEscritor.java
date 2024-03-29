package practicaChat;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.crypto.Cipher;

public class ThreadEscritor extends Thread {

    private ObjectOutputStream out;
    private Socket cs;
    private PublicKey serverPublicKey;
    private Semaphore s1;
    private SharedData sharedData;

    public ThreadEscritor(ObjectOutputStream out, Socket cs, PublicKey serverPublicKey, Semaphore s1, SharedData sharedData) {
        super();
        this.out = out;
        this.cs = cs;
        this.serverPublicKey = serverPublicKey;
        this.s1 = s1;
        this.sharedData = sharedData;
    }

    public String encrypt(String mensaje, PublicKey serverPublicKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
        byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public void run() {
        try {
            Scanner entrada = new Scanner(System.in);
            String mensaje;
            String parsedMensaje[];
            String encyptMensaje;            

            while (true) {
            	Thread.sleep(100);
                if (sharedData.getInChat()) {
                    System.out.println("Mensaje: ");
                    mensaje = entrada.nextLine();
                    out.writeObject(encrypt(mensaje, serverPublicKey));
                    if (mensaje.equalsIgnoreCase("EXIT")) {
                    	sharedData.setInChat(false);
                        continue;
                    }
                } else {
                    System.out.print("Comando > ");
                    mensaje = entrada.nextLine();
                    if (mensaje.equalsIgnoreCase("EXIT")) {
                        System.out.println("Saliendo del programa...");
                        break;
                    } else {
                        parsedMensaje = mensaje.split(" ");
                        if (parsedMensaje.length < 4) {
                            if (mensaje.contains("CREATE") || mensaje.contains("LIST") || mensaje.contains("JOIN")) {

                                if (parsedMensaje[0].equalsIgnoreCase("LIST")) {
                                    encyptMensaje = encrypt(mensaje, serverPublicKey);
                                    out.writeObject(encyptMensaje);

                                } else if (parsedMensaje[0].equalsIgnoreCase("JOIN")) {
                                    encyptMensaje = encrypt(mensaje, serverPublicKey);
                                    out.writeObject(encyptMensaje);
                                    

                                } else if (parsedMensaje[0].equalsIgnoreCase("CREATE")) {

                                    if (parsedMensaje.length > 1 && parsedMensaje.length < 4) {
                                        if (!parsedMensaje[1].contains("#") && parsedMensaje.length == 2) {
                                            encyptMensaje = encrypt(mensaje, serverPublicKey);
                                            out.writeObject(encyptMensaje);
                                        }
                                        else if(!parsedMensaje[1].contains("#") && !parsedMensaje[2].contains("#") && parsedMensaje.length == 3) {
                                        	encyptMensaje = encrypt(mensaje, serverPublicKey);
                                            out.writeObject(encyptMensaje);
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println("El mensaje no se reconoce");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
