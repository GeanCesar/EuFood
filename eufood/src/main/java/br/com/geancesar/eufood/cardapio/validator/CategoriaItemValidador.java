package br.com.geancesar.eufood.cardapio.validator;

import br.com.geancesar.eufood.cardapio.interceptor.CadastrarCategoriaItemInterceptor;
import br.com.geancesar.eufood.cardapio.model.CategoriaItemCardapio;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.util.model.RespostaValidacao;

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

	public RespostaValidacao validaDadosCadastro(CadastrarCategoriaItemInterceptor interceptor) {
		String mensagem = null;
		boolean ok = true;

		if (ok && (interceptor.getDescricao() == null || interceptor.getDescricao().length() <= 0)) {
			mensagem = "Descrição da categoria precisa ser preenchida";
			ok = false;
		}

		return new RespostaValidacao(mensagem, ok);
	}

	public CategoriaItemCardapio getCategoria(CadastrarCategoriaItemInterceptor interceptor, Restaurante restaurante) {
		CategoriaItemCardapio categoria = new CategoriaItemCardapio();
		categoria.setDescricao(interceptor.getDescricao());
		categoria.setOrdem(interceptor.getOrdem());
		categoria.setRestaurante(restaurante);
		return categoria;
	}

}
