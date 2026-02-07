package br.com.geancesar.eufood.pedido.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.pedido.model.PedidoSubItem;

public interface PedidoSubItemRepository extends CrudRepository<PedidoSubItem, String> {

}
