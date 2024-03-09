package practicaChat;

import java.util.ArrayList;

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

}
