package br.com.geancesar.eufood.cardapio.interceptor;

import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;

public class CadastrarCategoriaSubItemInterceptor {

	private String descricao;
	private int ordem;
	private String uuidRestaurante;
	private int quantidadeMinima;
	private int quantidadeMaxima;

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
	public int getQuantidadeMinima() {
		return quantidadeMinima;
	}

	public void setQuantidadeMinima(int quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}

	public int getQuantidadeMaxima() {
		return quantidadeMaxima;
	}

	public void setQuantidadeMaxima(int quantidadeMaxima) {
		this.quantidadeMaxima = quantidadeMaxima;
	}
	
	public void atualizaCategoria(CategoriaSubItem categoria) {
		categoria.setDescricao(getDescricao());
		categoria.setOrdem(getOrdem());
		categoria.setQuantidadeMaxima(getQuantidadeMaxima());
		categoria.setQuantidadeMinima(getQuantidadeMinima());		
	}

}
