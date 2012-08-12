package tela.editingSupport;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import banco.modelo.ForneceProduto;

public class ForneceProdutoEditingSupport extends EditingSupport{

	private TableViewer viewer;
	
	public ForneceProdutoEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		TextCellEditor textEditor = new TextCellEditor(viewer.getTable());
		
		return textEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ForneceProduto fp = (ForneceProduto)element;

		if(fp.getValorUnitario() != null)
			return fp.getValorUnitario().toString();
		else
			return "";
	}

	@Override
	protected void setValue(Object element, Object value) {
		String valor = (String) value;
		
		if(!valor.isEmpty()){
			((ForneceProduto)element).setValorUnitario(new BigDecimal(valor));
			viewer.refresh();
		}
	}

}
