package banco.utils;

import java.util.List;

import banco.modelo.Status;

public interface StatusUtils extends MecasoftUtils<Status>{

	List<Status> findAllByStatusAndFuncao(boolean status, Boolean pausar);
	
}
