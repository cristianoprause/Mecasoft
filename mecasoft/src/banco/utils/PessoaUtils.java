package banco.utils;

import java.util.List;

import banco.modelo.Pessoa;
import banco.modelo.TipoFuncionario;

public interface PessoaUtils extends MecasoftUtils<Pessoa>{

	List<Pessoa> findAllByTipoAndStatus(String tipo, Boolean status);
	List<Pessoa> findAllByTipoFuncionario(TipoFuncionario tipo);
	
}
