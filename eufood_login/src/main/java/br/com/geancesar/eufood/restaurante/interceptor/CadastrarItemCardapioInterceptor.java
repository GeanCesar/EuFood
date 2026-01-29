package br.com.geancesar.eufood.restaurante.interceptor;

import java.math.BigDecimal;

import br.com.geancesar.eufood.restaurante.model.ItemCardapio;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;

public class CadastrarItemCardapioInterceptor {

	private String uuidRestaurante;

	private String nome;

	private BigDecimal valor;

	private String descricao;

	private int quantidadeMinimaSubItem;

	public String getUuidRestaurante() {
		return uuidRestaurante;
	}

	public void setUuidRestaurante(String uuidRestaurante) {
		this.uuidRestaurante = uuidRestaurante;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantidadeMinimaSubItem() {
		return quantidadeMinimaSubItem;
	}

	public void setQuantidadeMinimaSubItem(int quantidadeMinimaSubItem) {
		this.quantidadeMinimaSubItem = quantidadeMinimaSubItem;
	}

	public ItemCardapio cadastrar(RestauranteRepository restauranteRepository) {

		ItemCardapio item = new ItemCardapio();
		item.setDescricao(descricao);
		item.setNome(nome);
		item.setQuantidadeMinimaSubItem(quantidadeMinimaSubItem);
		item.setValor(valor);
		item.setRestaurante(restauranteRepository.findById(uuidRestaurante).get());

		return item;
	}

}
