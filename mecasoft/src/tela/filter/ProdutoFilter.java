package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.ProdutoServico;

public class ProdutoFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		ProdutoServico p = (ProdutoServico)element;
		
		if(p.getDescricao().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(p.getStatus().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(p.getValorUnitario().toString().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(p.getQuantidade().toString().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
