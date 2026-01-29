package br.com.geancesar.eufood.restaurante.interceptor;

import br.com.geancesar.eufood.restaurante.model.Restaurante;

public class CadastrarRestauranteInterceptor {

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Restaurante cadastrar() {
		Restaurante restaurante = new Restaurante();
		restaurante.setNome(nome);
		return restaurante;
	}

}
