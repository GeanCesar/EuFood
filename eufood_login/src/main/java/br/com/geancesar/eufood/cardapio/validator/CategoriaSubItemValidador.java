package br.com.geancesar.eufood.cardapio.validator;

import br.com.geancesar.eufood.cardapio.interceptor.CadastrarCategoriaSubItemInterceptor;
import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;
import br.com.geancesar.eufood.restaurante.model.Restaurante;

public class CategoriaSubItemValidador {

	private static CategoriaSubItemValidador instance;

	private CategoriaSubItemValidador() {
	}

	public static CategoriaSubItemValidador getInstance() {
		if (instance == null) {
			instance = new CategoriaSubItemValidador();
		}
		return instance;
	}

	public String validaDadosCadastro(CadastrarCategoriaSubItemInterceptor interceptor) {
		String mensagem = null;

		if (interceptor.getDescricao() == null || interceptor.getDescricao().length() <= 0) {
			mensagem = "Descrição da categoria precisa ser preenchida";
		}

		if (interceptor.getUuidRestaurante() == null || interceptor.getUuidRestaurante().length() <= 0) {
			mensagem = "UUID do restaurante precisa ser preenchido";
		}

		return mensagem;
	}

	public CategoriaSubItem getCategoria(CadastrarCategoriaSubItemInterceptor interceptor, Restaurante restaurante) {
		CategoriaSubItem categoria = new CategoriaSubItem();
		categoria.setQuantidadeMaxima(interceptor.getQuantidadeMaxima());
		categoria.setQuantidadeMinima(categoria.getQuantidadeMinima());
		categoria.setDescricao(interceptor.getDescricao());
		categoria.setOrdem(interceptor.getOrdem());
		categoria.setRestaurante(restaurante);
		return categoria;
	}

}
