package tela.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ErroDialog extends TitleAreaDialog {
	private Text txtErroMessage;
	private String texto;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ErroDialog(Shell parentShell, String texto) {
		super(parentShell);
		this.texto = texto;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(org.eclipse.wb.swt.ResourceManager.getPluginImage("mecasoft", "assents/funcoes/error64.png"));
		setMessage("Não foi possivel completar a operação devido aos seguintes erros");
		setTitle("ERRO");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		txtErroMessage = new Text(container, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtErroMessage.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		txtErroMessage.setForeground(SWTResourceManager.getColor(255, 0, 0));
		txtErroMessage.setEditable(false);
		GridData gd_txtErroMessage = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_txtErroMessage.heightHint = 134;
		txtErroMessage.setLayoutData(gd_txtErroMessage);
		txtErroMessage.setText(texto);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
}
