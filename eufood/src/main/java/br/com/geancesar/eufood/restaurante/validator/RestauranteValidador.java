package br.com.geancesar.eufood.restaurante.validator;

import br.com.geancesar.eufood.restaurante.interceptor.CadastrarRestauranteInterceptor;
import br.com.geancesar.eufood.util.model.RespostaValidacao;

public class RestauranteValidador {

	private static RestauranteValidador instance;

	private RestauranteValidador() {
	}

	public RespostaValidacao validarDadosCadastro(CadastrarRestauranteInterceptor restaurante) {

		String mensagem = "";
		boolean ok = true;

		if (ok && (restaurante == null)) {
			mensagem = "Campos do restaurante vazios";
			ok = false;
		}

		if (ok && (restaurante.getNome() == null || restaurante.getNome().isEmpty())) {
			mensagem = "Nome do restaurante precisa ser preenchido";
			ok = false;
		}

		return new RespostaValidacao(mensagem, ok);
	}

	public static RestauranteValidador getInstance() {
		if (instance == null) {
			instance = new RestauranteValidador();
		}
		return instance;
	}

}
