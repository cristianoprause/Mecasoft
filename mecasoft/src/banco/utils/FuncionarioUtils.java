package banco.utils;

import java.util.List;

import banco.modelo.Funcionario;

public interface FuncionarioUtils extends MecasoftUtils<Funcionario>{

	List<Funcionario> findAllSemUsuario();
	
}
