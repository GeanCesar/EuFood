package br.com.geancesar.eufood.cardapio.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.geancesar.eufood.cardapio.interceptor.CadastrarCategoriaItemInterceptor;
import br.com.geancesar.eufood.cardapio.model.CategoriaItemCardapio;
import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;
import br.com.geancesar.eufood.cardapio.model.CategoriaSubItemRest;
import br.com.geancesar.eufood.cardapio.model.ItemSubItem;
import br.com.geancesar.eufood.cardapio.model.SubItemCardapioRest;
import br.com.geancesar.eufood.cardapio.repository.CategoriaItemRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemSubItemRepository;
import br.com.geancesar.eufood.cardapio.validator.CategoriaItemValidador;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaValidacao;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("restaurante/categoria")
public class CategoriaItemCardapioController {

	@Autowired
	ItemCardapioRepository itemCardapioRepository;

	@Autowired
	CategoriaItemRepository repository;

	@Autowired
	RestauranteRepository restauranteRepository;

	@Autowired
	TokenService tokenService;

	@Autowired
	LoginUsuarioRepository usuarioRepository;

	@Autowired
	ItemSubItemRepository subItemRepository;

	@Autowired
	ItemCardapioRepository itemRepository;

	@Autowired
	private HttpServletRequest request;

	CategoriaItemCardapioController(ItemCardapioRepository itemCardapioRepository) {
		this.itemCardapioRepository = itemCardapioRepository;
	}

	@PostMapping(value = "/cadastrar")
	public ResponseEntity<String> cadastrarCategoria(@RequestBody CadastrarCategoriaItemInterceptor interceptor) {
		RespostaValidacao respostaValidacao = CategoriaItemValidador.getInstance().validaDadosCadastro(interceptor);
		if (!respostaValidacao.isOk()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaValidacao.getMensagem());
		}

		if (!validaTokenRestaurante(interceptor.getUuidRestaurante())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token não condiz com o uuid de restaurante.");
		}

		Optional<Restaurante> restaurante = restauranteRepository.findById(interceptor.getUuidRestaurante());

		CategoriaItemCardapio categoria = CategoriaItemValidador.getInstance().getCategoria(interceptor,
				restaurante.get());
		repository.save(categoria);

		return ResponseEntity.status(HttpStatus.CREATED).body(categoria.getUuid());
	}

	@GetMapping(value = "/listar")
	public ResponseEntity<List<CategoriaItemCardapio>> listarCategorias(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {
		List<CategoriaItemCardapio> categorias = repository.findAllByRestauranteUuidOrderByOrdemAsc(uuidRestaurante);

		if (categorias == null || categorias.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(categorias);
	}

	@GetMapping(value = "listar/item")
	public ResponseEntity<List<CategoriaSubItemRest>> listarSubItens(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante,
			@RequestParam(value = "uuid-item") String uuidItem) {

		List<ItemSubItem> subItems = subItemRepository.findAllByItemPrincipalUuidOrderByOrdem(uuidItem);

		if (subItems == null || subItems.size() == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		List<CategoriaSubItemRest> categorias = processaSubItems(subItems);

		return ResponseEntity.status(HttpStatus.OK).body(categorias);
	}

	private List<CategoriaSubItemRest> processaSubItems(List<ItemSubItem> subItems) {
		List<CategoriaSubItemRest> categorias = new ArrayList<>();

		Map<CategoriaSubItem, List<ItemSubItem>> categoriasComItens = subItems.stream()
				.collect(Collectors.groupingBy(ItemSubItem::getCategoriaSubItem));

		for (Entry<CategoriaSubItem, List<ItemSubItem>> categoria : categoriasComItens.entrySet()) {
			for (ItemSubItem item : categoria.getValue()) {
				CategoriaSubItemRest cat = new CategoriaSubItemRest();
				cat.setDescricao(categoria.getKey().getDescricao());
				cat.setQuantidadeMaxima(categoria.getKey().getQuantidadeMaxima());
				cat.setQuantidadeMinima(categoria.getKey().getQuantidadeMinima());
				cat.setUuid(categoria.getKey().getUuid());

				SubItemCardapioRest subItem = SubItemCardapioRest.fromItemCardapioAndAssociacao(item.getSubItem(),
						item);
				if (categorias.contains(cat)) {
					categorias.get(categorias.indexOf(cat)).getItens().add(subItem);
				} else {
					cat.getItens().add(subItem);
					categorias.add(cat);
				}
			}
		}
		return categorias;
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
