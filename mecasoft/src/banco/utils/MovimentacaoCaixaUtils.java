package banco.utils;

import java.util.List;

import banco.modelo.Caixa;
import banco.modelo.MovimentacaoCaixa;

public interface MovimentacaoCaixaUtils extends MecasoftUtils<MovimentacaoCaixa>{
	
	List<MovimentacaoCaixa> findAllByCaixaAndTipo(Caixa caixa, Character tipo);
	
}
