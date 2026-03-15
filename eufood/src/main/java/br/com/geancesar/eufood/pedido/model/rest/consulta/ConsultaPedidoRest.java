package br.com.geancesar.eufood.pedido.model.rest.consulta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.geancesar.eufood.cardapio.model.ItemCardapio;
import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.pedido.model.Pedido;
import br.com.geancesar.eufood.pedido.model.PedidoItem;
import br.com.geancesar.eufood.pedido.model.PedidoStatus;
import br.com.geancesar.eufood.pedido.model.PedidoSubItem;
import br.com.geancesar.eufood.pedido.repository.PedidoStatusRepository;

public class ConsultaPedidoRest {

	private List<ConsultaPedidoItemRest> items;
	private String numeroPedido;
	private String uuidRestaurante;
	private String uuidUsuario;
	private String uuidPedido;
	private BigDecimal valorTotal;
	private BigDecimal valorFrete;
	private Date dataCriacao;
	private List<ConsultaPedidoStatusRest> status;

	public String getUuidUsuario() {
		return uuidUsuario;
	}

	public void setUuidUsuario(String uuidUsuario) {
		this.uuidUsuario = uuidUsuario;
	}

	public void setUuidRestaurante(String uuidRestaurante) {
		this.uuidRestaurante = uuidRestaurante;
	}

	public String getUuidRestaurante() {
		return uuidRestaurante;
	}

	public List<ConsultaPedidoItemRest> getItems() {
		return items;
	}

	public void setItems(List<ConsultaPedidoItemRest> items) {
		this.items = items;
	}

	public String getUuidPedido() {
		return uuidPedido;
	}

	public void setUuidPedido(String uuidPedido) {
		this.uuidPedido = uuidPedido;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public void setStatus(List<ConsultaPedidoStatusRest> status) {
		this.status = status;
	}

	public List<ConsultaPedidoStatusRest> getStatus() {
		return status;
	}

	public ConsultaPedidoRest fromPedido(Pedido pedido, ItemCardapioRepository itemRepository,
			PedidoStatusRepository pedidoStatusRepository) {
		ConsultaPedidoRest rest = new ConsultaPedidoRest();
		rest.setDataCriacao(pedido.getDataHora());
		rest.setUuidPedido(pedido.getUuid());
		rest.setUuidUsuario(pedido.getUsuario().getUuid());
		rest.setNumeroPedido(pedido.getNumeroPedido());
		rest.setUuidRestaurante(pedido.getRestaurante().getUuid());
		rest.setValorFrete(pedido.getValorFrete());
		rest.setValorTotal(pedido.getValorTotal());
		rest.setItems(new ArrayList<>());

		for (PedidoItem pedidoItem : pedido.getItems()) {
			ConsultaPedidoItemRest item = new ConsultaPedidoItemRest();
			item.setDesconto(pedidoItem.getDesconto());
			item.setPreco(pedidoItem.getPreco());
			item.setQuantidade(pedidoItem.getQuantidade());
			item.setUuid(pedidoItem.getUuidItem());
			item.setValorTotal(pedidoItem.getValorTotal());
			item.setSubItems(new ArrayList<>());

			ItemCardapio itemConsultado = itemRepository.findById(item.getUuid()).get();
			item.setDescricao(itemConsultado.getDescricao());
			item.setNome(itemConsultado.getNome());

			if (pedidoItem.getSubItems() != null) {
				for (PedidoSubItem subItem : pedidoItem.getSubItems()) {
					ItemCardapio subItemConsultado = itemRepository.findById(subItem.getUuidItem()).get();
					ConsultaPedidoSubItemRest sub = new ConsultaPedidoSubItemRest();
					sub.setDescricao(subItemConsultado.getDescricao());
					sub.setNome(subItemConsultado.getNome());
					sub.setQuantidade(subItem.getQuantidade());
					sub.setUuid(subItem.getUuid());
					sub.setValorTotal(subItem.getValorTotal());
					item.getSubItems().add(sub);
				}
			}

			rest.getItems().add(item);
		}

		List<PedidoStatus> todosStatus = pedidoStatusRepository.findAllByPedidoUuidOrderByDataHoraDesc(pedido.getUuid());
		rest.setStatus(new ArrayList<>());
		for (PedidoStatus status : todosStatus) {
			ConsultaPedidoStatusRest statusRest = new ConsultaPedidoStatusRest();
			statusRest.setDataHora(status.getDataHora());
			statusRest.setStatus(status.getStatus());
			statusRest.setUuid(status.getUuid());
			rest.getStatus().add(statusRest);
		}

		return rest;
	}

}
