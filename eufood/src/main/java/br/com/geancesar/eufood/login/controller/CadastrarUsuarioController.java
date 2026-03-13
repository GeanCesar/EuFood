package br.com.geancesar.eufood.login.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.geancesar.eufood.login.interceptor.CadastrarUsuarioInterceptor;
import br.com.geancesar.eufood.login.interceptor.model.UsuarioInterceptor;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.model.UsuarioRole;
import br.com.geancesar.eufood.login.repository.CadastrarUsuarioRepository;
import br.com.geancesar.eufood.login.validator.CadastrarUsuarioValidator;
import br.com.geancesar.eufood.util.model.RespostaValidacao;

@RestController
@RequestMapping("usuario")
public class CadastrarUsuarioController {

	@Autowired
	CadastrarUsuarioRepository repository;

	@PostMapping("/cadastrar")
	/**
	 * Endpoint para cadastrar usuario na tabela e retornar status de criacao
	 * 
	 * @param usuarioInterceptor
	 * @return
	 */
	public ResponseEntity<String> cadastrar(@RequestBody UsuarioInterceptor usuarioInterceptor) {
		RespostaValidacao respostaValidacao = CadastrarUsuarioValidator.getInstance()
				.validarCadastrarUsuario(usuarioInterceptor, repository);
		if (respostaValidacao.isOk()) {
			Usuario usuario = CadastrarUsuarioInterceptor.getInstance().cadastrar(usuarioInterceptor);
			usuario.setRole(UsuarioRole.USUARIO.getRole());

			if (repository.save(usuario) != null)
				return ResponseEntity.status(HttpStatus.CREATED).body("");
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaValidacao.getMensagem());
	}

	@PostMapping("/cadastrar_usuario_restaurante")
	/**
	 * Endpoint para cadastrar usuario com Role RESTAURANTE na tabela e retornar
	 * status de criacao
	 * 
	 * @param usuarioInterceptor
	 * @return
	 */
	public ResponseEntity<String> cadastrarUsuarioRestaurante(@RequestBody UsuarioInterceptor usuarioInterceptor) {
		RespostaValidacao respostaValidacao = CadastrarUsuarioValidator.getInstance()
				.validarCadastrarUsuario(usuarioInterceptor, repository);
		if (respostaValidacao.isOk()) {
			Usuario usuario = CadastrarUsuarioInterceptor.getInstance().cadastrar(usuarioInterceptor);
			usuario.setRole(UsuarioRole.RESTAURANTE.getRole());

			if (repository.save(usuario) != null)
				return ResponseEntity.status(HttpStatus.CREATED).body("");
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaValidacao.getMensagem());
	}

	@PostMapping("/cadastrar/consultar_telefone")
	/**
	 * Endpoint responsavel por informar se o telefone existe na base de dados
	 * 
	 * @param usuarioInterceptor
	 * @return
	 */
	public ResponseEntity<String> consultarTelefone(@RequestBody UsuarioInterceptor usuarioInterceptor) {
		if (usuarioInterceptor.getTelefone() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Telefone obrigatório");
		}
		usuarioInterceptor.setTelefone(usuarioInterceptor.getTelefone().replace(" ", "").replace("-", ""));

		Optional<Usuario> usuario = repository.findByTelefone(usuarioInterceptor.getTelefone());

		if (!usuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
		}

		return ResponseEntity.status(HttpStatus.OK).body("");
	}

}
