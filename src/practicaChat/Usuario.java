package practicaChat;

import java.io.ObjectOutputStream;
import java.security.PublicKey;

public class Usuario {
	private ObjectOutputStream out;
	private PublicKey publicKey;

	public Usuario() {
	}

	public Usuario(ObjectOutputStream out, PublicKey publicKey) {
		this.out = out;
		this.publicKey = publicKey;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String toString() {
		return "Usuario [out=" + out + ", publicKey=" + publicKey + "]";
	}

}
