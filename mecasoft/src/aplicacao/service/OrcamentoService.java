package aplicacao.service;

import java.util.List;

import banco.modelo.Orcamento;
import banco.utils.OrcamentoUtils;

public class OrcamentoService extends MecasoftService<Orcamento>{

	private Orcamento modelo;
	
	@Override
	public OrcamentoUtils getDAO() {
		return getInjector().getInstance(OrcamentoUtils.class);
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
	public Orcamento find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<Orcamento> findAll() {
		return getDAO().findAll();
	}
	
	public List<Orcamento> findAllPendente(){
		return getDAO().findAllByStatus(Orcamento.PENDENTE);
	}
	
	public List<Orcamento> findAllAprovado(){
		return getDAO().findAllByStatus(Orcamento.APROVADO);
	}

	public Orcamento getModelo() {
		return modelo;
	}

	public void setModelo(Orcamento modelo) {
		this.modelo = modelo;
	}

}
