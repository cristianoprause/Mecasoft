package aplicacao.service;

import java.util.List;

import banco.modelo.DuplicataPaga;
import banco.utils.DuplicataPagaUtils;

public class DuplicataPagaService extends MecasoftService<DuplicataPaga>{

	private DuplicataPaga duplicataPaga;
	
	@Override
	public DuplicataPagaUtils getDAO() {
		return getInjector().getInstance(DuplicataPagaUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(duplicataPaga);
	}

	@Override
	public void delete() {
		getDAO().delete(duplicataPaga);
	}

	@Override
	public DuplicataPaga find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<DuplicataPaga> findAll() {
		return getDAO().findAll();
	}

	public DuplicataPaga getDuplicataPaga() {
		return duplicataPaga;
	}

	public void setDuplicataPaga(DuplicataPaga duplicataPaga) {
		this.duplicataPaga = duplicataPaga;
	}

}
