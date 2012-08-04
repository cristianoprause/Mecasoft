package mecasoft;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class PerspectiveListenerAdapter extends PartListenerAdapter {

	@Override
	public void partClosed(IWorkbenchPart part) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (activePage == null)
			return; // Se não existe página ativa, não continua. Ou seja, o
					// usuário está fechando o programa

		IEditorReference[] refs = activePage.getEditorReferences();

		if (refs.length == 0)
			activePage.setEditorAreaVisible(false);
	}
}