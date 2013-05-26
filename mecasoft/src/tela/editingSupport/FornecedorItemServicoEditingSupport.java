package tela.editingSupport;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TreeViewer;

import banco.modelo.ForneceProduto;
import banco.modelo.ItemServico;

public class FornecedorItemServicoEditingSupport extends EditingSupport{

	private TreeViewer viewer;
	
	public FornecedorItemServicoEditingSupport(TreeViewer tvServicoProduto) {
		super(tvServicoProduto);
		this.viewer = tvServicoProduto;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		ItemServico is = (ItemServico) element;
		List<ForneceProduto> listaFornecedor = is.getItem().getListaFornecedores();
		String arrayNomes[] = new String[listaFornecedor.size()];
		
		for(int c = 0; c < listaFornecedor.size(); c++){
			arrayNomes[c] = listaFornecedor.get(c).getPessoa().getNome();
		}
		
		return new ComboBoxCellEditor(viewer.getTree(), arrayNomes);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ItemServico is = (ItemServico)element;
		List<ForneceProduto> listaFornecedor = is.getItem().getListaFornecedores();
		
		if(is.getFornecedor() != null){
			for(int c = 0; c < is.getItem().getListaFornecedores().size(); c++){
				if(is.getFornecedor().equals(listaFornecedor.get(c).getPessoa()))
					return c;
			}
		}
		
		return -1;
	}

	@Override
	protected void setValue(Object element, Object value) {
		ItemServico is = (ItemServico)element;
		Integer posicao = (Integer)value;
		
		if(posicao > -1){
			is.setFornecedor(is.getItem().getListaFornecedores().get(posicao).getPessoa());
			is.setValorUnitario(is.getItem().getListaFornecedores().get(posicao).getValorUnitario());
			
			BigDecimal total = is.getValorUnitario().multiply(new BigDecimal(is.getQuantidade()));
			is.setTotal(total);
			
			viewer.refresh();
		}
	}
	
}
