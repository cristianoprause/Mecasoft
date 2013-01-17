package tela.dialog;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.LabelProvider;
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

import banco.modelo.Pessoa;

import aplicacao.service.PessoaService;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PontoDialog extends TitleAreaDialog {
	
	private PessoaService service = new PessoaService();
	private Pessoa funcionario;
	
	private Text txtFuncionario;

	public PontoDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Sistema de ponto");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblFuncionario = new Label(container, SWT.NONE);
		lblFuncionario.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		lblFuncionario.setText("Funcion\u00E1rio:");
		
		txtFuncionario = new Text(container, SWT.BORDER);
		txtFuncionario.setEnabled(false);
		txtFuncionario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionar = new Button(container, SWT.NONE);
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				funcionario = selecionarFuncionario();
				if(funcionario == null)
					txtFuncionario.setText("");
				else
					txtFuncionario.setText(funcionario.getNome());
			}
		});
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");

		return area;
	}
	
	public Pessoa selecionarFuncionario(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNome();
			}
		});
		
		sid.setElements(service.findAllFuncionariosAtivos().toArray());
		
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	@Override
	protected void okPressed() {
		if(funcionario == null){
			setErrorMessage("Selecione um funcionário.");
			return;
		}
			
		super.okPressed();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}

}
