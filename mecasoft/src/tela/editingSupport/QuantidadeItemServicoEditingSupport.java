package tela.editingSupport;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;

import banco.modelo.ItemServico;
import banco.modelo.ProdutoServico;

public class QuantidadeItemServicoEditingSupport extends EditingSupport{

	private TreeViewer viewer;
	
	public QuantidadeItemServicoEditingSupport(TreeViewer tvServicoProduto) {
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
		return ((ItemServico)element).getItem().getTipo().equals(ProdutoServico.TIPOPRODUTO);
	}

	@Override
	protected Object getValue(Object element) {
		ItemServico is = (ItemServico)element;
		
		if(is != null)
			return is.getQuantidade().toString();
		
		return "";
	}

	@Override
	protected void setValue(Object element, Object value) {
		try{
			ItemServico is = (ItemServico)element;
			String quantidade = (String) value;
			
			if(!quantidade.isEmpty()){
				Integer valorQuantidade = Integer.parseInt(quantidade);
				
				if(valorQuantidade.compareTo(0) >= 0){
				
					is.setQuantidade(valorQuantidade);
					
					BigDecimal total = is.getValorUnitario().multiply(new BigDecimal(is.getQuantidade()));
					
					is.setTotal(total);
					viewer.refresh();
				}
			}
		}catch(Exception e){}
		
	}

}
