package br.com.geancesar.eufood.pedido.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.pedido.model.Pedido;
import br.com.geancesar.eufood.pedido.model.rest.consulta.ConsultaPedidoRest;
import br.com.geancesar.eufood.pedido.repository.PedidoRepository;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaRequisicao;

@Controller
@RequestMapping("pedido")
public class PedidoController {

	@Autowired
	ItemCardapioRepository itemCardapioRepository;

	@Autowired
	PedidoRepository repository;

	@Autowired
	private TokenService tokenService;

	PedidoController(ItemCardapioRepository itemCardapioRepository) {
		this.itemCardapioRepository = itemCardapioRepository;
	}

	@GetMapping("/listar")
	public ResponseEntity<RespostaRequisicao> listar() {
		String uuidUsuario = tokenService.getUuidUsuario();

		List<ConsultaPedidoRest> pedidos = buscaPedidos(uuidUsuario);

		if (pedidos != null && pedidos.size() > 0) {
			return ResponseEntity.status(HttpStatus.FOUND)
					.body(new RespostaRequisicao(true, HttpStatus.FOUND.value(), pedidos));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new RespostaRequisicao(true, HttpStatus.NOT_FOUND.value(), null));
	}

	private List<ConsultaPedidoRest> buscaPedidos(String uuidUsuario) {
		List<ConsultaPedidoRest> pedidosRest = new ArrayList<>();

		List<Pedido> pedidos = repository.findAllByUsuarioUuidOrderByDataHoraDesc(uuidUsuario);
		for (Pedido pedido : pedidos) {
			ConsultaPedidoRest p = new ConsultaPedidoRest();
			pedidosRest.add(p.fromPedido(pedido, itemCardapioRepository));
		}

		return pedidosRest;
	}
}
