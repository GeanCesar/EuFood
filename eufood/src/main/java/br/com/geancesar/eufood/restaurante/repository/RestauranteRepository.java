package br.com.geancesar.eufood.restaurante.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.restaurante.model.Restaurante;

public interface RestauranteRepository extends CrudRepository<Restaurante, String> {

	List<Restaurante> findAllByUsuarioUuid(String uuid);

}
