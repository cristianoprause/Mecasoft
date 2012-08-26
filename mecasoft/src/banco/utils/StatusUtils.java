package banco.utils;

import java.util.List;

import banco.modelo.Status;

public interface StatusUtils extends MecasoftUtils<Status>{

	List<Status> findAllByStatus(boolean status);
	
}
