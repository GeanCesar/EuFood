package br.com.geancesar.eufood.pedido.model.rest.consulta;

import java.math.BigDecimal;
import java.util.List;

public class ConsultaPedidoItemRest {

	private String uuid;
	private BigDecimal quantidade;
	private BigDecimal valorTotal;
	private BigDecimal preco;
	private BigDecimal desconto;
	private String descricao;
	private String nome;
	private List<ConsultaPedidoSubItemRest> subItems;

	public String getUuid() {
		return uuid;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<ConsultaPedidoSubItemRest> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<ConsultaPedidoSubItemRest> subItems) {
		this.subItems = subItems;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
