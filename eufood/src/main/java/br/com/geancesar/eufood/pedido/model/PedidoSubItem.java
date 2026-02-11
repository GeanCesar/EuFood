package br.com.geancesar.eufood.pedido.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_pedido_sub_item")
public class PedidoSubItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;

	private String uuidItem;

	private BigDecimal quantidade;

	private BigDecimal valorTotal;

	private BigDecimal preco;

	@ManyToOne
	@JoinColumn(name = "uuid_item_principal", nullable = false)
	@JsonIgnore
	private PedidoItem itemPrincipal;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuidItem() {
		return uuidItem;
	}

	public void setUuidItem(String uuidItem) {
		this.uuidItem = uuidItem;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
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

	public PedidoItem getItemPrincipal() {
		return itemPrincipal;
	}

	public void setItemPrincipal(PedidoItem itemPrincipal) {
		this.itemPrincipal = itemPrincipal;
	}

}
