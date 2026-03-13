package br.com.geancesar.eufood.cardapio.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.geancesar.eufood.cardapio.interceptor.CadastrarCategoriaSubItemInterceptor;
import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;
import br.com.geancesar.eufood.cardapio.model.ItemSubItem;
import br.com.geancesar.eufood.cardapio.repository.CategoriaSubItemRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemSubItemRepository;
import br.com.geancesar.eufood.cardapio.validator.CategoriaSubItemValidador;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaRequisicao;
import br.com.geancesar.eufood.util.model.RespostaValidacao;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("restaurante/sub_item/categoria")
public class CategoriaSubItemCardapioController {

	@Autowired
	private CategoriaSubItemRepository repository;

	@Autowired
	private ItemSubItemRepository repositoryAssociacao;

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

	@PatchMapping(value = "/atualizar")
	public ResponseEntity<RespostaRequisicao> atualizarCategoria(@RequestParam(value = "uuid-categoria") String uuidCategoria, 
			@RequestBody CadastrarCategoriaSubItemInterceptor interceptor) {		
		Optional<CategoriaSubItem> categoria = repository.findById(uuidCategoria); 
		
		if(!categoria.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespostaRequisicao(false,
					HttpStatus.BAD_REQUEST.value(), "Categoria não encontrada"));
		}
		
		if (!validaTokenRestaurante(categoria.get().getRestaurante().getUuid())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespostaRequisicao(false,
					HttpStatus.BAD_REQUEST.value(), "Token não condiz com o uuid de restaurante."));
		}
		
		interceptor.atualizaCategoria(categoria.get());
		repository.save(categoria.get());

		return ResponseEntity.status(HttpStatus.OK)
				.body(new RespostaRequisicao(true, HttpStatus.OK.value(), categoria.get()));
	}
	
	@DeleteMapping(value = "/remover")
	public ResponseEntity<RespostaRequisicao> removerCategoria(@RequestParam(value = "uuid-categoria") String uuidCategoria) {		
		Optional<CategoriaSubItem> categoria = repository.findById(uuidCategoria); 
		
		if(!categoria.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespostaRequisicao(false,
					HttpStatus.BAD_REQUEST.value(), "Categoria não encontrada"));
		}
		
		if (!validaTokenRestaurante(categoria.get().getRestaurante().getUuid())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespostaRequisicao(false,
					HttpStatus.BAD_REQUEST.value(), "Token não condiz com o uuid de restaurante."));
		}
		
		List<ItemSubItem> itensSub = repositoryAssociacao.findAllByCategoriaSubItemUuid(uuidCategoria);
		for(ItemSubItem item : itensSub) {
			repositoryAssociacao.delete(item);
		}
		repository.delete(categoria.get());

		return ResponseEntity.status(HttpStatus.OK)
				.body(new RespostaRequisicao(true, HttpStatus.OK.value(), categoria.get()));
	}

	@GetMapping(value = "/listar")
	public ResponseEntity<RespostaRequisicao> listarCategorias(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {
		List<CategoriaSubItem> categorias = repository.findAllByRestauranteUuid(uuidRestaurante);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new RespostaRequisicao(true, HttpStatus.OK.value(), categorias));
	}

	@GetMapping(value = "/listar_sub_items")
	public ResponseEntity<RespostaRequisicao> listarSubItemsCategorias(
			@RequestParam(value = "uuid-item-principal") String uuidItem,
			@RequestParam(value = "uuid-categoria") String uuidCategoria) {
		List<ItemSubItem> itemSubItens = repositoryAssociacao
				.findAllByItemPrincipalUuidAndCategoriaSubItemUuidOrderByOrdem(uuidItem, uuidCategoria);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new RespostaRequisicao(true, HttpStatus.OK.value(), itemSubItens));
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
