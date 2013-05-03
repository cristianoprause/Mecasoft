package aplicacao.service;

import java.util.List;

import banco.modelo.ItemServico;
import banco.utils.ItemServicoUtils;

public class ItemServicoService extends MecasoftService<ItemServico>{
	
	@Override
	public ItemServicoUtils getDAO() {
		return getInjector().getInstance(ItemServicoUtils.class);
	}

	@Override
	public void saveOrUpdate() {}

	@Override
	public void delete() {
		getDAO().delete(modelo);
	}

	@Override
	public ItemServico find(Long id) {
		return null;
	}

	@Override
	public List<ItemServico> findAll() {
		return null;
	}

	public ItemServico getModelo() {
		return modelo;
	}

	public void setModelo(ItemServico modelo) {
		this.modelo = modelo;
	}

}
