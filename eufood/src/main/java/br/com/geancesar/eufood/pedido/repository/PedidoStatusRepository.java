package br.com.geancesar.eufood.pedido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.geancesar.eufood.pedido.model.PedidoStatus;

public interface PedidoStatusRepository extends CrudRepository<PedidoStatus, String> {

	public List<PedidoStatus> findAllByPedidoUuidOrderByDataHoraDesc(String uuid);

	@Query(value = "SELECT s.* from tb_pedido_status s	INNER JOIN tb_pedido p ON p.uuid = s.uuid_pedido WHERE status = 'DESPACHADO'    AND p.uuid_restaurante = ?1 AND NOT EXISTS(SELECT * from tb_pedido_status WHERE status = 'CONCLUIDO' AND p.uuid_restaurante = ?1)", nativeQuery = true)
	List<PedidoStatus> findPedidosSemConclusao(String uuidRestaurante);
	
	@Query(value = "SELECT s.* from tb_pedido_status s	LEFT JOIN tb_pedido p ON p.uuid = s.uuid_pedido LEFT OUTER JOIN (SELECT s2.* from tb_pedido_status s2 LEFT JOIN tb_pedido p2 ON p2.uuid = s2.uuid_pedido where s2.status = 'CONFIRMADO' AND p2.uuid_restaurante = ?1) s2 ON s2.uuid_pedido = s.uuid_pedido    where s.status = 'CRIADO' AND p.uuid_restaurante = ?1 AND s2.uuid is null", nativeQuery = true)
	List<PedidoStatus> findPedidosSemConfirmacao(String uuidRestaurante);

}
