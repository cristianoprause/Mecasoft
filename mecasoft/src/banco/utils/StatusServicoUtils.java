package banco.utils;

import banco.modelo.Pessoa;
import banco.modelo.StatusServico;

public interface StatusServicoUtils extends MecasoftUtils<StatusServico>{

	StatusServico findStatusFuncionario(Pessoa funcionario);
	
}
