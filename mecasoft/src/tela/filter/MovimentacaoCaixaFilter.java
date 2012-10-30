package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import aplicacao.helper.FormatterHelper;
import banco.modelo.MovimentacaoCaixa;

public class MovimentacaoCaixaFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		if(searchNull())
			return true;
		
		MovimentacaoCaixa mc = (MovimentacaoCaixa)element;
		
		if(mc.getId().toString().matches(search.toLowerCase()))
			return true;
		
		if(FormatterHelper.getDateFormatData().format(mc.getDataMovimentacao()).matches(search.toLowerCase()))
			return true;
		
		if(mc.getTipo().toString().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(mc.getStatus().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(mc.getMotivo().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
