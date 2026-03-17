package br.com.geancesar.eufood.pedido.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.pedido.model.ControlePedidosRestaurante;
import br.com.geancesar.eufood.pedido.model.rest.consulta.ConsultaControlePedidoRest;
import br.com.geancesar.eufood.pedido.repository.ControlePedidosRestauranteRepository;
import br.com.geancesar.eufood.pedido.service.ControlePedidoService;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("controle_pedido")
public class ControlePedidosRestauranteController {

	@Autowired
	RestauranteRepository restauranteRepository;

	@Autowired
	ControlePedidosRestauranteRepository repository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginUsuarioRepository usuarioRepository;

	@Autowired
	private ControlePedidoService controlePedidoService;

	@GetMapping(value = "consultar")
	public ResponseEntity<ConsultaControlePedidoRest> getControle(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {
		if (!validaTokenRestaurante(uuidRestaurante)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		Optional<ControlePedidosRestaurante> controle = repository.findByRestauranteUuid(uuidRestaurante);
		Optional<Restaurante> restaurante = restauranteRepository.findById(uuidRestaurante);

		if (!controle.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		controlePedidoService.atualizaStatusPedidoConcluido(restaurante.get());

		ConsultaControlePedidoRest rest = ConsultaControlePedidoRest.fromControlePedido(controle.get());
		return ResponseEntity.status(HttpStatus.OK).body(rest);
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
