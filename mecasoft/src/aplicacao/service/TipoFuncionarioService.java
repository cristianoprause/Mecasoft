package aplicacao.service;

import java.util.List;

import banco.modelo.TipoFuncionario;
import banco.utils.TipoFuncionarioUtils;

public class TipoFuncionarioService extends MecasoftService<TipoFuncionario>{

	@Override
	public TipoFuncionarioUtils getDAO() {
		return getInjector().getInstance(TipoFuncionarioUtils.class);
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
	public TipoFuncionario find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<TipoFuncionario> findAll() {
		return getDAO().findAll();
	}
	
	public TipoFuncionario getTipo() {
		return getModelo();
	}

	public void setTipo(TipoFuncionario tipo) {
		setModelo(tipo);
	}
	
}
