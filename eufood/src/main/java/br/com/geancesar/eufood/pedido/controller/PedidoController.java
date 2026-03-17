package br.com.geancesar.eufood.pedido.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.pedido.model.Pedido;
import br.com.geancesar.eufood.pedido.model.PedidoStatus;
import br.com.geancesar.eufood.pedido.model.Status;
import br.com.geancesar.eufood.pedido.model.rest.consulta.ConsultaPedidoRest;
import br.com.geancesar.eufood.pedido.model.rest.consulta.ConsultaPedidoStatusRest;
import br.com.geancesar.eufood.pedido.repository.PedidoRepository;
import br.com.geancesar.eufood.pedido.repository.PedidoStatusRepository;
import br.com.geancesar.eufood.pedido.service.ControlePedidoService;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.Util;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("pedido")
public class PedidoController {

	@Autowired
	ItemCardapioRepository itemCardapioRepository;

	@Autowired
	PedidoRepository repository;

	@Autowired
	RestauranteRepository restauranteRepository;

	@Autowired
	PedidoStatusRepository pedidoStatusRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginUsuarioRepository usuarioRepository;
	
	@Autowired
	private ControlePedidoService controlePedidoService;

	PedidoController(ItemCardapioRepository itemCardapioRepository) {
		this.itemCardapioRepository = itemCardapioRepository;
	}

	@GetMapping("/listar")
	public ResponseEntity<List<ConsultaPedidoRest>> listar() {
		String uuidUsuario = tokenService.getUuidUsuario();

		List<ConsultaPedidoRest> pedidos = buscaPedidos(uuidUsuario, null, null);

		if (pedidos != null && pedidos.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(pedidos);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@GetMapping("/restaurante/listar")
	public ResponseEntity<List<ConsultaPedidoRest>> listarPorRestaurante(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante,
			@RequestParam(value = "data-inicio") String dataInicio) {

		Date data = Util.getInstance().getDate(dataInicio);

		if (!validaTokenRestaurante(uuidRestaurante) || data == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		List<ConsultaPedidoRest> pedidos = buscaPedidos(null, uuidRestaurante, data);
		if (pedidos != null && pedidos.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(pedidos);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PatchMapping("atualizar/status")
	public ResponseEntity<String> atualizarStatus(@RequestParam(value = "uuid-pedido") String uuidPedido,
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante, @RequestParam String status) {

		if (!validaTokenRestaurante(uuidRestaurante)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		Status novoStatus = Status.valueOf(status.toUpperCase());
		if (novoStatus == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status inválido");
		}

		Optional<Pedido> pedido = repository.findById(uuidPedido);
		if (pedido.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum pedido encontrado");
		}
		
		if(podeAtualizarStatus(pedido.get(), novoStatus)) {
			criaStatus(novoStatus, pedido.get());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status inválido");
		}

		controlePedidoService.atualizaControle(uuidRestaurante);
		return ResponseEntity.status(HttpStatus.OK).body("");
	}

	private boolean podeAtualizarStatus(Pedido pedido, Status novoStatus) {
		ConsultaPedidoRest p = new ConsultaPedidoRest();
		p = p.fromPedido(pedido, itemCardapioRepository, pedidoStatusRepository);
		
		ConsultaPedidoStatusRest ultimoStatus = p.getStatus().get(0);
		switch(ultimoStatus.getStatus()) {
			case "CANCELADO": 
				return false;
			case "CRIADO":
				if(novoStatus == Status.CONFIRMADO)
					return true;
				break;
			case "CONFIRMADO":
				if(novoStatus == Status.DESPACHADO)
					return true;
				break;
			case "CONCLUIDO":
				return false;
			default:
				break;
		}
		
		if(novoStatus == Status.CANCELADO) {
			return true;
		}
		
		return false;
	}

	private void criaStatus(Status novoStatus, Pedido pedido) {
		PedidoStatus pedidoStatus = new PedidoStatus();
		pedidoStatus.setDataHora(new Date());
		pedidoStatus.setStatus(novoStatus.toString());
		pedidoStatus.setPedido(pedido);
		pedidoStatusRepository.save(pedidoStatus);
	}

	private List<ConsultaPedidoRest> buscaPedidos(String uuidUsuario, String uuidRestaurante, Date dataInicio) {
		List<ConsultaPedidoRest> pedidosRest = new ArrayList<>();

		List<Pedido> pedidos = null;

		if (uuidUsuario != null) {
			pedidos = repository.findAllByUsuarioUuidOrderByDataHoraDesc(uuidUsuario);
		} else if (uuidRestaurante != null) {
			pedidos = repository.findAllByRestauranteUuidAndDataHoraAfterOrderByDataHoraDesc(uuidRestaurante, dataInicio);
		}

		if (pedidos != null) {
			for (Pedido pedido : pedidos) {
				ConsultaPedidoRest p = new ConsultaPedidoRest();
				pedidosRest.add(p.fromPedido(pedido, itemCardapioRepository, pedidoStatusRepository));
			}
		}

		return pedidosRest;
	}

	private boolean validaTokenRestaurante(String uuidRestaurante) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return false;
		}
		String token = authHeader.replace("Bearer ", "");

		String login = tokenService.validateToken(token);
		Optional<Usuario> usuario = usuarioRepository.findByTelefone(login);

		if (usuario.isPresent()) {
			Optional<Restaurante> restaurante = restauranteRepository.findById(uuidRestaurante);
			if (restaurante.isPresent() && restaurante.get().getUsuario().getUuid().equals(usuario.get().getUuid())) {
				return true;
			}
		}
		return false;
	}
}
