package practicaChat;

import java.io.DataOutputStream;

public class Usuario {
	private DataOutputStream out;
	private String publicKey;

	public Usuario() {
	}

	public Usuario(DataOutputStream out, String publicKey) {
		this.out = out;
		this.publicKey = publicKey;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setOut(DataOutputStream out) {
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
