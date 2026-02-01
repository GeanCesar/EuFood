package br.com.geancesar.eufood.cardapio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.geancesar.eufood.restaurante.model.Restaurante;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_categoria_sub_item_cardapio")
public class CategoriaSubItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;

	@ManyToOne
	@JsonIgnore
	private Restaurante restaurante;

	private String descricao;

	private int quantidadeMinima;

	private int quantidadeMaxima;
	
	private int ordem;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantidadeMinima() {
		return quantidadeMinima;
	}

	public void setQuantidadeMinima(int quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}

	public int getQuantidadeMaxima() {
		return quantidadeMaxima;
	}

	public void setQuantidadeMaxima(int quantidadeMaxima) {
		this.quantidadeMaxima = quantidadeMaxima;
	}
	
	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	
	public int getOrdem() {
		return ordem;
	}

}
