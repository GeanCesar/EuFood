package br.com.geancesar.eufood.pedido.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_pedido")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	@OneToMany(mappedBy = "pedido")
	private List<PedidoItem> items;

	private BigDecimal valorTotal;

	private Date dataHora;

	private StatusPedido statusAtual;

	private Usuario usuario;

	private Restaurante restaurante;


	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public List<PedidoItem> getItems() {
		return items;
	}

	public void setItems(List<PedidoItem> items) {
		this.items = items;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public StatusPedido getStatusAtual() {
		return statusAtual;
	}

	public void setStatusAtual(StatusPedido statusAtual) {
		this.statusAtual = statusAtual;
	}

}
