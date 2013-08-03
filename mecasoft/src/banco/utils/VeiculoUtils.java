package banco.utils;

import java.util.List;

import banco.modelo.Pessoa;
import banco.modelo.Veiculo;

public interface VeiculoUtils extends MecasoftUtils<Veiculo>{
	
	List<Veiculo> findAllByPessoa(Pessoa pessoa);
	
}
