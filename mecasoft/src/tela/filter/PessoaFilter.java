package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.Pessoa;

public class PessoaFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		if(searchNull())
			return true;
		
		Pessoa pessoa = (Pessoa)element;
		
		if(pessoa.getNomeFantasia().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(pessoa.getStatus().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(pessoa.getCelular().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(pessoa.getFoneFax().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
