package aplicacao.command;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;
import static aplicacao.helper.MessageHelper.openInformation;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import banco.connection.HibernateConnection;
import banco.modelo.MovimentacaoCaixa;
import banco.modelo.MovimentacaoEstoque;
import banco.modelo.ProdutoServico;

import aplicacao.helper.UsuarioHelper;
import aplicacao.service.MovimentacaoCaixaService;
import aplicacao.service.MovimentacaoEstoqueService;
import aplicacao.service.ProdutoServicoService;

import tela.dialog.MovimentacaoEstoqueDialog;

public class MovimentacaoEstoqueCommand extends AbstractHandler{

	private MovimentacaoEstoqueDialog med;
	private MovimentacaoEstoqueService estoqueService = new MovimentacaoEstoqueService();
	private MovimentacaoCaixaService caixaService = new MovimentacaoCaixaService();
	private ProdutoServicoService produtoService = new ProdutoServicoService();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(UsuarioHelper.getCaixa() == null){
			openError("O caixa esta fechado.\n Abra-o primeiro para depois registrar movimentações no estoque.");
			return null;
		}
		
		med = new MovimentacaoEstoqueDialog(getActiveShell());
		if(med.open() == IDialogConstants.OK_ID){
			try{
				MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();
				movimentacaoEstoque.setFornecedor(med.getFornecedor());
				movimentacaoEstoque.setProduto(med.getProduto());
				movimentacaoEstoque.setQuantidade(med.getQuantidade());
				movimentacaoEstoque.setTipo(MovimentacaoEstoque.TIPOENTRADA);
				movimentacaoEstoque.setValorTotal(med.getValorTotal());
				movimentacaoEstoque.setValorUnitario(med.getValorUnitario());
				
				estoqueService.setMovimentacao(movimentacaoEstoque);
				estoqueService.saveOrUpdate();
				
				MovimentacaoCaixa movimentacaoCaixa = new MovimentacaoCaixa();
				movimentacaoCaixa.setMotivo("Compra do produto " + med.getProduto().getDescricao() + " para o estoque.");
				movimentacaoCaixa.setMovimentacaoEstoque(movimentacaoEstoque);
				movimentacaoCaixa.setStatus(MovimentacaoCaixa.STATUSCOMPRA);
				movimentacaoCaixa.setTipo(MovimentacaoCaixa.TIPOSAIDA);
				movimentacaoCaixa.setValor(med.getValorTotal());
				
				caixaService.setMovimentacao(movimentacaoCaixa);
				caixaService.saveOrUpdate();
				
				ProdutoServico produto = med.getProduto();
				produto.setQuantidade(produto.getQuantidade() + med.getQuantidade());
				
				produtoService.setProdutoServico(produto);
				produtoService.saveOrUpdate();
				
				openInformation("Movimentação no estoque realizada com sucesso!");
				HibernateConnection.commit();
				
			}catch(Exception e){
				openError("Ouve um erro ao registrar a movimentação:\n" + e.getMessage());
				HibernateConnection.rollBack();
			}
		}
		return null;
	}

}
