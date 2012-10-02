package tela.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import banco.modelo.ItemServico;
import banco.modelo.ProdutoServico;

public class ServicoPrestadoProdutoFilter extends ViewerFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		ItemServico item = (ItemServico)element;
		
		if(item.getItem().getTipo().equals(ProdutoServico.TIPOPRODUTO))
			return true;
		
		return false;
	}

}
