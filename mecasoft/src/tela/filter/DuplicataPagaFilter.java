package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import aplicacao.helper.FormatterHelper;
import banco.modelo.DuplicataPaga;

public class DuplicataPagaFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if(searchNull())
			return true;
		
		DuplicataPaga d = (DuplicataPaga)element;
		
		if(d.getDuplicata().getNumero().matches(search.toLowerCase()))
			return true;
		
		if(d.getDuplicata().getServicoPrestado().getCliente().getNome().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(FormatterHelper.DATEFORMATDATA.format(d.getDataPagamento()).matches(search.toLowerCase()))
			return true;
		
		if(FormatterHelper.getDecimalFormat().format(d.getDuplicata().getValor()).matches(search.toLowerCase()))
			return true;
		
		if(FormatterHelper.getDecimalFormat().format(d.getValorDesconto()).matches(search.toLowerCase()))
			return true;
		
		if(FormatterHelper.getDecimalFormat().format(d.getValorTotal()).matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
