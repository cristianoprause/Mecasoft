package aplicacao.service;

import java.util.List;

import banco.modelo.Veiculo;
import banco.utils.VeiculoUtils;

public class VeiculoService extends MecasoftService<Veiculo>{

	@Override
	public VeiculoUtils getDAO() {
		return getInjector().getInstance(VeiculoUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(modelo);
	}

	@Override
	public void delete() {
		getDAO().delete(modelo);
	}
	
	public Veiculo find(Long id){
		return getDAO().find(id);
	}
	
	public List<Veiculo> findAll(){
		return getDAO().findAll();
	}
	
	public Veiculo getVeiculo() {
		return getModelo();
	}

	public void setVeiculo(Veiculo veiculo) {
		setModelo(veiculo);
	}


}
