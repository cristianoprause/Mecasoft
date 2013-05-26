package banco.utils;

import java.util.List;

public interface MecasoftUtils<T> extends TFEUtils{
	
	void saveOrUpdate(T modelo);
	void delete(T modelo);
	void commit();
	void rollBack();
	T find(Long id);
	List<T> findAll();

}
