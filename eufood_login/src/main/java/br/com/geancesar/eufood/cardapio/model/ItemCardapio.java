package br.com.geancesar.eufood.cardapio.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.geancesar.eufood.restaurante.model.Restaurante;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;

@Entity(name = "tb_item_cardapio")
public class ItemCardapio {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;

	@ManyToOne
	@JsonIgnore
	private Restaurante restaurante;

	@NotEmpty
	private String nome;

	private BigDecimal valor;

	@Lob
	@Column(name = "descricao", length = 1024)
	private String descricao;

	private String imagem;

	private String tipoItem;

	@ManyToOne
	private CategoriaItemCardapio categoria;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
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

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public CategoriaItemCardapio getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaItemCardapio categoria) {
		this.categoria = categoria;
	}

	public TipoItem getTipoItem() {
		return TipoItem.valueOf(tipoItem);
	}

	public void setTipoItem(TipoItem tipoItem) {
		this.tipoItem = tipoItem.getDescricao();
	}

}
