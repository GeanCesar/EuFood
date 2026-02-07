package br.com.geancesar.eufood.pedido.model.rest.criacao;

import java.math.BigDecimal;
import java.util.List;

public class CriacaoPedidoItemRest {

	private String uuid;
	private BigDecimal quantidade;
	private List<CriacaoPedidoSubItemRest> subItems;

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

	public List<CriacaoPedidoSubItemRest> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<CriacaoPedidoSubItemRest> subItems) {
		this.subItems = subItems;
	}

}
