package br.com.geancesar.eufood.pedido.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.pedido.model.ControlePedidosRestaurante;

public interface ControlePedidosRestauranteRepository extends CrudRepository<ControlePedidosRestaurante, String> {

	Optional<ControlePedidosRestaurante> findByRestauranteUuid(String uuidRestaurante);

}
