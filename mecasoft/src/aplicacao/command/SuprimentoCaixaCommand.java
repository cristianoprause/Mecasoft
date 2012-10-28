package aplicacao.command;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import tela.dialog.SuprimentoCaixaDialog;

import aplicacao.helper.UsuarioHelper;

public class SuprimentoCaixaCommand extends AbstractHandler{

	private SuprimentoCaixaDialog scd;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(UsuarioHelper.getCaixa() == null){
			openError("O caixa esta fechado.\n Abra-o primeiro para depois realizar um suprimento.");
			return null;
		}
		
		scd = new SuprimentoCaixaDialog(getActiveShell());
		scd.open();
		
		return null;
	}

}
