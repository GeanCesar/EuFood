package br.com.geancesar.eufood.cardapio.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.cardapio.model.CategoriaSubItem;

public interface CategoriaSubItemRepository extends CrudRepository<CategoriaSubItem, String> {
	
	List<CategoriaSubItem> findAllByRestauranteUuid(String uuid);
}
