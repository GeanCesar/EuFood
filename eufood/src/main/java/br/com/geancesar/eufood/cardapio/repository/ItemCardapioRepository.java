package br.com.geancesar.eufood.cardapio.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.cardapio.model.ItemCardapio;

public interface ItemCardapioRepository extends CrudRepository<ItemCardapio, String> {

	public List<ItemCardapio> findAllByRestauranteUuidAndTipoItemOrderByOrdem(String uuidRestaurante, String tipoItem);

	public List<ItemCardapio> findAllByRestauranteUuidAndCategoriaUuidAndTipoItemOrderByOrdem(String uuidRestaurante, String categoria, String tipoItem);

}
