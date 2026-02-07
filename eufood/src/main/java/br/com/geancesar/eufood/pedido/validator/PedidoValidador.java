package br.com.geancesar.eufood.pedido.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.geancesar.eufood.cardapio.model.ItemCardapio;
import br.com.geancesar.eufood.cardapio.model.ItemSubItem;
import br.com.geancesar.eufood.cardapio.repository.ItemCardapioRepository;
import br.com.geancesar.eufood.cardapio.repository.ItemSubItemRepository;
import br.com.geancesar.eufood.login.model.Usuario;
import br.com.geancesar.eufood.login.repository.LoginUsuarioRepository;
import br.com.geancesar.eufood.pedido.model.rest.criacao.CriacaoPedidoItemRest;
import br.com.geancesar.eufood.pedido.model.rest.criacao.CriacaoPedidoRest;
import br.com.geancesar.eufood.pedido.model.rest.criacao.CriacaoPedidoSubItemRest;
import br.com.geancesar.eufood.security.TokenService;
import br.com.geancesar.eufood.util.model.RespostaValidacao;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PedidoValidador {

	@Autowired
	ItemCardapioRepository itemRepository;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private LoginUsuarioRepository usuarioRepository;

	@Autowired
	private ItemSubItemRepository itemSubRepository;

	private static PedidoValidador instance;

	private PedidoValidador() {
	}

	public static PedidoValidador getInstance() {
		if (instance == null) {
			instance = new PedidoValidador();
		}
		return instance;
	}

	public RespostaValidacao validarCriacao(CriacaoPedidoRest pedido) {
		String mensagem = "";
		boolean ok = true;

		if (pedido == null) {
			mensagem = "Pedido sem informação";
			ok = false;
		}

		if (ok && (pedido.getItems() == null || pedido.getItems().size() <= 0)) {
			mensagem = "É necessário ter pelo menos 1 item";
			ok = false;
		} else {
			for (CriacaoPedidoItemRest item : pedido.getItems()) {
				if (ok && (item.getQuantidade().compareTo(BigDecimal.ZERO) <= 0)) {
					mensagem = "Item " + item.getUuid() + " com quantidade zerada";
					ok = false;
				}
			}
		}

		if (ok && (pedido.getUuidUsuario() == null)) {
			mensagem = "É necessário informar um usuário";
			ok = false;
		}

		if (ok && (pedido.getUuidRestaurante() == null)) {
			mensagem = "É necessário informar um restaurante";
			ok = false;
		}

		if (ok) {
			for (CriacaoPedidoItemRest item : pedido.getItems()) {
				Optional<ItemCardapio> itemCardapio = itemRepository.findById(item.getUuid());
				if (ok && (itemCardapio == null || itemCardapio.isEmpty())) {
					mensagem = "UUID " + item.getUuid() + " não encontrado";
					ok = false;
				}

				if (ok && itemCardapio.isPresent()
						&& !itemCardapio.get().getRestaurante().getUuid().equals(pedido.getUuidRestaurante())) {
					mensagem = "Item " + item.getUuid() + " não pertence ao cardapio do restaurante";
					ok = false;
				}

				if (ok && item.getSubItems() != null && item.getSubItems().size() > 0) {
					for (CriacaoPedidoSubItemRest sub : item.getSubItems()) {
						List<ItemSubItem> subItems = itemSubRepository
								.findAllByItemPrincipalUuidAndSubItemUuid(item.getUuid(), sub.getUuid());
						if (ok && (subItems == null || subItems.size() <= 0)) {
							mensagem = "Subitem " + sub.getUuid()
									+ " não existe na relação restaurante / item principal informada";
							ok = false;
						}
					}
				}
			}
		}

		if (ok && !validaToken(pedido.getUuidUsuario())) {
			mensagem = "Token informado não condiz com o usuário informado no pedido";
			ok = false;
		}

		return new RespostaValidacao(mensagem, ok);
	}

	private boolean validaToken(String uuid) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return false;
		}
		String token = authHeader.replace("Bearer ", "");

		String login = tokenService.validateToken(token);
		Optional<Usuario> usuario = usuarioRepository.findByTelefone(login);

		if (usuario.isPresent() && usuario.get().getUuid().equalsIgnoreCase(uuid)) {
			return true;
		}
		return false;
	}

}
