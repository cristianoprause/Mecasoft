package tela.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import tela.componentes.MecasoftText;

public class ParametroRelatorioServicoAnaliticoClienteDialog extends TitleAreaDialog {

	private Long numero;
	
	private MecasoftText txtNumeroServico;
	
	public ParametroRelatorioServicoAnaliticoClienteDialog(Shell parentShell) {
		super(parentShell);
		numero = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Informe os campos para filtrar os resultados");
		setTitle("Relat\u00F3rio de servi\u00E7o anal\u00EDtico para o cliente");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblServicoNumero = new Label(container, SWT.NONE);
		lblServicoNumero.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServicoNumero.setText("Servi\u00E7o N\u00BA:");
		
		txtNumeroServico = new MecasoftText(container, SWT.NONE);
		txtNumeroServico.setOptions(MecasoftText.NUMEROS, -1);
		txtNumeroServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void okPressed() {
		if(txtNumeroServico.getText().isEmpty()){
			setErrorMessage("Informe o número do serviço.");
			return;
		}
		
		numero = Long.parseLong(txtNumeroServico.getText());
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 176);
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

}
