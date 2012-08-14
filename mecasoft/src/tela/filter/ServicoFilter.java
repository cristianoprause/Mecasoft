package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.ProdutoServico;

public class ServicoFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		ProdutoServico ps = (ProdutoServico)element;
		
		if(ps.getDescricao().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(ps.getValorUnitario().toString().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(ps.getAtivo() && "ativo".matches(search.toLowerCase()))
			return true;
		
		if(!ps.getAtivo() && "desativado".matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
