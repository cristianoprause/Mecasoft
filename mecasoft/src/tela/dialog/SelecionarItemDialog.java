package tela.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class SelecionarItemDialog extends ElementListSelectionDialog{

	public SelecionarItemDialog(Shell parent, ILabelProvider renderer) {
		super(parent, renderer);
		
		setTitle("Selecionar Item");
		setMessage("Selecione um item ou utilize o filtro");
	}
	
	public Object elementoSelecionado(){
		if(open() == IDialogConstants.OK_ID)
			return getFirstResult();
		else
			return null;
	}

}
