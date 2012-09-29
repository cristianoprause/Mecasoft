package banco.utils;

import java.util.List;

import banco.modelo.FormaPagamento;

public interface FormaPagamentoUtils extends MecasoftUtils<FormaPagamento>{

	List<FormaPagamento> findAllByStatus(Boolean status);
	
}
