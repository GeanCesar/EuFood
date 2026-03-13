package br.com.geancesar.eufood.cardapio.validator;

import java.util.Optional;

import br.com.geancesar.eufood.cardapio.interceptor.CadastrarItemSubItemInterceptor;
import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;
import br.com.geancesar.eufood.cardapio.model.ItemCardapio;
import br.com.geancesar.eufood.cardapio.repository.CategoriaSubItemRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;

public class SubItemValidador {

	private static SubItemValidador instance;

	private SubItemValidador() {
	}

	public static SubItemValidador getInstance() {
		if (instance == null) {
			instance = new SubItemValidador();
		}
		return instance;
	}

	public Object validarAssociacao(ItemCardapioRepository itemRepository,
			CategoriaSubItemRepository categoriaSubItemRepository, String uuidCategoria, String uuidItemCardapio,
			String uuidSubItem, int ordem) {
		Optional<CategoriaSubItem> categoria = categoriaSubItemRepository.findById(uuidCategoria);
		if (categoria.isEmpty()) {
			return "UUID da Categoria não encontrado";
		}

		Optional<ItemCardapio> item = itemRepository.findById(uuidItemCardapio);
		if (item.isEmpty()) {
			return "UUID do item não encontrado";
		}

		Optional<ItemCardapio> sub = itemRepository.findById(uuidSubItem);
		if (sub.isEmpty()) {
			return "UUID do subitem não encontrado";
		}
		
		CadastrarItemSubItemInterceptor interceptor = new CadastrarItemSubItemInterceptor();
		return interceptor.associar(categoria.get(), item.get(), sub.get(), ordem);
	}

}
