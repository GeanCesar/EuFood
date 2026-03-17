package br.com.geancesar.eufood.pedido.model.rest.consulta;

import br.com.geancesar.eufood.pedido.model.ControlePedidosRestaurante;

public class ConsultaControlePedidoRest {

	private boolean possuiNovoPedido;

	public boolean isPossuiNovoPedido() {
		return possuiNovoPedido;
	}

	public void setPossuiNovoPedido(boolean possuiNovoPedido) {
		this.possuiNovoPedido = possuiNovoPedido;
	}

	public static ConsultaControlePedidoRest fromControlePedido(ControlePedidosRestaurante controle) {
		ConsultaControlePedidoRest rest = new ConsultaControlePedidoRest();
		rest.setPossuiNovoPedido(controle.isPossuiNovoPedido());
		return rest;
	}
}
