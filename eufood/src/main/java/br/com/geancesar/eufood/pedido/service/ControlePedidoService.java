package br.com.geancesar.eufood.pedido.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.geancesar.eufood.pedido.model.ControlePedidosRestaurante;
import br.com.geancesar.eufood.pedido.model.PedidoStatus;
import br.com.geancesar.eufood.pedido.model.Status;
import br.com.geancesar.eufood.pedido.repository.ControlePedidosRestauranteRepository;
import br.com.geancesar.eufood.pedido.repository.PedidoStatusRepository;
import br.com.geancesar.eufood.restaurante.model.Restaurante;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;

@Service
public class ControlePedidoService {

	@Autowired
	PedidoStatusRepository pedidoStatusRepository;

	@Autowired
	ControlePedidosRestauranteRepository controlePedidosRepository;

	@Autowired
	RestauranteRepository restauranteRepository;

	public void atualizaStatusPedidoConcluido(Restaurante restaurante) {
		List<PedidoStatus> status = pedidoStatusRepository.findPedidosSemConclusao(restaurante.getUuid());
		for (PedidoStatus s : status) {
			Calendar dataHora = Calendar.getInstance();

			Calendar dataHoraVencimento = Calendar.getInstance();
			dataHoraVencimento.setTime(s.getDataHora());
			dataHoraVencimento.add(Calendar.MINUTE, restaurante.getMinutosConfirmacaoPedido());

			if (dataHora.after(dataHoraVencimento)) {
				criaStatusConcluido(s);
			}
		}
	}

	public void atualizaControle(String uuidRestaurante) {
		Optional<Restaurante> restaurante = restauranteRepository.findById(uuidRestaurante);
		
		Optional<ControlePedidosRestaurante> controle = controlePedidosRepository
				.findByRestauranteUuid(restaurante.get().getUuid());

		List<PedidoStatus> status = pedidoStatusRepository.findPedidosSemConfirmacao(restaurante.get().getUuid());
		if (status == null || status.size() <= 0) {
			controle.get().setPossuiNovoPedido(false);
			controlePedidosRepository.save(controle.get());			
			return;
		}		

		if (controle.isPresent()) {
			controle.get().setPossuiNovoPedido(true);
			controlePedidosRepository.save(controle.get());
		}
	}

	private void criaStatusConcluido(PedidoStatus s) {
		PedidoStatus concluido = new PedidoStatus();
		concluido.setDataHora(new Date());
		concluido.setStatus(Status.CONCLUIDO.toString());
		concluido.setPedido(s.getPedido());

		pedidoStatusRepository.save(concluido);
	}

}
