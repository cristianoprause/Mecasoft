package aplicacao.command;

import static aplicacao.helper.MessageHelper.openQuestion;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

public class ReiniciarCommand extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(openQuestion("Tem certeza que deseja reiniciar o sistema?"))
			return PlatformUI.getWorkbench().restart();
		
		return null;
	}

}
