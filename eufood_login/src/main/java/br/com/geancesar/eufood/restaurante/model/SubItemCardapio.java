package br.com.geancesar.eufood.restaurante.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;

@Entity(name = "tb_sub_item_cardapio")
public class SubItemCardapio {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String uuid;
	
	@ManyToOne
	private ItemCardapio item;
	
	@NotEmpty
	private String nome;
	
	private String descricao;

	private BigDecimal valor;
	
	private String imagem;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public ItemCardapio getItem() {
		return item;
	}

	public void setItem(ItemCardapio item) {
		this.item = item;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	
	public String getImagem() {
		return imagem;
	}

}
