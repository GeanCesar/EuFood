package br.com.geancesar.eufood.restaurante.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.geancesar.eufood.login.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;

@Entity(name = "tb_restaurante")
public class Restaurante implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;

	@NotEmpty
	private String nome;

	private String categoria;

	@JsonIgnore
	@ManyToOne
	private Usuario usuario;

	private String imagemPerfil;

	private String imagemCapa;
	
	@Column(columnDefinition = "int default 60")
	private int minutosConfirmacaoPedido;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setImagemPerfil(String imagemPerfil) {
		this.imagemPerfil = imagemPerfil;
	}

	public String getImagemPerfil() {
		return imagemPerfil;
	}

	public String getImagemCapa() {
		return imagemCapa;
	}

	public void setImagemCapa(String imagemCapa) {
		this.imagemCapa = imagemCapa;
	}
	
	public int getMinutosConfirmacaoPedido() {
		return minutosConfirmacaoPedido;
	}
	
	public void setMinutosConfirmacaoPedido(int minutosConfirmacaoPedido) {
		this.minutosConfirmacaoPedido = minutosConfirmacaoPedido;
	}

}
