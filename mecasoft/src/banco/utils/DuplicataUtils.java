package banco.utils;

import banco.modelo.Duplicata;

public interface DuplicataUtils extends MecasoftUtils<Duplicata>{

	Duplicata findUltimaDuplicata();
	
}
