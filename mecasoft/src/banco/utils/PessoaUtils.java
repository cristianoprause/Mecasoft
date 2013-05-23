package banco.utils;

import java.util.List;

import com.google.inject.ImplementedBy;

import banco.DAO.PessoaDAO;
import banco.modelo.Pessoa;
import banco.modelo.TipoFuncionario;

@ImplementedBy(PessoaDAO.class)
public interface PessoaUtils extends MecasoftUtils<Pessoa>{

	List<Pessoa> findAllByTipoAndStatus(String tipo, Boolean status);
	List<Pessoa> findAllByTipoFuncionario(TipoFuncionario tipo);
	List<Pessoa> findAllFuncionarioAPagar();
	
}
