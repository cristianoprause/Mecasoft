package tela.editingSupport;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import aplicacao.helper.FormatterHelper;
import banco.modelo.ForneceProduto;
import banco.modelo.ProdutoServico;

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
			return FormatterHelper.getDecimalFormat().format(fp.getValorUnitario());

		return "";
	}

	@Override
	protected void setValue(Object element, Object value) {
		String valor = (String) value;
		
		if(!valor.isEmpty()){
			try{
				BigDecimal preco = new BigDecimal(valor.replace(".", "").replace(",", "."));
				if(preco.compareTo(BigDecimal.ZERO) >= 0){
					ForneceProduto fp = (ForneceProduto)element;
					fp.setValorUnitario(preco);
					
					calcularValores(fp);
					
					viewer.refresh();
				}
			}catch(Exception e){}
		}
	}
	
	private void calcularValores(ForneceProduto forneceProd){
		BigDecimal media = BigDecimal.ZERO;
		ProdutoServico ps = forneceProd.getId().getProduto();
		
		for(ForneceProduto fp : ps.getListaFornecedores()){
			if(fp.getValorUnitario() != null){
				media = media.add(fp.getValorUnitario());
			}
		}
		
		if(ps.getListaFornecedores().size() != 0)
			media = media.divide(new BigDecimal(ps.getListaFornecedores().size()));
		else
			media = BigDecimal.ZERO;
		
		ps.setValorUnitario(media);
		
	}

}
