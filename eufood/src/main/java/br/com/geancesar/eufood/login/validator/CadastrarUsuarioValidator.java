package br.com.geancesar.eufood.login.validator;

import br.com.geancesar.eufood.login.interceptor.model.UsuarioInterceptor;
import br.com.geancesar.eufood.login.repository.CadastrarUsuarioRepository;
import br.com.geancesar.eufood.util.model.RespostaValidacao;

public class CadastrarUsuarioValidator {

	private static CadastrarUsuarioValidator instance;

	private CadastrarUsuarioValidator() {
	};

	public RespostaValidacao validarCadastrarUsuario(UsuarioInterceptor usuario,
			CadastrarUsuarioRepository repository) {

		String mensagemErro = "";
		boolean ok = true;

		if (ok && (usuario.getTelefone() == null)) {
			mensagemErro = "Telefone obrigatório";
			ok = false;
		}

		if (ok && (usuario.getSenha() == null || usuario.getSenha().length() == 0)) {
			mensagemErro = "Senha obrigatória";
			ok = false;
		}

		if (ok && (repository.findByTelefone(usuario.getTelefone()).isPresent())) {
			mensagemErro = "Telefone já utilizado";
			ok = false;
		}

		return new RespostaValidacao(mensagemErro, ok);

	}

	public static CadastrarUsuarioValidator getInstance() {
		if (instance == null) {
			instance = new CadastrarUsuarioValidator();
		}
		return instance;
	}

}
