package br.com.geancesar.eufood.pedido.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.pedido.model.Pedido;
import br.com.geancesar.eufood.pedido.model.rest.consulta.ConsultaPedidoRest;
import br.com.geancesar.eufood.pedido.repository.PedidoRepository;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaRequisicao;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("pedido")
public class PedidoController {

	@Autowired
	ItemCardapioRepository itemCardapioRepository;

	@Autowired
	PedidoRepository repository;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private LoginUsuarioRepository usuarioRepository;

	PedidoController(ItemCardapioRepository itemCardapioRepository) {
		this.itemCardapioRepository = itemCardapioRepository;
	}

	@GetMapping("/listar")
	public ResponseEntity<RespostaRequisicao> listar(@RequestParam(value = "uuid-usuario") String uuidUsuario) {

		if (!validaToken(uuidUsuario)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespostaRequisicao(true,
					HttpStatus.BAD_REQUEST.value(), "Token não condiz com o uuid do usuário informado"));
		}

		List<ConsultaPedidoRest> pedidos = buscaPedidos(uuidUsuario);

		if (pedidos != null && pedidos.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new RespostaRequisicao(true, HttpStatus.OK.value(), pedidos));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new RespostaRequisicao(true, HttpStatus.NOT_FOUND.value(), null));
	}

	private List<ConsultaPedidoRest> buscaPedidos(String uuidUsuario) {
		List<ConsultaPedidoRest> pedidosRest = new ArrayList<>();

		List<Pedido> pedidos = repository.findAllByUsuarioUuid(uuidUsuario);
		for (Pedido pedido : pedidos) {
			ConsultaPedidoRest p = new ConsultaPedidoRest();
			pedidosRest.add(p.fromPedido(pedido, itemCardapioRepository));
		}

		return pedidosRest;
	}

	private boolean validaToken(String uuid) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return false;
		}
		String token = authHeader.replace("Bearer ", "");

		String login = tokenService.validateToken(token);
		Optional<Usuario> usuario = usuarioRepository.findByTelefone(login);

		if (usuario.isPresent() && usuario.get().getUuid().equalsIgnoreCase(uuid)) {
			return true;
		}
		return false;
	}

}
