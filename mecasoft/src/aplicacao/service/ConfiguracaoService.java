package aplicacao.service;

import java.util.List;

import banco.modelo.Configuracao;
import banco.utils.ConfiguracaoUtils;

public class ConfiguracaoService extends MecasoftService<Configuracao>{

	private Configuracao configuracao;
	
	@Override
	public ConfiguracaoUtils getDAO() {
		return getInjector().getInstance(ConfiguracaoUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(configuracao);
	}

	@Override
	public void delete() {
		getDAO().delete(configuracao);
	}

	@Override
	public Configuracao find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<Configuracao> findAll() {
		return getDAO().findAll();
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

}
