package aplicacao.service;

import java.util.List;

import aplicacao.helper.UsuarioHelper;
import banco.modelo.Status;
import banco.utils.StatusUtils;

public class StatusService extends MecasoftService<Status>{

	private Status status;
	
	@Override
	public StatusUtils getDAO() {
		return getInjector().getInstance(StatusUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(status);
	}

	@Override
	public void delete() {
		getDAO().delete(status);
	}

	@Override
	public Status find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<Status> findAll() {
		return getDAO().findAll();
	}
	
	public List<Status> findAllAtivos(){
		List<Status> listaStatus = getDAO().findAllByStatusAndFuncao(true, null);
		
		if(UsuarioHelper.getConfiguracaoPadrao() != null)
			listaStatus.remove(UsuarioHelper.getConfiguracaoPadrao().getStatusFinalizarServico());
		
		return listaStatus;
	}
	
	public List<Status> findAllAtivosByFuncao(boolean pausar){
		return getDAO().findAllByStatusAndFuncao(true, pausar);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
