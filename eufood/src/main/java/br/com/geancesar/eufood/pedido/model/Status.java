package br.com.geancesar.eufood.pedido.model;

public enum Status {

	CRIADO(1), CONFIRMADO(2), DESPACHADO(3), CANCELADO(4), CONCLUIDO(5);

	Status(int ordem) {
		this.ordem = ordem;
	}

	private int ordem;

	public int getOrdem() {
		return ordem;
	}

}
