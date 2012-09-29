package tela.filter;

import org.eclipse.jface.viewers.Viewer;

import banco.modelo.ServicoPrestado;

public class ServicoPrestadoFilter extends MecasoftFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchNull())
			return true;
		
		ServicoPrestado sp = (ServicoPrestado)element;
		
		if(sp.getId().toString().matches(search.toLowerCase()))
			return true;
		
		if(sp.getCliente().getNomeFantasia().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(sp.getVeiculo().getModelo().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(sp.getUltimoStatus().getFuncionario().getNomeFantasia().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		if(sp.getUltimoStatus().getStatus().getDescricao().toLowerCase().matches(search.toLowerCase()))
			return true;
		
		return false;
	}

}
