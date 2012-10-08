package tela.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class SimNaoCancelarDialog extends Dialog {

	private String mensagem;
	
	private int id;
	
	public SimNaoCancelarDialog(Shell parentShell, String mensagem) {
		super(parentShell);
		this.mensagem = mensagem;
		id = IDialogConstants.NO_ID;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		
		Label lblImagem = new Label(container, SWT.NONE);
		lblImagem.setImage(Display.getDefault().getSystemImage(SWT.ICON_QUESTION));
		
		Label lblMensagem = new Label(container, SWT.NONE);
		lblMensagem.setText(mensagem);

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Sim", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Não", false);
		createButton(parent, IDialogConstants.NO_ID, "Cancelar", false);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		this.id = buttonId;
		
		if(buttonId == IDialogConstants.NO_ID)
			close();
		
		super.buttonPressed(buttonId);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 174);
	}
	
	public int getId() {
		return id;
	}

}
