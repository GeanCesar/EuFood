package br.com.geancesar.eufood.cardapio.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.cardapio.model.CategoriaItemCardapio;

public interface CategoriaItemRepository extends CrudRepository<CategoriaItemCardapio, String> {

	public List<CategoriaItemCardapio> findAllByRestauranteUuidOrderByOrdemAsc(String uuidRestaurante);
	
	public void deleteAllByRestauranteUuid(String uuidRestaurante);

}
