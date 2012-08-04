package banco.utils;

import java.util.List;

import banco.modelo.TipoVeiculo;
import banco.modelo.Veiculo;

public interface VeiculoUtils extends MecasoftUtils<Veiculo>{

	List<Veiculo> findAllByTipo(TipoVeiculo tipo);
	
}
