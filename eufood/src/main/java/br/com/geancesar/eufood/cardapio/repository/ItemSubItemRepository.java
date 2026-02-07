package br.com.geancesar.eufood.cardapio.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.cardapio.model.ItemSubItem;

public interface ItemSubItemRepository extends CrudRepository<ItemSubItem, String> {

	public List<ItemSubItem> findAllByItemPrincipalUuid(String uuidItem);

	public List<ItemSubItem> findAllByItemPrincipalUuidAndSubItemUuid(String uuidItem, String uuidSubItem);
}
