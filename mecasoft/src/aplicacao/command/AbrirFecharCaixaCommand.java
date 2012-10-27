package aplicacao.command;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import tela.dialog.AbrirFecharCaixaDialog;

public class AbrirFecharCaixaCommand extends AbstractHandler{
	
	private AbrirFecharCaixaDialog afcd;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		afcd = new AbrirFecharCaixaDialog(getActiveShell());
		afcd.open();
		
		return null;
	}

}
