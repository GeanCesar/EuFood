package br.com.geancesar.eufood.cardapio.model;

public enum TipoItem {

	ITEM("ITEM"), SUBITEM("SUBITEM");

	TipoItem(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
