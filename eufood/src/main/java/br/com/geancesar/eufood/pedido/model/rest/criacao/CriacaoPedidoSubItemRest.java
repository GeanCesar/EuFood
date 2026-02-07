package br.com.geancesar.eufood.pedido.model.rest.criacao;

import java.math.BigDecimal;

public class CriacaoPedidoSubItemRest {

	private String uuid;
	private BigDecimal quantidade;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

}
