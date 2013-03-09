package tela.filter;

import org.eclipse.jface.viewers.ViewerFilter;

public abstract class MecasoftFilter extends ViewerFilter{

	protected String search;

	public void setSearch(String search){
		this.search = ".*"+search.toLowerCase()+".*";
	}
	
	public Boolean searchNull(){
		return search == null || search.isEmpty();
	}
	
}
