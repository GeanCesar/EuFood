package br.com.geancesar.eufood.restaurante.validator;

import br.com.geancesar.eufood.restaurante.interceptor.CadastrarRestauranteInterceptor;

public class RestauranteValidador {

	private static RestauranteValidador instance;

	private RestauranteValidador() {
	}

	public String validarDadosCadastro(CadastrarRestauranteInterceptor restaurante) {

		String mensagem = null;

		if (restaurante == null) {
			mensagem = "Campos do restaurante vazios";
		}

		if (restaurante.getNome() == null || restaurante.getNome().isEmpty()) {
			mensagem = "Nome do restaurante precisa ser preenchido";
		}

		return mensagem;
	}

	public static RestauranteValidador getInstance() {
		if (instance == null) {
			instance = new RestauranteValidador();
		}
		return instance;
	}

}
