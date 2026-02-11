package br.com.geancesar.eufood.pedido.model.rest.criacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.geancesar.eufood.cardapio.model.ItemCardapio;
import br.com.geancesar.eufood.cardapio.model.ItemSubItem;
import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemSubItemRepository;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.pedido.model.Pedido;
import br.com.geancesar.eufood.pedido.model.PedidoItem;
import br.com.geancesar.eufood.pedido.model.PedidoSubItem;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;

public class CriacaoPedidoRest {

	private List<CriacaoPedidoItemRest> items;

	private String uuidRestaurante;

	private String uuidUsuario;

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

	public List<CriacaoPedidoItemRest> getItems() {
		return items;
	}

	public void setItems(List<CriacaoPedidoItemRest> items) {
		this.items = items;
	}

	public Pedido toPedido(RestauranteRepository restauranteRepository, LoginUsuarioRepository usuarioRepository,
			ItemCardapioRepository itemRepository, ItemSubItemRepository itemSubRepository) {
		Optional<Usuario> usuario = usuarioRepository.findById(getUuidUsuario());
		Optional<Restaurante> restaurante = restauranteRepository.findById(getUuidRestaurante());

		Pedido pedido = new Pedido();
		pedido.setDataHora(new Date());
		pedido.setRestaurante(restaurante.get());
		pedido.setUsuario(usuario.get());
		pedido.setItems(new ArrayList<>());

		BigDecimal valorTotal = BigDecimal.ZERO;
		for (CriacaoPedidoItemRest itemRest : items) {
			BigDecimal valorTotalItem = BigDecimal.ZERO;

			PedidoItem item = new PedidoItem();
			item.setQuantidade(itemRest.getQuantidade());
			item.setUuidItem(itemRest.getUuid());

			Optional<ItemCardapio> itemCardapio = itemRepository.findById(itemRest.getUuid());
			item.setPreco(itemCardapio.get().getValor());

			valorTotalItem = itemCardapio.get().getValor().multiply(itemRest.getQuantidade());
			item.setPedido(pedido);
			item.setDesconto(BigDecimal.ZERO);

			if (itemRest.getSubItems() != null) {
				item.setSubItems(new ArrayList<>());
				for (CriacaoPedidoSubItemRest subItem : itemRest.getSubItems()) {
					List<ItemSubItem> s = itemSubRepository.findAllByItemPrincipalUuidAndSubItemUuid(item.getUuidItem(),
							subItem.getUuid());

					PedidoSubItem sub = new PedidoSubItem();
					sub.setItemPrincipal(item);
					sub.setQuantidade(subItem.getQuantidade());
					sub.setUuidItem(subItem.getUuid());
					sub.setPreco(s.get(0).getSubItem().getValor());
					sub.setValorTotal(s.get(0).getSubItem().getValor().multiply(sub.getQuantidade()));

					valorTotalItem = valorTotalItem.add(sub.getValorTotal().multiply(item.getQuantidade()));
					item.getSubItems().add(sub);
				}
			}

			item.setValorTotal(valorTotalItem);
			valorTotal = valorTotal.add(item.getValorTotal());
			pedido.getItems().add(item);
		}

		// TODO Tornar o frete dinamico
		pedido.setValorFrete(BigDecimal.valueOf(4.99));
		pedido.setValorTotal(valorTotal.add(pedido.getValorFrete()));

		return pedido;
	}

}
