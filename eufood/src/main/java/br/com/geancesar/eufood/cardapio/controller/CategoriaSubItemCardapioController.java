package br.com.geancesar.eufood.cardapio.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.geancesar.eufood.cardapio.interceptor.CadastrarCategoriaSubItemInterceptor;
import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;
import br.com.geancesar.eufood.cardapio.repository.CategoriaSubItemRepository;
import br.com.geancesar.eufood.cardapio.validator.CategoriaSubItemValidador;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaRequisicao;
import br.com.geancesar.eufood.util.model.RespostaValidacao;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("restaurante/sub_item/categoria")
public class CategoriaSubItemCardapioController {

	@Autowired
	private CategoriaSubItemRepository repository;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	TokenService tokenService;

	@Autowired
	LoginUsuarioRepository usuarioRepository;

	@Autowired
	private HttpServletRequest request;

	@PostMapping(value = "/cadastrar")
	public ResponseEntity<RespostaRequisicao> cadastrarCategoria(
			@RequestBody CadastrarCategoriaSubItemInterceptor interceptor) {
		RespostaValidacao respostaValidacao = CategoriaSubItemValidador.getInstance().validaDadosCadastro(interceptor);
		if (!respostaValidacao.isOk()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RespostaRequisicao(false, HttpStatus.BAD_REQUEST.value(), respostaValidacao.getMensagem()));
		}

		if (!validaTokenRestaurante(interceptor.getUuidRestaurante())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespostaRequisicao(false,
					HttpStatus.BAD_REQUEST.value(), "Token não condiz com o uuid de restaurante."));
		}

		Optional<Restaurante> restaurante = restauranteRepository.findById(interceptor.getUuidRestaurante());

		CategoriaSubItem categoria = CategoriaSubItemValidador.getInstance().getCategoria(interceptor,
				restaurante.get());
		repository.save(categoria);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new RespostaRequisicao(true, HttpStatus.CREATED.value(), categoria.getUuid()));
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
