package aplicacao.service;

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
	
}
