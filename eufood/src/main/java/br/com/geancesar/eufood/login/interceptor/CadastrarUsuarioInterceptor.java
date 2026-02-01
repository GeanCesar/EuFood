package br.com.geancesar.eufood.login.interceptor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.geancesar.eufood.login.interceptor.model.UsuarioInterceptor;
import br.com.geancesar.eufood.login.model.Usuario;

public class CadastrarUsuarioInterceptor {

	private static CadastrarUsuarioInterceptor instance;

	public static CadastrarUsuarioInterceptor getInstance() {
		if (instance == null) {
			instance = new CadastrarUsuarioInterceptor();
		}
		return instance;
	}

	private CadastrarUsuarioInterceptor() {
	}

	public Usuario cadastrar(UsuarioInterceptor usuarioInterceptor) {
		String encryptedPassword = new BCryptPasswordEncoder().encode(usuarioInterceptor.getSenha());

		Usuario usuario = new Usuario();
		usuario.setNome(usuarioInterceptor.getNome());
		usuario.setSenha(encryptedPassword);
		usuario.setTelefone(usuarioInterceptor.getTelefone().replace(" ", "").replace("-", ""));

		return usuario;
	}

}
