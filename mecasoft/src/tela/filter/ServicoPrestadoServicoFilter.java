package tela.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import banco.modelo.ItemServico;
import banco.modelo.ProdutoServico;

public class ServicoPrestadoServicoFilter extends ViewerFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		ItemServico item = (ItemServico)element;
		
		if(item.getItem().getTipo().equals(ProdutoServico.TIPOSERVICO))
			return true;
		
		return false;
	}

}
