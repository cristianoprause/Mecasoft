package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.Papel;

public class PapelFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		Papel papel = (Papel)element;
		
		if(papel.getNome().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
