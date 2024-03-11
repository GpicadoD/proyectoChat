package practicaChat;

/**
 * 
 * Clase que contiene la información que se comparte entre los threads lector y
 * escritor del cliente.
 * 
 * @author Javier Olivan y Geancarlos Picado
 *
 */
public class SharedData {
	private Boolean inChat;

	public SharedData(Boolean inChat) {
		this.inChat = inChat;
	}

	public Boolean getInChat() {
		return inChat;
	}

	public void setInChat(Boolean inChat) {
		this.inChat = inChat;
	}

	@Override
	public String toString() {
		return "SharedData [inChat=" + inChat + "]";
	}

}
