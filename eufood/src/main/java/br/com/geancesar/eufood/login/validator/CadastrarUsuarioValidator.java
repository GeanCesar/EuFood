package br.com.geancesar.eufood.login.validator;

import br.com.geancesar.eufood.login.interceptor.model.UsuarioInterceptor;
import br.com.geancesar.eufood.login.repository.CadastrarUsuarioRepository;

public class CadastrarUsuarioValidator {
	
	private static CadastrarUsuarioValidator instance;
	
	private CadastrarUsuarioValidator() {};

	public String validarCadastrarUsuario(UsuarioInterceptor usuario, CadastrarUsuarioRepository repository) {

		String mensagemErro = "";

		if (usuario.getTelefone() == null) {
			mensagemErro = "Telefone obrigatório";
		}

		if (usuario.getSenha() == null || usuario.getSenha().length() == 0) {
			mensagemErro = "Senha obrigatória";
		}

		if (repository.findByTelefone(usuario.getTelefone()).isPresent()) {
			mensagemErro = "Telefone já utilizado";
		}

		return mensagemErro;

	}
	
	public static CadastrarUsuarioValidator getInstance() {
		if(instance == null) {
			instance = new CadastrarUsuarioValidator();
		}		
		return instance;
	}

}
