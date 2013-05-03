package aplicacao.service;

import java.util.Date;
import java.util.List;

import banco.modelo.ServicoPrestado;
import banco.utils.ServicoPrestadoUtils;

public class ServicoPrestadoService extends MecasoftService<ServicoPrestado>{

	@Override
	public ServicoPrestadoUtils getDAO() {
		return getInjector().getInstance(ServicoPrestadoUtils.class);
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
	public ServicoPrestado find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<ServicoPrestado> findAll() {
		return getDAO().findAll();
	}
	
	public List<ServicoPrestado> findAllAtivosByPeriodo(Date dataInicial, Date dataFinal){
		return getDAO().findAllByPeriodoAndStatusAndConclusao(dataInicial, dataFinal, true, null);
	}
	
	public List<ServicoPrestado> findAllNaoCancelado(){
		return getDAO().findAllByStatusAndConclusao(true, null);
	}
	
	public List<ServicoPrestado> findAllNaoConcluidos(){
		return getDAO().findAllByStatusAndConclusao(true, true);
	}

	public ServicoPrestado getServicoPrestado() {
		return getModelo();
	}
	
	public void setServicoPrestado(ServicoPrestado servicoPrestado) {
		setModelo(servicoPrestado);
	}
	
}
