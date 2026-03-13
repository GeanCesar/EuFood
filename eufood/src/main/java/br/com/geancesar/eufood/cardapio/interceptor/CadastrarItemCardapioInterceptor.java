package br.com.geancesar.eufood.cardapio.interceptor;

import java.math.BigDecimal;

import br.com.geancesar.eufood.cardapio.model.ItemCardapio;
import br.com.geancesar.eufood.cardapio.model.TipoItem;
import br.com.geancesar.eufood.cardapio.repository.CategoriaItemRepository;
import br.com.geancesar.eufood.restaurante.repository.RestauranteRepository;

public class CadastrarItemCardapioInterceptor {

	private String uuidRestaurante;
	private String nome;
	private BigDecimal valor;
	private String descricao;
	private String uuidCategoria;
	private int ordem;

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

	public void setUuidCategoria(String uuidCategoria) {
		this.uuidCategoria = uuidCategoria;
	}

	public String getUuidCategoria() {
		return uuidCategoria;
	}

	public ItemCardapio cadastrar(RestauranteRepository restauranteRepository, TipoItem tipo,
			CategoriaItemRepository categoriaRepository) {
		ItemCardapio item = new ItemCardapio();
		item.setDescricao(descricao);
		item.setNome(nome);
		item.setValor(valor);
		item.setTipoItem(tipo);
		item.setRestaurante(restauranteRepository.findById(uuidRestaurante).get());
		item.setCategoria(categoriaRepository.findById(uuidCategoria).get());
		item.setOrdem(ordem);

		return item;
	}

}
