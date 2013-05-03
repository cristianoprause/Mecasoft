package aplicacao.service;

import java.util.List;

import banco.modelo.Caixa;
import banco.utils.CaixaUtils;

public class CaixaService extends MecasoftService<Caixa>{

	@Override
	public CaixaUtils getDAO() {
		return getInjector().getInstance(CaixaUtils.class);
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
		return getModelo();
	}

	public void setCaixa(Caixa caixa) {
		setModelo(caixa);
	}

}
