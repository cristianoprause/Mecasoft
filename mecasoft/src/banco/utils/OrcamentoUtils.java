package banco.utils;

import java.util.List;

import banco.modelo.Orcamento;

public interface OrcamentoUtils extends MecasoftUtils<Orcamento>{

	List<Orcamento> findAllByStatus(String status);
	
}
