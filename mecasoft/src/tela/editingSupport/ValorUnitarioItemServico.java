package tela.editingSupport;

import java.math.BigDecimal;
import java.text.ParseException;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import aplicacao.helper.FormatterHelper;
import banco.modelo.ItemServico;

public class ValorUnitarioItemServico extends EditingSupport{

	private TableViewer viewer;

	public ValorUnitarioItemServico(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	
	@Override
	protected CellEditor getCellEditor(Object element) {
		TextCellEditor tce = new TextCellEditor(viewer.getTable());
		return tce;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ItemServico is = (ItemServico)element;
		
		if(is.getValorUnitario() != null)
			return FormatterHelper.getDecimalFormat().format(is.getValorUnitario());
		
		return "";
	}

	@Override
	protected void setValue(Object element, Object value) {
		ItemServico is = (ItemServico)element;
		String val = (String)value;
		
		if(!val.isEmpty()){
			try {
				is.setValorUnitario(new BigDecimal(FormatterHelper.getDecimalFormat().parse(val).toString()));
				is.setTotal(is.getValorUnitario().multiply(new BigDecimal(is.getQuantidade())));
				
				viewer.refresh();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
	}

}
