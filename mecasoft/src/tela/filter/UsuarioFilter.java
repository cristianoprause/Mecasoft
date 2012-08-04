package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.Usuario;

public class UsuarioFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		Usuario u = (Usuario)element;
		
		if(u.getFuncionario().getNomeFantasia().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(u.getLogin().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(u.getPapel().getNome().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(u.getAtivo() && "ativo".matches(search.toLowerCase()))
			return true;
		
		if(!u.getAtivo() && "desativado".matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
