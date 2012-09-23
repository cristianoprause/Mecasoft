package aplicacao.service;

import java.util.List;

import banco.modelo.Pessoa;
import banco.modelo.StatusServico;
import banco.utils.StatusServicoUtils;

public class StatusServicoService extends MecasoftService<StatusServico>{

	private StatusServico statusServico;
	
	@Override
	public StatusServicoUtils getDAO() {
		return getInjector().getInstance(StatusServicoUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(statusServico);
	}

	@Override
	public void delete() {
		getDAO().delete(statusServico);
	}

	@Override
	public StatusServico find(Long id) {
		return getDAO().find(id);
	}
	
	public StatusServico findStatusFuncionario(Pessoa funcionario){
		return getDAO().findStatusFuncionario(funcionario);
	}

	@Override
	public List<StatusServico> findAll() {
		return getDAO().findAll();
	}

	public StatusServico getStatusServico() {
		return statusServico;
	}

	public void setStatusServico(StatusServico statusServico) {
		this.statusServico = statusServico;
	}

}
