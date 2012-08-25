package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.Status;

public class StatusFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		Status status = (Status)element;
		
		if(status.getDescricao().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(status.getStatus().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(status.isPausar() && "pausa o serviço".matches(search.toLowerCase()))
			return true;
		
		if(!status.isPausar() && "da continuidade ao serviço".matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
