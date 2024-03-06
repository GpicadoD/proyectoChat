package practicaChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * Clase Thread, que gestiona las acciones del Cliente con el que establece
 * conexión
 * 
 * @author GeancarlosPicado
 *
 */
public class ThreadChat extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private int id;
	private Socket cs;
	
	public ThreadChat(DataInputStream in, DataOutputStream out, int id, Socket cs) {
		this.in = in;
		this.out = out;
		this.id = id;
		this.cs = cs;
	}
	/*
	 * Pre: --- 
	 * Post:
	 */
	public void run() {
		try {
			String clave = (in.readUTF());
			System.out.println(clave);
			out.writeUTF("1234");
			out.writeUTF("Mensaje a thread");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}