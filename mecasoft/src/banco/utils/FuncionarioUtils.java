package banco.utils;

import java.util.List;

import banco.modelo.Funcionario;
import banco.modelo.TipoFuncionario;

public interface FuncionarioUtils extends MecasoftUtils<Funcionario>{

	List<Funcionario> findAllAtivos();
	List<Funcionario> findAllSemUsuario();
	List<Funcionario> findAllByTipo(TipoFuncionario tipo);
}
