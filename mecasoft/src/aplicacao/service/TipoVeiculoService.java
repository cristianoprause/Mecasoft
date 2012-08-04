package aplicacao.service;

import java.util.List;

import banco.modelo.TipoVeiculo;
import banco.utils.TipoVeiculoUtils;

public class TipoVeiculoService extends MecasoftService<TipoVeiculo>{

	private TipoVeiculo tipoVeiculo;
	
	@Override
	public TipoVeiculoUtils getDAO() {
		return getInjector().getInstance(TipoVeiculoUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(tipoVeiculo);
	}
	
	public void delete(){
		getDAO().delete(tipoVeiculo);
	}
	
	public TipoVeiculo find(Long id){
		return getDAO().find(id);
	}
	
	public List<TipoVeiculo> findAll(){
		return getDAO().findAll();
	}

	public TipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

}
