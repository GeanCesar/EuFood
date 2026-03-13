package br.com.geancesar.eufood.cardapio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.geancesar.eufood.cardapio.interceptor.CadastrarItemCardapioInterceptor;
import br.com.geancesar.eufood.cardapio.model.CategoriaItemCardapio;
import br.com.geancesar.eufood.cardapio.model.ItemCardapio;
import br.com.geancesar.eufood.cardapio.model.TipoItem;
import br.com.geancesar.eufood.cardapio.repository.CategoriaItemRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("restaurante/item_cardapio")
public class ItemCardapioController {

	@Value(value = "${caminho.disco.imagens}")
	public String discoArquivos;

	private static final String CAMINHO_IMAGENS = "\\Eu Food\\Imagens\\restaurantes\\";

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private LoginUsuarioRepository usuarioRepository;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CategoriaItemRepository categoriaRepository;

	@Autowired
	private ItemCardapioRepository repository;

	@GetMapping("listar")
	public ResponseEntity<List<ItemCardapio>> listarItens(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {
		List<ItemCardapio> items = repository.findAllByRestauranteUuidAndTipoItemOrderByOrdem(uuidRestaurante,
				TipoItem.ITEM.toString());

		if (items != null && items.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(items);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PatchMapping("atualiza_ordem")
	public ResponseEntity<String> atualizaOrdemItem(@RequestParam(value = "uuid-item") String uuidItem,
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante, @RequestParam int ordem) {

		if (!validaTokenRestaurante(uuidRestaurante, uuidItem, null)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token não corresponde ao restaurante informado");
		}

		Optional<ItemCardapio> item = repository.findById(uuidItem);

		if (item == null || item.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado");
		}

		item.get().setOrdem(ordem);
		repository.save(item.get());

		return ResponseEntity.status(HttpStatus.OK).body("");
	}

	@GetMapping("buscar")
	public ResponseEntity<ItemCardapio> buscarItem(@RequestParam(value = "uuid-item") String uuidItem) {
		Optional<ItemCardapio> item = repository.findById(uuidItem);

		if (item != null && item.get() != null) {
			return ResponseEntity.status(HttpStatus.OK).body(item.get());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@DeleteMapping("remover")
	public ResponseEntity<String> removerItem(@RequestParam(value = "uuid-item") String uuidItem,
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {

		if (!validaTokenRestaurante(uuidRestaurante, uuidItem, null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token não condiz com restaurante informado");
		}

		Optional<ItemCardapio> item = repository.findById(uuidItem);

		if (item == null || !item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
		}

		repository.delete(item.get());

		return ResponseEntity.status(HttpStatus.OK).body("");

	}

	@GetMapping("listar_por_categoria")
	public ResponseEntity<List<ItemCardapio>> listarItensPorCategoria(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante,
			@RequestParam(value = "uuid-categoria") String uuidCategoria) {
		List<ItemCardapio> items = repository.findAllByRestauranteUuidAndCategoriaUuidAndTipoItemOrderByOrdem(
				uuidRestaurante, uuidCategoria, TipoItem.ITEM.toString());

		if (items != null && items.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(items);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrarItem(@RequestBody CadastrarItemCardapioInterceptor itemInterceptor) {
		if (!validaTokenRestaurante(itemInterceptor.getUuidRestaurante(), null, null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("UUID do restaurante não condiz com o token informado");
		}

		ItemCardapio item = itemInterceptor.cadastrar(restauranteRepository, TipoItem.ITEM, categoriaRepository);
		repository.save(item);

		return ResponseEntity.status(HttpStatus.CREATED).body(item.getUuid());
	}

	@PostMapping("/sub_item/cadastrar")
	public ResponseEntity<String> cadastrarSubItem(@RequestBody CadastrarItemCardapioInterceptor itemInterceptor) {
		if (!validaTokenRestaurante(itemInterceptor.getUuidRestaurante(), null, null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("UUID do restaurante não condiz com o token informado");
		}

		ItemCardapio item = itemInterceptor.cadastrar(restauranteRepository, TipoItem.SUBITEM, categoriaRepository);
		repository.save(item);

		return ResponseEntity.status(HttpStatus.CREATED).body(item.getUuid());
	}

	@PutMapping("/associar_categoria")
	public ResponseEntity<String> associarCategoria(@RequestParam(value = "uuid-categoria") String uuidCategoria,
			@RequestParam(value = "uuid-item") String uuidItem,
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {

		if (!validaTokenRestaurante(uuidRestaurante, uuidItem, uuidCategoria)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("UUID do restaurante / item ou categoria não condiz com o token informado");
		}

		Optional<CategoriaItemCardapio> categoria = categoriaRepository.findById(uuidCategoria);
		if (categoria == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID da categoria não encontrado");
		}

		Optional<ItemCardapio> item = repository.findById(uuidItem);
		item.get().setCategoria(categoria.get());
		repository.save(item.get());

		return ResponseEntity.status(HttpStatus.OK).body("Categoria associada com sucesso");

	}

	@PostMapping("/upload/imagem_perfil")
	public ResponseEntity<String> uploadImagemPerfil(@RequestParam MultipartFile file,
			@RequestParam(required = true, value = "uuid-restaurante") String uuidRestaurante,
			@RequestParam(required = true, value = "uuid-item-cardapio") String uuidItemCardapio) {
		try {
			if (!validaTokenRestaurante(uuidRestaurante, uuidItemCardapio, null)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("UUID do restaurante não condiz com o token informado");
			}

			Optional<ItemCardapio> item = repository.findById(uuidItemCardapio);
			if (!item.get().getRestaurante().getUuid().equals(uuidRestaurante)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID do item não condiz com o restaurante");
			}

			Path uploadPath = Paths.get(discoArquivos + CAMINHO_IMAGENS + uuidRestaurante);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Path filePath = uploadPath.resolve(file.getOriginalFilename());
			file.transferTo(filePath.toFile());

			item.get().setImagem(file.getOriginalFilename());

			repository.save(item.get());

			return ResponseEntity.status(HttpStatus.OK).body("");

		} catch (IOException | IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping(value = "/imagem_item")
	public @ResponseBody byte[] getImagemItem(
			@RequestParam(required = true, value = "uuid-item-cardapio") String uuidItemCardapio) {
		Optional<ItemCardapio> item = repository.findById(uuidItemCardapio);
		if (item.isPresent()) {
			InputStream stream = null;
			try {
				File imagem = new File(discoArquivos + CAMINHO_IMAGENS + item.get().getRestaurante().getUuid() + "\\"
						+ item.get().getImagem());
				stream = new FileInputStream(imagem);

				return IOUtils.toByteArray(stream);
			} catch (Exception e) {
				return null;
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

	private boolean validaTokenRestaurante(String uuidRestaurante, String uuidItemCardapio, String uuidCategoria) {
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
				if (uuidItemCardapio != null) {
					return validaItemRestaurante(uuidItemCardapio, uuidCategoria);
				}
				return true;
			}
		}
		return false;
	}

	private boolean validaItemRestaurante(String uuidItemCardapio, String uuidCategoria) {
		Optional<ItemCardapio> item = repository.findById(uuidItemCardapio);
		if (!item.isPresent()) {
			return false;
		}
		if (uuidCategoria != null) {
			return validaCategoria(uuidCategoria);
		}
		return true;

	}

	private boolean validaCategoria(String uuidCategoria) {
		Optional<CategoriaItemCardapio> categoria = categoriaRepository.findById(uuidCategoria);
		if (!categoria.isPresent()) {
			return false;
		}
		return true;
	}

}
