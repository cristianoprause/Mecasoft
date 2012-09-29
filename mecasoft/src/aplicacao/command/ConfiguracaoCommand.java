package aplicacao.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import aplicacao.helper.LayoutHelper;

import tela.dialog.ConfiguracaoDialog;

public class ConfiguracaoCommand extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new ConfiguracaoDialog(LayoutHelper.getActiveShell()).open();
		return null;
	}
	
}
