package aplicacao.service;

import java.util.List;

import banco.modelo.Papel;
import banco.utils.PapelUtils;

public class PapelService extends MecasoftService<Papel>{

	private Papel papel;
	
	@Override
	public PapelUtils getDAO() {
		return getInjector().getInstance(PapelUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(papel);
	}
	
	public void delete(){
		getDAO().delete(papel);
	}
	
	public Papel find(Long id){
		return getDAO().find(id);
	}
	
	public List<Papel> findAll(){
		return getDAO().findAll();
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

}
