package br.com.geancesar.eufood.login.model;

public enum UsuarioRole {

	ADMIN("ADMIN"), USUARIO("USUARIO"), RESTAURANTE("RESTAURANTE");

	private String role;

	UsuarioRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

}
