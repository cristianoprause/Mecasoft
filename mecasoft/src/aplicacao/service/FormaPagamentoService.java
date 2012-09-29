package aplicacao.service;

import java.util.List;

import banco.modelo.FormaPagamento;
import banco.utils.FormaPagamentoUtils;

public class FormaPagamentoService extends MecasoftService<FormaPagamento>{

	private FormaPagamento forma;
	
	@Override
	public FormaPagamentoUtils getDAO() {
		return getInjector().getInstance(FormaPagamentoUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(forma);
	}

	@Override
	public void delete() {
		getDAO().delete(forma);
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
		return forma;
	}

	public void setForma(FormaPagamento forma) {
		this.forma = forma;
	}

}
