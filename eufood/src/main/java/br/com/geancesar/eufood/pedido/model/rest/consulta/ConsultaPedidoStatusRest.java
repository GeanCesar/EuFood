package br.com.geancesar.eufood.pedido.model.rest.consulta;

import java.util.Date;

public class ConsultaPedidoStatusRest {

	private String uuid;
	private Date dataHora;
	private String status;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
