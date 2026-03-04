package br.com.geancesar.eufood.pedido.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemSubItemRepository;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.pedido.model.Pedido;
import br.com.geancesar.eufood.pedido.model.PedidoItem;
import br.com.geancesar.eufood.pedido.model.PedidoStatus;
import br.com.geancesar.eufood.pedido.model.PedidoSubItem;
import br.com.geancesar.eufood.pedido.model.Status;
import br.com.geancesar.eufood.pedido.model.rest.criacao.CriacaoPedidoRest;
import br.com.geancesar.eufood.pedido.repository.PedidoItemRepository;
import br.com.geancesar.eufood.pedido.repository.PedidoRepository;
import br.com.geancesar.eufood.pedido.repository.PedidoStatusRepository;
import br.com.geancesar.eufood.pedido.repository.PedidoSubItemRepository;
import br.com.geancesar.eufood.pedido.validator.PedidoValidador;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaRequisicao;
import br.com.geancesar.eufood.util.model.RespostaValidacao;

@RestController
@RequestMapping("/pedido")
public class GeradorPedidoController {

	@Autowired
	PedidoRepository repository;

	@Autowired
	PedidoItemRepository pedidoItemRepository;

	@Autowired
	PedidoStatusRepository pedidoStatusRepository;

	@Autowired
	PedidoSubItemRepository pedidoSubItemRepository;

	@Autowired
	PedidoValidador validador;

	@Autowired
	RestauranteRepository restauranteRepository;

	@Autowired
	LoginUsuarioRepository usuarioRepository;

	@Autowired
	ItemCardapioRepository itemCardapioRepository;

	@Autowired
	ItemSubItemRepository itemSubRepository;
	
	@Autowired
	TokenService tokenService;

	@PostMapping("/criar")
	public ResponseEntity<RespostaRequisicao> criarPedido(@RequestBody CriacaoPedidoRest pedidoRest) {

		RespostaValidacao respostaValidacao = validador.validarCriacao(pedidoRest);
		if (!respostaValidacao.isOk()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RespostaRequisicao(true, HttpStatus.BAD_REQUEST.value(), respostaValidacao.getMensagem()));
		}

		Pedido pedido = pedidoRest.toPedido(restauranteRepository, usuarioRepository, itemCardapioRepository,
				itemSubRepository, tokenService);
		salvaPedido(pedido);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new RespostaRequisicao(true, HttpStatus.CREATED.value(), ""));

	}

	private boolean salvaPedido(Pedido pedido) {
		repository.save(pedido);

		PedidoStatus status = new PedidoStatus();
		status.setDataHora(pedido.getDataHora());
		status.setStatus(Status.AGUARDANDO_PAGAMENTO.toString());
		status.setPedido(pedido);
		pedidoStatusRepository.save(status);

		for (PedidoItem item : pedido.getItems()) {
			pedidoItemRepository.save(item);

			if (item.getSubItems() != null) {
				for (PedidoSubItem sub : item.getSubItems()) {
					pedidoSubItemRepository.save(sub);
				}
			}
		}

		return true;
	}

}
