package tela.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.ResourceManager;
import tela.componentes.MecasoftText;

public class ConfiguracaoDialog extends TitleAreaDialog {
	private Text txtEmpresa;
	private Text txtInicioTarde;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConfiguracaoDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Selecione a pessoa cadastrada que representa a empresa e informe os horarios de espediente");
		setTitle("Configura\u00E7\u00F5es");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(5, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblEmpresa = new Label(container, SWT.NONE);
		lblEmpresa.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmpresa.setText("Empresa:");
		
		txtEmpresa = new Text(container, SWT.BORDER);
		txtEmpresa.setEnabled(false);
		txtEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnSelecionar = new Button(container, SWT.NONE);
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");
		
		Label lblPeriodoManh = new Label(container, SWT.NONE);
		lblPeriodoManh.setText("Per\u00EDodo manh\u00E3:");
		
		MecasoftText txtInicioManha = new MecasoftText(container, SWT.NONE);
		GridData gridData = (GridData) txtInicioManha.text.getLayoutData();
		gridData.widthHint = 100;
		gridData.grabExcessHorizontalSpace = false;
		txtInicioManha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblAte = new Label(container, SWT.NONE);
		lblAte.setText("at\u00E9");
		
		MecasoftText txtFinalManha = new MecasoftText(container, SWT.NONE);
		GridData gridData_1 = (GridData) txtFinalManha.text.getLayoutData();
		gridData_1.widthHint = 100;
		gridData_1.grabExcessHorizontalSpace = false;
		txtFinalManha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblPeriodoTarde = new Label(container, SWT.NONE);
		lblPeriodoTarde.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodoTarde.setText("Per\u00EDodo tarde:");
		
		txtInicioTarde = new Text(container, SWT.BORDER);
		txtInicioTarde.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblAt = new Label(container, SWT.NONE);
		lblAt.setText("at\u00E9");
		
		MecasoftText txtFinalTarde = new MecasoftText(container, SWT.NONE);
		GridData gridData_2 = (GridData) txtFinalTarde.text.getLayoutData();
		gridData_2.grabExcessHorizontalSpace = false;
		gridData_2.widthHint = 100;
		new Label(container, SWT.NONE);

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
