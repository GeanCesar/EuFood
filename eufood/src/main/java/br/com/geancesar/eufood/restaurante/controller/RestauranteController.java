package br.com.geancesar.eufood.restaurante.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.geancesar.eufood.cardapio.repository.CategoriaItemRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.pedido.model.ControlePedidosRestaurante;
import br.com.geancesar.eufood.pedido.repository.ControlePedidosRestauranteRepository;
import br.com.geancesar.eufood.restaurante.interceptor.CadastrarRestauranteInterceptor;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;
import br.com.geancesar.eufood.restaurante.validator.RestauranteValidador;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaValidacao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("restaurante")
public class RestauranteController {

	@Value(value = "${caminho.disco.imagens}")
	public String discoArquivos;

	private static final String CAMINHO_IMAGENS = "\\Eu Food\\Imagens\\restaurantes\\";

	@Autowired
	RestauranteRepository repository;
	
	@Autowired
	ItemCardapioRepository itemRepository;
	
	@Autowired
	CategoriaItemRepository categoriaItemRepository;
	
	@Autowired
	ControlePedidosRestauranteRepository controlePedidosRepository;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private LoginUsuarioRepository usuarioRepository;

	@PostMapping("/cadastrar")
	/**
	 * Metodo responsavel por cadastrar um restaurante
	 * 
	 * @param interceptor
	 * @return
	 */
	public ResponseEntity<String> cadastrarRestaurante(@RequestBody CadastrarRestauranteInterceptor interceptor) {

		RespostaValidacao respotaValidacao = RestauranteValidador.getInstance().validarDadosCadastro(interceptor);

		if (!respotaValidacao.isOk()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respotaValidacao.getMensagem());
		}

		Restaurante restaurante = interceptor.cadastrar();
		restaurante.setUsuario(getUsuarioToken());
		repository.save(restaurante);
		
		controlePedidosRepository.save(new ControlePedidosRestaurante(restaurante));

		return ResponseEntity.status(HttpStatus.CREATED).body(restaurante.getUuid());
	}

	@DeleteMapping(value = "/deletar")
	@Transactional
	public ResponseEntity<String> deletarRestaurante(@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {

		if (!validaTokenRestaurante(uuidRestaurante)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Restaurante não condiz com o token informado");
		}

		Optional<Restaurante> restaurante = repository.findById(uuidRestaurante);

		if (!restaurante.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
		}

		categoriaItemRepository.deleteAllByRestauranteUuid(restaurante.get().getUuid());
		itemRepository.deleteAllByRestauranteUuid(restaurante.get().getUuid());
		repository.delete(restaurante.get());
		return ResponseEntity.status(HttpStatus.OK).body("");
	}

	@GetMapping(value = "/consultar")
	public ResponseEntity<Restaurante> getRestaurante(
			@RequestParam(value = "uuid-restaurante") String uuidRestaurante) {
		Optional<Restaurante> restaurante = repository.findById(uuidRestaurante);

		if (!restaurante.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(restaurante.get());
	}

	@GetMapping(value = "/listar")
	public ResponseEntity<Iterable<Restaurante>> getRestaurantes() {
		Iterable<Restaurante> restaurantes = repository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(restaurantes);
	}

	@GetMapping(value = "/listar/usuario")
	public ResponseEntity<Iterable<Restaurante>> getRestaurantesPorUsuario() {
		Iterable<Restaurante> restaurantes = repository.findAllByUsuarioUuid(getUsuarioToken().getUuid());
		return ResponseEntity.status(HttpStatus.OK).body(restaurantes);
	}

	@GetMapping(value = "/imagem_perfil")
	public @ResponseBody byte[] getImagemItem(
			@RequestParam(required = true, value = "uuid-restaurante") String uuidRestaurante) {

		Optional<Restaurante> restaurante = repository.findById(uuidRestaurante);
		if (restaurante.isPresent()) {
			try {
				File imagem = new File(
						discoArquivos + CAMINHO_IMAGENS + uuidRestaurante + "\\" + restaurante.get().getImagemPerfil());
				InputStream stream = new FileInputStream(imagem);
				return IOUtils.toByteArray(stream);
			} catch (Exception e) {
			}
		}

		return null;
	}

	@PostMapping("/upload/imagem_perfil")
	public ResponseEntity<String> uploadImagemPerfil(@RequestParam MultipartFile file,
			@RequestParam(required = true, value = "uuid-restaurante") String uuidRestaurante) {
		try {
			if (!validaTokenRestaurante(uuidRestaurante)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("UUID do restaurante não condiz com o token informado");
			}

			Path uploadPath = Paths.get(discoArquivos + CAMINHO_IMAGENS + uuidRestaurante);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Path filePath = uploadPath.resolve(file.getOriginalFilename());
			file.transferTo(filePath.toFile());

			Optional<Restaurante> restaurante = repository.findById(uuidRestaurante);
			restaurante.get().setImagemPerfil(file.getOriginalFilename());

			repository.save(restaurante.get());

			return ResponseEntity.status(HttpStatus.OK).body("");

		} catch (IOException | IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
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
			Optional<Restaurante> restaurante = repository.findById(uuidRestaurante);
			if (restaurante.isPresent() && restaurante.get().getUsuario().getUuid().equals(usuario.get().getUuid())) {
				return true;
			}
		}
		return false;
	}

	private Usuario getUsuarioToken() {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return null;
		}
		String token = authHeader.replace("Bearer ", "");

		String login = tokenService.validateToken(token);
		Optional<Usuario> usuario = usuarioRepository.findByTelefone(login);

		if (usuario.isPresent()) {
			return usuario.get();
		}
		return null;
	}

}
