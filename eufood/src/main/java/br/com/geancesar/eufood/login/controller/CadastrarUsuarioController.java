package br.com.geancesar.eufood.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.geancesar.eufood.login.interceptor.CadastrarUsuarioInterceptor;
import br.com.geancesar.eufood.login.interceptor.model.UsuarioInterceptor;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.model.UsuarioRole;
import br.com.geancesar.eufood.login.repository.CadastrarUsuarioRepository;
import br.com.geancesar.eufood.login.validator.CadastrarUsuarioValidator;
import br.com.geancesar.eufood.util.model.RespostaRequisicao;

@Controller
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
	public ResponseEntity<RespostaRequisicao> cadastrar(@RequestBody UsuarioInterceptor usuarioInterceptor) {
		String ok = CadastrarUsuarioValidator.getInstance().validarCadastrarUsuario(usuarioInterceptor, repository);
		if (ok.isEmpty()) {
			Usuario usuario = CadastrarUsuarioInterceptor.getInstance().cadastrar(usuarioInterceptor);
			usuario.setRole(UsuarioRole.USUARIO.getRole());

			if (repository.save(usuario) != null)
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(new RespostaRequisicao(true, HttpStatus.CREATED.value()));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new RespostaRequisicao(false, HttpStatus.BAD_REQUEST.value(), ok));
	}

	@PostMapping("/cadastrar_usuario_restaurante")
	/**
	 * Endpoint para cadastrar usuario com Role RESTAURANTE na tabela e retornar
	 * status de criacao
	 * 
	 * @param usuarioInterceptor
	 * @return
	 */
	public ResponseEntity<RespostaRequisicao> cadastrarUsuarioRestaurante(
			@RequestBody UsuarioInterceptor usuarioInterceptor) {
		String ok = CadastrarUsuarioValidator.getInstance().validarCadastrarUsuario(usuarioInterceptor, repository);
		if (ok.isEmpty()) {
			Usuario usuario = CadastrarUsuarioInterceptor.getInstance().cadastrar(usuarioInterceptor);
			usuario.setRole(UsuarioRole.RESTAURANTE.getRole());

			if (repository.save(usuario) != null)
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(new RespostaRequisicao(true, HttpStatus.CREATED.value()));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new RespostaRequisicao(false, HttpStatus.BAD_REQUEST.value(), ok));
	}

	@PostMapping("/cadastrar/consultar_telefone")
	/**
	 * Endpoint responsavel por informar se o telefone existe na base de dados
	 * 
	 * @param usuarioInterceptor
	 * @return
	 */
	public ResponseEntity<RespostaRequisicao> consultarTelefone(@RequestBody UsuarioInterceptor usuarioInterceptor) {
		if (usuarioInterceptor.getTelefone() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new RespostaRequisicao(false, HttpStatus.NOT_FOUND.value(), "Telefone obrigatório"));
		}
		usuarioInterceptor.setTelefone(usuarioInterceptor.getTelefone().replace(" ", "").replace("-", ""));
		if (!repository.findByTelefone(usuarioInterceptor.getTelefone()).isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new RespostaRequisicao(false, HttpStatus.NOT_FOUND.value()));
		}

		return ResponseEntity.status(HttpStatus.FOUND).body(new RespostaRequisicao(false, HttpStatus.FOUND.value()));
	}

}
