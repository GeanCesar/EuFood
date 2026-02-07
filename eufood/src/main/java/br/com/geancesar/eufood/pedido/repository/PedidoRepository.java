package br.com.geancesar.eufood.pedido.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.pedido.model.Pedido;

public interface PedidoRepository extends CrudRepository<Pedido, String> {

}
