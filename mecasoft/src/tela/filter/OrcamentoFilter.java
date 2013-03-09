package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import aplicacao.helper.FormatterHelper;
import banco.modelo.Orcamento;

public class OrcamentoFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		Orcamento orcamento = (Orcamento) element;
		
		if(orcamento.getNumero().matches(search))
			return true;
		
		if(orcamento.getCliente().getNome().toLowerCase().matches(search))
			return true;
		
		if(orcamento.getVeiculo().getMarca().toLowerCase().matches(search))
			return true;
		
		if(orcamento.getVeiculo().getModelo().toLowerCase().matches(search))
			return true;
		
		if(FormatterHelper.formatMoedaDuasCasas(orcamento.getValorTotal()).matches(search))
			return true;
		
		return false;
	}

}
