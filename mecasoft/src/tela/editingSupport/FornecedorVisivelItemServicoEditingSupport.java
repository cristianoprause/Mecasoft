package tela.editingSupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import banco.modelo.ItemServico;

public class FornecedorVisivelItemServicoEditingSupport extends EditingSupport{

	private TableViewer viewer;

	public FornecedorVisivelItemServicoEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
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
