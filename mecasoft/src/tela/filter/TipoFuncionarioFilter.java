package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.TipoFuncionario;

public class TipoFuncionarioFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		TipoFuncionario tipo = (TipoFuncionario)element;
		
		if(tipo.getNome().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
