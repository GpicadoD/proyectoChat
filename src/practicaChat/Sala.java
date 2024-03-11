package practicaChat;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * 
 * Clase que se encarga de crear un objeto sala, el cual contiene objetos
 * Usuarios en una lista y todas las funciones necesarias par gestionarlos
 * 
 * @author Javier Oliván y Geancarlos Picado
 *
 */
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

	/*
	 * Pre: ---
	 * Post: Función que encripta el mensaje que es enviado a todos los
	 * usuarios de la lista.
	 */
	public String encrypt(String mensaje, PublicKey clientPublicKey) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
		byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	/*
	 * Pre: ---
	 * Post: Función que busca a un usuario en la sala y lo elimina.
	 */
	public Usuario exitRoom(Usuario client) {
		for (Usuario user : this.usesrList) {
			if (user == client) {
				return user;
			}
		}
		return null;
	}
	
	/*
	 * Pre: --- 
	 * Post: Función que envía un mensaje encriptado a todos los integrantes de la sala.
	 */
	public void broadcastMessage(Usuario sender, String message) throws Exception {
		for (Usuario user : usesrList) {
			if (user != sender)
				user.getOut().writeObject(encrypt(message, user.getPublicKey()));
		}
	}

}
