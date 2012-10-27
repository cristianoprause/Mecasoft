package banco.utils;

import banco.modelo.Caixa;

public interface CaixaUtils extends MecasoftUtils<Caixa>{

	Caixa findUltimoCaixaByStatus(Boolean status);
	
}
