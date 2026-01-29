package br.com.geancesar.eufood.restaurante.validator;

import br.com.geancesar.eufood.restaurante.interceptor.CadastrarCategoriaItemInterceptor;
import br.com.geancesar.eufood.restaurante.model.CategoriaItemCardapio;
import br.com.geancesar.eufood.restaurante.model.Restaurante;

public class CategoriaItemValidador {

	private static CategoriaItemValidador instance;

	private CategoriaItemValidador() {
	}

	public static CategoriaItemValidador getInstance() {
		if (instance == null) {
			instance = new CategoriaItemValidador();
		}
		return instance;
	}

	public String validaDadosCadastro(CadastrarCategoriaItemInterceptor interceptor) {
		String mensagem = null;

		if (interceptor.getDescricao() == null || interceptor.getDescricao().length() <= 0) {
			mensagem = "Descrição da categoria precisa ser preenchida";
		}

		return mensagem;
	}

	public CategoriaItemCardapio getCategoria(CadastrarCategoriaItemInterceptor interceptor, Restaurante restaurante) {
		CategoriaItemCardapio categoria = new CategoriaItemCardapio();
		categoria.setDescricao(interceptor.getDescricao());
		categoria.setOrdem(interceptor.getOrdem());
		categoria.setRestaurante(restaurante);
		return categoria;
	}

}
