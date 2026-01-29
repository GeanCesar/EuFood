package br.com.geancesar.eufood.restaurante.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.restaurante.interceptor.CadastrarCategoriaItemInterceptor;
import br.com.geancesar.eufood.restaurante.model.CategoriaItemCardapio;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.CategoriaItemRepository;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.restaurante.validator.CategoriaItemValidador;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaRequisicao;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("restaurante/categoria")
public class CategoriaItemCardapioController {

	@Autowired
	CategoriaItemRepository repository;

	@Autowired
	RestauranteRepository restauranteRepository;

	@Autowired
	TokenService tokenService;

	@Autowired
	LoginUsuarioRepository usuarioRepository;

	@Autowired
	private HttpServletRequest request;

	@PostMapping(value = "/cadastrar")
	public ResponseEntity<RespostaRequisicao> cadastrarCategoria(
			@RequestBody CadastrarCategoriaItemInterceptor interceptor) {
		String mensagem = CategoriaItemValidador.getInstance().validaDadosCadastro(interceptor);
		if (mensagem != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new RespostaRequisicao(false, HttpStatus.BAD_REQUEST.value(), mensagem));
		}

		if (!validaTokenRestaurante(interceptor.getUuidRestaurante())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespostaRequisicao(false,
					HttpStatus.BAD_REQUEST.value(), "Token não condiz com o uuid de restaurante."));
		}

		Optional<Restaurante> restaurante = restauranteRepository.findById(interceptor.getUuidRestaurante());

		CategoriaItemCardapio categoria = CategoriaItemValidador.getInstance().getCategoria(interceptor,
				restaurante.get());
		repository.save(categoria);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new RespostaRequisicao(true, HttpStatus.CREATED.value(), categoria.getUuid()));
	}

	@GetMapping(value = "/listar")
	public ResponseEntity<RespostaRequisicao> listarCategorias(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {
		List<CategoriaItemCardapio> categorias = repository.findAllByRestauranteUuidOrderByOrdemAsc(uuidRestaurante);

		if (categorias == null || categorias.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new RespostaRequisicao(false, HttpStatus.NOT_FOUND.value(), null));
		}
		
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(new RespostaRequisicao(true, HttpStatus.FOUND.value(), categorias));
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
