package tela.editingSupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import banco.modelo.ItemServico;

public class ItemVisivelItemServicoEditingSupport extends EditingSupport{

	private TreeViewer viewer;

	public ItemVisivelItemServicoEditingSupport(TreeViewer tvServicoProduto) {
		super(tvServicoProduto);
		this.viewer = tvServicoProduto;
	}
	
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return ((ItemServico)element).isFornecedorVisivel();
	}

	@Override
	protected void setValue(Object element, Object value) {
		ItemServico is = (ItemServico)element;
		is.setFornecedorVisivel((Boolean)value);
		viewer.refresh();
	}

}
