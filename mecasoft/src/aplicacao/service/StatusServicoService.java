package aplicacao.service;

import java.util.Date;
import java.util.List;

import banco.modelo.Pessoa;
import banco.modelo.ServicoPrestado;
import banco.modelo.StatusServico;
import banco.utils.StatusServicoUtils;

public class StatusServicoService extends MecasoftService<StatusServico>{

	@Override
	public StatusServicoUtils getDAO() {
		return getInjector().getInstance(StatusServicoUtils.class);
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
	
	public List<StatusServico> findAllByFuncionarioAndPeriodoAndServico(Pessoa funcionario, Date dtInicial, Date dtFinal, ServicoPrestado servico){
		return getDAO().findAllByFuncionarioAndPeriodoAndServico(funcionario, dtInicial, dtFinal, servico);
	}
	
	public List<StatusServico> findAllByFuncionarioAndPeriodo(Pessoa funcionario, Date dtInicial, Date dtFinal){
		return getDAO().findAllByFuncionarioAndPeriodoAndServico(funcionario, dtInicial, dtFinal, null);
	}
	
	public StatusServico getStatusServico() {
		return getModelo();
	}

	public void setStatusServico(StatusServico statusServico) {
		setModelo(statusServico);
	}

}
