package aplicacao.service;

import java.util.List;

import banco.modelo.ServicoPrestado;
import banco.utils.ServicoPrestadoUtils;

public class ServicoPrestadoService extends MecasoftService<ServicoPrestado>{

	private ServicoPrestado servicoPrestado;
	
	@Override
	public ServicoPrestadoUtils getDAO() {
		return getInjector().getInstance(ServicoPrestadoUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(servicoPrestado);
	}

	@Override
	public void delete() {
		getDAO().delete(servicoPrestado);
	}

	@Override
	public ServicoPrestado find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<ServicoPrestado> findAll() {
		return getDAO().findAll();
	}

}
