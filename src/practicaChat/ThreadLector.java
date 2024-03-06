package practicaChat;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;

public class ThreadLector extends Thread{
	
	private DataInputStream in;
	private Socket cs;
	
	public ThreadLector(DataInputStream in, Socket cs) {
		super();
		this.in = in;
		this.cs = cs;
	}
	
	public void run() {
		try {
			System.out.println(in.readUTF());
			
		} catch (Exception e) {
			
			
		}
		
		
	}
	
}
