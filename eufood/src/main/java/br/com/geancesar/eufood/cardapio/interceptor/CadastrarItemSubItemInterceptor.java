package br.com.geancesar.eufood.cardapio.interceptor;

import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;
import br.com.geancesar.eufood.cardapio.model.ItemCardapio;
import br.com.geancesar.eufood.cardapio.model.ItemSubItem;

public class CadastrarItemSubItemInterceptor {

	public ItemSubItem associar(CategoriaSubItem categoria, ItemCardapio itemCardapio, ItemCardapio sub, int ordem) {
		ItemSubItem itemSub = new ItemSubItem();
		itemSub.setCategoriaSubItem(categoria);
		itemSub.setItemPrincipal(itemCardapio);
		itemSub.setSubItem(sub);
		itemSub.setOrdem(ordem);
		return itemSub;
	}

}
