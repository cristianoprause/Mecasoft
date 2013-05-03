package aplicacao.service;

import java.util.List;

import banco.modelo.Papel;
import banco.utils.PapelUtils;

public class PapelService extends MecasoftService<Papel>{

	@Override
	public PapelUtils getDAO() {
		return getInjector().getInstance(PapelUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(modelo);
	}
	
	public void delete(){
		getDAO().delete(modelo);
	}
	
	public Papel find(Long id){
		return getDAO().find(id);
	}
	
	public List<Papel> findAll(){
		return getDAO().findAll();
	}

	public Papel getPapel() {
		return getModelo();
	}

	public void setPapel(Papel papel) {
		setModelo(papel);
	}

}
