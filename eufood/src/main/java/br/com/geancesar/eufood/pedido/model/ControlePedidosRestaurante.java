package br.com.geancesar.eufood.pedido.model;

import br.com.geancesar.eufood.restaurante.model.Restaurante;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_controle_pedidos")
public class ControlePedidosRestaurante {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;

	private boolean possuiNovoPedido;

	@ManyToOne
	@JoinColumn(name = "uuid_restaurante", nullable = false)
	private Restaurante restaurante;
	
	public ControlePedidosRestaurante() {}
	
	public ControlePedidosRestaurante(Restaurante restaurante) {
		setRestaurante(restaurante);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isPossuiNovoPedido() {
		return possuiNovoPedido;
	}

	public void setPossuiNovoPedido(boolean possuiNovoPedido) {
		this.possuiNovoPedido = possuiNovoPedido;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

}
