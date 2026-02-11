package br.com.geancesar.eufood.pedido.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_pedido_item")
public class PedidoItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(insertable = false, updatable = false)
	private String uuid;

	private String uuidItem;

	@ManyToOne
	@JoinColumn(name = "uuid_pedido", nullable = false)
	@JsonIgnore
	private Pedido pedido;

	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "itemPrincipal")
	private List<PedidoSubItem> subItems;

	private BigDecimal preco;
	private BigDecimal quantidade;
	private BigDecimal desconto;
	private BigDecimal valorTotal;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getUuidItem() {
		return uuidItem;
	}

	public void setUuidItem(String uuidItem) {
		this.uuidItem = uuidItem;
	}

	public List<PedidoSubItem> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<PedidoSubItem> subItems) {
		this.subItems = subItems;
	}

}
