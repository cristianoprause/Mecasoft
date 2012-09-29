package tela.editingSupport;

import static aplicacao.helper.MessageHelper.openWarning;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import aplicacao.helper.FormatterHelper;
import banco.modelo.ItemServico;

public class DescontoItemServicoEditingSupport extends EditingSupport{

	private TableViewer viewer;
	
	public DescontoItemServicoEditingSupport(TableViewer viewer) {
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
		ItemServico item = (ItemServico)element;
		
		if(item != null)
			return FormatterHelper.getDecimalFormat().format(item.getDesconto());

		return "";
	}

	@Override
	protected void setValue(Object element, Object value) {
		try{
			String valor = (String) value;
			ItemServico is = (ItemServico)element;
			
			if(!valor.isEmpty()){
				
				BigDecimal desconto = new BigDecimal(valor.replace(",", "."));
				BigDecimal total = is.getValorUnitario().multiply(new BigDecimal(is.getQuantidade()))
					.subtract(desconto).add(is.getAcrescimo());
				
				if(total.compareTo(BigDecimal.ZERO) < 0){
					openWarning("O desconto não pode ser superior ao valor total.");
					return;
				}
				
				is.setDesconto(desconto);
				is.setTotal(total);
				
				viewer.refresh();
			}
		}catch(Exception e){}
				
	}

}
