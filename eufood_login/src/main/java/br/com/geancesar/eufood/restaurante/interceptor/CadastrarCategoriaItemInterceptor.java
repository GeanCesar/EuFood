package br.com.geancesar.eufood.restaurante.interceptor;

public class CadastrarCategoriaItemInterceptor {

	private String descricao;
	private int ordem;
	private String uuidRestaurante;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public String getUuidRestaurante() {
		return uuidRestaurante;
	}

	public void setUuidRestaurante(String uuidRestaurante) {
		this.uuidRestaurante = uuidRestaurante;
	}

}
