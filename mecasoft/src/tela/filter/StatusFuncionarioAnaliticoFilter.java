package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import aplicacao.helper.FormatterHelper;
import banco.modelo.StatusServico;

public class StatusFuncionarioAnaliticoFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		if(searchNull())
			return true;
		
		StatusServico ss = (StatusServico)element;
		
		if(ss.getStatus().getDescricao().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(FormatterHelper.getDateFormatData("dd/MM/yyyy HH:mm").format(ss.getData()).matches(search.toLowerCase()))
			return true;
		
		if(ss.getServicoPrestado().getId().toString().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
