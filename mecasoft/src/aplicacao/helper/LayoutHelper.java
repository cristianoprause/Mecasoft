package aplicacao.helper;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class LayoutHelper {

	private static Shell activeShell;
	
	public static Shell getActiveShell(){
		if(activeShell == null)
			activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		
		return activeShell;
	}
	
}
