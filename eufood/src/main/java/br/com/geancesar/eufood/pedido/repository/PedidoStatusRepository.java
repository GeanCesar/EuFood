package br.com.geancesar.eufood.pedido.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.pedido.model.PedidoStatus;

public interface PedidoStatusRepository extends CrudRepository<PedidoStatus, String> {

}
