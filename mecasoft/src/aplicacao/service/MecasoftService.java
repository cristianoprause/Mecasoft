package aplicacao.service;

import java.util.List;

import com.google.inject.Injector;

import aplicacao.helper.InjectorHelper;
import banco.utils.MecasoftUtils;

public abstract class MecasoftService<T> {

	public abstract MecasoftUtils<T> getDAO();
	
	public Boolean isDirty(){
		return getDAO().isDirty();
	}
	
	public Injector getInjector(){
	
		return InjectorHelper.getInstance();
		
	}
	
	public abstract void saveOrUpdate();
	public abstract void delete();
	public abstract T find(Long id);
	public abstract List<T> findAll();
	
}
