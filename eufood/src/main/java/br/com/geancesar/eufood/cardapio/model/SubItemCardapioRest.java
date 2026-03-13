package br.com.geancesar.eufood.cardapio.model;

import java.math.BigDecimal;

public class SubItemCardapioRest {

	private String uuid;
	private String nome;
	private BigDecimal valor;
	private String descricao;
	private String imagem;
	private String tipoItem;
	private CategoriaItemCardapio categoria;
	private String uuidAssociacao;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public String getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}

	public CategoriaItemCardapio getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaItemCardapio categoria) {
		this.categoria = categoria;
	}

	public String getUuidAssociacao() {
		return uuidAssociacao;
	}

	public void setUuidAssociacao(String uuidAssociacao) {
		this.uuidAssociacao = uuidAssociacao;
	}

	public static SubItemCardapioRest fromItemCardapioAndAssociacao(ItemCardapio item, ItemSubItem associacao) {
		SubItemCardapioRest rest = new SubItemCardapioRest();
		rest.setUuid(item.getUuid());
		rest.setNome(item.getNome());
		rest.setValor(item.getValor());
		rest.setDescricao(item.getDescricao());
		rest.setImagem(item.getImagem());
		rest.setTipoItem(item.getTipoItem().toString());
		rest.setCategoria(item.getCategoria());
		rest.setUuidAssociacao(associacao.getUuid());
		return rest;
	}

}
