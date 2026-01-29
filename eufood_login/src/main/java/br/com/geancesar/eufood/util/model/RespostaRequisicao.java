package br.com.geancesar.eufood.util.model;

public class RespostaRequisicao {

	public RespostaRequisicao(boolean ok, int status, Object extra) {
		this.status = status;
		this.extra = extra;
		this.ok = ok;
	}
	
	public RespostaRequisicao(boolean ok, int status) {
		this.status = status;
		this.ok = ok;
	}
	
	public RespostaRequisicao() {}

	private int status;
	private Object extra;
	private boolean ok;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

}
