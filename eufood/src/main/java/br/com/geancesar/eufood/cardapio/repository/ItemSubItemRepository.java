package br.com.geancesar.eufood.cardapio.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.cardapio.model.ItemSubItem;

public interface ItemSubItemRepository extends CrudRepository<ItemSubItem, String> {

	public List<ItemSubItem> findAllByItemPrincipalUuidOrderByOrdem(String uuidItem);

	public List<ItemSubItem> findAllByItemPrincipalUuidAndSubItemUuid(String uuidItem, String uuidSubItem);
	
	public List<ItemSubItem> findAllByItemPrincipalUuidAndCategoriaSubItemUuidOrderByOrdem(String uuidItem, String uuidCategoria);
	
	public List<ItemSubItem> findAllByCategoriaSubItemUuid(String uuidCategoria);
}
