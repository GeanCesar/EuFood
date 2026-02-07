package br.com.geancesar.eufood.pedido.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.pedido.model.PedidoItem;

public interface PedidoItemRepository extends CrudRepository<PedidoItem, String> {

}
