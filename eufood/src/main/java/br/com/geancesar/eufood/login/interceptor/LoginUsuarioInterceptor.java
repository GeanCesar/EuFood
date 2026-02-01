package br.com.geancesar.eufood.login.interceptor;

import br.com.geancesar.eufood.login.interceptor.model.UsuarioInterceptor;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.util.Criptografador;

public class LoginUsuarioInterceptor {	

	private static LoginUsuarioInterceptor instance;
	
	public static LoginUsuarioInterceptor getInstance() {
		if(instance == null) {
			instance = new LoginUsuarioInterceptor();
		}
		return instance;
	}
	
	private LoginUsuarioInterceptor() {}

	public Usuario login(UsuarioInterceptor usuarioInterceptor) {
		Usuario usuario = new Usuario();
		usuario.setNome(usuarioInterceptor.getNome());
		usuario.setSenha(Criptografador.getInstance().criptografaSenha(usuarioInterceptor.getSenha()));
		usuario.setTelefone(usuarioInterceptor.getTelefone().replace(" ", "").replace("-", ""));

		return usuario;
	}

}
