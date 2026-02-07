package br.com.geancesar.eufood.util.model;

public class RespostaValidacao {

	private boolean ok;
	private String mensagem;

	public RespostaValidacao() {
	}

	public RespostaValidacao(String mensagem) {
		this.mensagem = mensagem;
		this.ok = false;
	}

	public RespostaValidacao(String mensagem, boolean ok) {
		this.mensagem = mensagem;
		this.ok = ok;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

}
