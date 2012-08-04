package aplicacao.helper;

import aplicacao.guice.MecasoftModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorHelper {

	public static Injector injector;
	
	public static Injector getInstance(){
		if(injector == null){
			injector = Guice.createInjector(new MecasoftModule());
		}
		
		return injector;
	}
	
}
