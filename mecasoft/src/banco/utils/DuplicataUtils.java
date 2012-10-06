package banco.utils;

import java.util.Date;
import java.util.List;

import banco.modelo.Duplicata;

public interface DuplicataUtils extends MecasoftUtils<Duplicata>{

	Duplicata findUltimaDuplicata();
	Duplicata findByNumero(String numero);
	
	List<Duplicata> findAllByPagamento(boolean pago);
	List<Duplicata> findAllByPeriodoAndPagamento(Date dtInicial, Date dtFinal, Boolean pagamento);
	
}
