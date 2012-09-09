package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.FormaPagamento;

public class FormaPagamentoFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		FormaPagamento fp = (FormaPagamento)element;
		
		if(fp.getNome().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(fp.isGeraPagVista() && "gera pagamento à vista".matches(search.toLowerCase()))
			return true;
		
		if(fp.isGeraDuplicata() && "gera duplicatas".matches(search.toLowerCase()))
			return true;
		
		if(fp.getStatus().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
