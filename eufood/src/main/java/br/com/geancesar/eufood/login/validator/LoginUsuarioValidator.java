package br.com.geancesar.eufood.login.validator;

import br.com.geancesar.eufood.login.interceptor.model.UsuarioInterceptor;

public class LoginUsuarioValidator {

	public void validarDados(UsuarioInterceptor usuario) {
		usuario.setTelefone(usuario.getTelefone().replace(" ", "").replace("-", ""));
	}

}
