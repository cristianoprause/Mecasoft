package banco.utils;

import java.util.List;

import banco.modelo.ProdutoServico;

public interface ProdutoServicoUtils extends MecasoftUtils<ProdutoServico>{

	List<ProdutoServico> findAllByTipo(String tipo);
	
}
