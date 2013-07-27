package aplicacao.command;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import aplicacao.backupRestoreBanco.CriarBackupRestaurar;

public class ExportarBancoCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			FileDialog fd = new FileDialog(getActiveShell(), SWT.SAVE);
			fd.setFilterExtensions(new String[] { ".mecasoft" });

			String caminho = fd.open();

			if (caminho != null)
				new CriarBackupRestaurar().backup(caminho);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
