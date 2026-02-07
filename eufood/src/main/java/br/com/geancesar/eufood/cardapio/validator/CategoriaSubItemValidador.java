package br.com.geancesar.eufood.cardapio.validator;

import br.com.geancesar.eufood.cardapio.interceptor.CadastrarCategoriaSubItemInterceptor;
import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.util.model.RespostaValidacao;

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

	public RespostaValidacao validaDadosCadastro(CadastrarCategoriaSubItemInterceptor interceptor) {
		String mensagem = null;
		boolean ok = true;

		if (ok && (interceptor.getDescricao() == null || interceptor.getDescricao().length() <= 0)) {
			mensagem = "Descrição da categoria precisa ser preenchida";
			ok = false;
		}

		if (ok && (interceptor.getUuidRestaurante() == null || interceptor.getUuidRestaurante().length() <= 0)) {
			mensagem = "UUID do restaurante precisa ser preenchido";
			ok = false;
		}

		return new RespostaValidacao(mensagem, ok);
	}

	public CategoriaSubItem getCategoria(CadastrarCategoriaSubItemInterceptor interceptor, Restaurante restaurante) {
		CategoriaSubItem categoria = new CategoriaSubItem();
		categoria.setQuantidadeMaxima(interceptor.getQuantidadeMaxima());
		categoria.setQuantidadeMinima(interceptor.getQuantidadeMinima());
		categoria.setDescricao(interceptor.getDescricao());
		categoria.setOrdem(interceptor.getOrdem());
		categoria.setRestaurante(restaurante);
		return categoria;
	}

}
