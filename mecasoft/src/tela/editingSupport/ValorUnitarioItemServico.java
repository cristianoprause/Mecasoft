package tela.editingSupport;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;

import aplicacao.helper.FormatterHelper;
import banco.modelo.ItemServico;

public class ValorUnitarioItemServico extends EditingSupport{

	private TreeViewer viewer;
	private Logger log = Logger.getLogger(getClass());

	public ValorUnitarioItemServico(TreeViewer tvServicoProduto) {
		super(tvServicoProduto);
		this.viewer = tvServicoProduto;
	}

	
	@Override
	protected CellEditor getCellEditor(Object element) {
		TextCellEditor tce = new TextCellEditor(viewer.getTree());
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
				log.error(e);
			}
			
		}
	}

}
