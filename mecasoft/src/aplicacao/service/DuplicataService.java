package aplicacao.service;

import java.util.Date;
import java.util.List;

import banco.modelo.Duplicata;
import banco.utils.DuplicataUtils;

public class DuplicataService extends MecasoftService<Duplicata>{

	@Override
	public DuplicataUtils getDAO() {
		return getInjector().getInstance(DuplicataUtils.class);
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
	public Duplicata find(Long id) {
		return getDAO().find(id);
	}
	
	public Duplicata findUltimaDuplicata(){
		return getDAO().findUltimaDuplicata();
	}
	
	public Duplicata findByNumero(String numero){
		return getDAO().findByNumero(numero);
	}

	@Override
	public List<Duplicata> findAll() {
		return getDAO().findAll();
	}
	
	public List<Duplicata> findAllNaoPagas(){
		return getDAO().findAllByPagamento(false);
	}
	
	public List<Duplicata> findAllNaoPagasByPeriodo(Date dtInicial, Date dtFinal){
		return getDAO().findAllByPeriodoAndPagamento(dtInicial, dtFinal, false);
	}

	public Duplicata getDuplicata() {
		return getModelo();
	}

	public void setDuplicata(Duplicata duplicata) {
		setModelo(duplicata);
	}

}
