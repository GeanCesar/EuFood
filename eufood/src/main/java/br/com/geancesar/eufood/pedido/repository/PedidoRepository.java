package br.com.geancesar.eufood.pedido.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.pedido.model.Pedido;

public interface PedidoRepository extends CrudRepository<Pedido, String> {

	List<Pedido> findAllByUsuarioUuidOrderByDataHoraDesc(String uuid);
	
	List<Pedido> findAllByRestauranteUuidAndDataHoraAfterOrderByDataHoraDesc(String uuidRestaurante, Date dataInicio);
	
	boolean existsByNumeroPedidoAndDataHoraAfter(String numeroPedido, Date data);

}
