package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.Veiculo;

public class VeiculoFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		Veiculo v = (Veiculo) element;
		
		if(v.getModelo().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(v.getMarca().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(v.getStatus().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(v.getCliente().getNomeFantasia().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
