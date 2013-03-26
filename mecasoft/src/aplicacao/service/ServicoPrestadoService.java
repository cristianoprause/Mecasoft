package aplicacao.service;

import java.util.Date;
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
	
	public void saveOrUpdateAutomatic(){
		getDAO().saveOrUpdateAutomatic(servicoPrestado);
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
	
	public List<ServicoPrestado> findAllAtivosByPeriodo(Date dataInicial, Date dataFinal){
		return getDAO().findAllByPeriodoAndStatusAndConclusao(dataInicial, dataFinal, true, null);
	}
	
	public List<ServicoPrestado> findAllNaoCancelado(){
		return getDAO().findAllByStatusAndConclusao(true, null);
	}
	
	public List<ServicoPrestado> findAllNaoConcluidos(){
		return getDAO().findAllByStatusAndConclusao(true, true);
	}

	public List<ServicoPrestado> findAllNaoConcluidosAutomatic(){
		return getDAO().findAllByStatusAndConclusaoAutomatic(true, true);
	}
	
	public ServicoPrestado getServicoPrestado() {
		return servicoPrestado;
	}
	
	public void setServicoPrestado(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}
	
}
