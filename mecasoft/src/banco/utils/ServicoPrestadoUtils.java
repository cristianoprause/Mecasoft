package banco.utils;

import java.util.Date;
import java.util.List;

import banco.modelo.ServicoPrestado;

public interface ServicoPrestadoUtils extends MecasoftUtils<ServicoPrestado>{

	void saveOrUpdateAutomatic(ServicoPrestado servico);
	List<ServicoPrestado> findAllByStatusAndConclusaoAutomatic(Boolean status, Boolean emExecucao);
	
	List<ServicoPrestado> findAllByPeriodoAndStatusAndConclusao(Date dataInicial, Date dataFinal, Boolean status, Boolean emExecucao);
	List<ServicoPrestado> findAllByStatusAndConclusao(Boolean status, Boolean emExecucao);
	
}
