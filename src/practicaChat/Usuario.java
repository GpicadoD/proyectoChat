package practicaChat;

import java.io.ObjectOutputStream;

public class Usuario {
	private ObjectOutputStream out;
	private String publicKey;

	public Usuario() {
	}

	public Usuario(ObjectOutputStream out, String publicKey) {
		this.out = out;
		this.publicKey = publicKey;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String toString() {
		return "Usuario [out=" + out + ", publicKey=" + publicKey + "]";
	}

}
