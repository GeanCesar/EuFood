package br.com.geancesar.eufood.login.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import br.com.geancesar.eufood.login.interceptor.model.UsuarioInterceptor;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.login.validator.LoginUsuarioValidator;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaRequisicao;

@Controller
@RequestMapping("/usuario_login")
public class LoginUsuarioController {

	@Autowired
	LoginUsuarioRepository repository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	/**
	 * Efetuar a requisicao de login
	 * 
	 * @param usuarioInterceptor
	 * @return
	 */
	public ResponseEntity<RespostaRequisicao> login(@RequestBody UsuarioInterceptor usuarioInterceptor) {

		LoginUsuarioValidator validador = new LoginUsuarioValidator();
		validador.validarDados(usuarioInterceptor);

		UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(
				usuarioInterceptor.getTelefone(), usuarioInterceptor.getSenha());

		Authentication auth = authenticationManager.authenticate(userNamePassword);

		String token = tokenService.generateToken((Usuario) auth.getPrincipal());

		if (auth.getPrincipal() != null) {
			Usuario usuario = (Usuario) auth.getPrincipal();

			usuario.setUltimoLogin(new Date());
			repository.save(usuario);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new RespostaRequisicao(true, HttpStatus.OK.value(), token));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new RespostaRequisicao(false, HttpStatus.UNAUTHORIZED.value()));
		}
	}

}
