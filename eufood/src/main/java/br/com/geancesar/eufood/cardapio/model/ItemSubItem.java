package br.com.geancesar.eufood.cardapio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_cardapio_item_sub_item")
public class ItemSubItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;

	@ManyToOne
	@JoinColumn(name = "uuid_item_principal")
	private ItemCardapio itemPrincipal;

	@ManyToOne
	@JoinColumn(name = "uuid_sub_item")
	private ItemCardapio subItem;

	@ManyToOne
	@JoinColumn(name = "uuid_categoria_subitem")
	private CategoriaSubItem categoriaSubItem;
	
	private int ordem;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public ItemCardapio getItemPrincipal() {
		return itemPrincipal;
	}

	public void setItemPrincipal(ItemCardapio itemPrincipal) {
		this.itemPrincipal = itemPrincipal;
	}

	public ItemCardapio getSubItem() {
		return subItem;
	}

	public void setSubItem(ItemCardapio subItem) {
		this.subItem = subItem;
	}

	public CategoriaSubItem getCategoriaSubItem() {
		return categoriaSubItem;
	}

	public void setCategoriaSubItem(CategoriaSubItem categoriaSubItem) {
		this.categoriaSubItem = categoriaSubItem;
	}
	
	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	
	public int getOrdem() {
		return ordem;
	}

}
