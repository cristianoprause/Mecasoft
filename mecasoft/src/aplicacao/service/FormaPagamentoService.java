package aplicacao.service;

import java.util.List;

import banco.modelo.FormaPagamento;
import banco.utils.FormaPagamentoUtils;

public class FormaPagamentoService extends MecasoftService<FormaPagamento>{

	@Override
	public FormaPagamentoUtils getDAO() {
		return getInjector().getInstance(FormaPagamentoUtils.class);
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
	public FormaPagamento find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<FormaPagamento> findAll() {
		return getDAO().findAll();
	}
	
	public List<FormaPagamento> findAllAtivos(){
		return getDAO().findAllByStatus(true);
	}

	public FormaPagamento getForma() {
		return getModelo();
	}

	public void setForma(FormaPagamento forma) {
		setModelo(forma);
	}

}
