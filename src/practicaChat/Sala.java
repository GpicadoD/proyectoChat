package practicaChat;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;

public class Sala {
	private String nombre;
	private String clave;
	private ArrayList<Usuario> usesrList;

	public Sala(String nombre, String clave, ArrayList<Usuario> usesrList) {
		this.nombre = nombre;
		this.clave = clave;
		this.usesrList = usesrList;
	}

	public Sala() {

	}

	public String getNombre() {
		return nombre;
	}

	public String getClave() {
		return clave;
	}

	public ArrayList<Usuario> getUsesrList() {
		return usesrList;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public void setUsesrList(ArrayList<Usuario> usesrList) {
		this.usesrList = usesrList;
	}

	@Override
	public String toString() {
		return "Sala [nombre=" + nombre + ", clave=" + clave + ", usesrList=" + usesrList + "]";
	}
	
	public String encrypt(String mensaje, PublicKey clientPublicKey) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
		byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}
	
	public Usuario exitRoom(Usuario client) {
		for (Usuario user : this.usesrList) {
			if (user == client) {
				return user;
			}
		}
		return null;
	}
	
	public void broadcastMessage(Usuario sender, String message) throws Exception {
		for (Usuario user : usesrList) {
			if (user != sender) user.getOut().writeObject(encrypt(message, user.getPublicKey()));
		}
	}

}
