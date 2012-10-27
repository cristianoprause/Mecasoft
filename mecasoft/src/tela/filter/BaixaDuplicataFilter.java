package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import aplicacao.helper.FormatterHelper;
import banco.modelo.Duplicata;

public class BaixaDuplicataFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
					
		Duplicata d = (Duplicata) element;
		
		if(d.getNumero().matches(search.toLowerCase()))
			return true;
		
		if(d.getServicoPrestado().getId().toString().matches(search.toLowerCase()))
			return true;
		
		if(FormatterHelper.getDateFormatData().format(d.getDataVencimento()).matches(search.toLowerCase()))
			return true;
		
		if(FormatterHelper.getDecimalFormat().format(d.getValor()).matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
