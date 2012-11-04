package aplicacao.command;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import tela.dialog.PagamentoFuncionarioDialog;

import aplicacao.helper.UsuarioHelper;

public class PagamentoFuncionarioCommand extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(UsuarioHelper.getCaixa() == null){
			openError("O caixa esta fechado.\nAbra-o primeiro para depois pagar os funcionários.");
			return null;
		}
		
		new PagamentoFuncionarioDialog(getActiveShell()).open();
		
		return null;
	}

}
