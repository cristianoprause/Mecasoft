package tela.contentProvider;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import banco.modelo.ItemServico;
import banco.modelo.ProdutoServico;

public class ItemServicoContentProvider implements ITreeContentProvider{

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		List<ItemServico> listaItem = (List<ItemServico>) inputElement;
		return listaItem.toArray(new ItemServico[listaItem.size()]);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		ItemServico item = (ItemServico) parentElement;
		return item.getListaItem().toArray();
	}

	@Override
	public Object getParent(Object element) {
		ItemServico item = (ItemServico)element;
		
		if(item.getServico() != null)
			return item.getServico();
		
		return item;
	}

	@Override
	public boolean hasChildren(Object element) {
		ItemServico item = (ItemServico)element;
		
		if(item.getItem().getTipo().equals(ProdutoServico.TIPOSERVICO))
			return true;
		
		return false;
	}

}
