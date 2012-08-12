package banco.utils;

import java.util.List;

import banco.modelo.Pessoa;

public interface PessoaUtils extends MecasoftUtils<Pessoa>{

	List<Pessoa> findAllByTipoAndStatus(String tipo, boolean status);
	
	
}
