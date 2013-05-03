package aplicacao.service;

import java.util.Date;
import java.util.List;

import banco.modelo.DuplicataPaga;
import banco.utils.DuplicataPagaUtils;

public class DuplicataPagaService extends MecasoftService<DuplicataPaga>{

	@Override
	public DuplicataPagaUtils getDAO() {
		return getInjector().getInstance(DuplicataPagaUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(modelo);
	}

	@Override
	public void delete() {
		getDAO().delete(modelo);
	}

	@Override
	public DuplicataPaga find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<DuplicataPaga> findAll() {
		return getDAO().findAll();
	}
	
	public List<DuplicataPaga> findAllByPeriodo(Date dtInicial, Date dtFinal){
		return getDAO().findAllByPeriodo(dtInicial, dtFinal);
	}

	public DuplicataPaga getDuplicataPaga() {
		return getModelo();
	}

	public void setDuplicataPaga(DuplicataPaga duplicataPaga) {
		setModelo(duplicataPaga);
	}

}
