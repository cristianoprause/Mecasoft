package tela.editingSupport;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import aplicacao.helper.FormatterHelper;
import banco.modelo.ItemServico;

public class AcrescimoItemServicoEditingSupport extends EditingSupport{

	private TableViewer viewer;
	
	public AcrescimoItemServicoEditingSupport(TableViewer viewer) {
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
		
		if(is != null)
			return FormatterHelper.getDecimalFormat().format(is.getAcrescimo());

		return "";
	}

	@Override
	protected void setValue(Object element, Object value) {
		try{
			String valor = (String)value;
			ItemServico is = (ItemServico)element;
			
			if(!valor.isEmpty()){
				is.setAcrescimo(new BigDecimal(valor.replace(",", ".")));
				
				BigDecimal total = is.getValorUnitario().multiply(new BigDecimal(is.getQuantidade()))
					.subtract(is.getDesconto()).add(is.getAcrescimo());
				is.setTotal(total);
				
				viewer.refresh();
			}
		}catch(Exception e){}
	}

}
