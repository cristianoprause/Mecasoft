package aplicacao.service;

import java.util.List;

import banco.modelo.Caixa;
import banco.utils.CaixaUtils;

public class CaixaService extends MecasoftService<Caixa>{

	private Caixa caixa;
	
	@Override
	public CaixaUtils getDAO() {
		return getInjector().getInstance(CaixaUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(caixa);
	}

	@Override
	public void delete() {
		getDAO().delete(caixa);
	}

	@Override
	public Caixa find(Long id) {
		return getDAO().find(id);
	}
	
	public Caixa findCaixaAtual(){
		return getDAO().findUltimoCaixaByStatus(true);
	}
	
	public Caixa findUltimoCaixaAberto(){
		return getDAO().findUltimoCaixaByStatus(false);
	}

	@Override
	public List<Caixa> findAll() {
		return getDAO().findAll();
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

}
