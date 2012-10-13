package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.Pessoa;
import banco.modelo.StatusServico;

import aplicacao.service.StatusServicoService;

public class StatusFuncionarioFilter extends MecasoftFilter{

	private StatusServicoService statusService = new StatusServicoService();
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		if(searchNull())
			return true;
		
		Pessoa funcionario = (Pessoa)element;
		
		if(funcionario.getNome().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		StatusServico status = statusService.findStatusFuncionario(funcionario);
		
		if(status != null && status.getStatus().getDescricao().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
