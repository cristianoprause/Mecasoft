package banco.utils;

import java.util.Date;
import java.util.List;

import banco.modelo.DuplicataPaga;

public interface DuplicataPagaUtils extends MecasoftUtils<DuplicataPaga>{

	List<DuplicataPaga> findAllByPeriodo(Date dtInicial, Date dtFinal);
	
}
