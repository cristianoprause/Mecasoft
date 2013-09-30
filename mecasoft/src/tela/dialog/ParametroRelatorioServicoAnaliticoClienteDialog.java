package tela.dialog;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.service.ServicoPrestadoService;
import banco.modelo.ServicoPrestado;

public class ParametroRelatorioServicoAnaliticoClienteDialog extends TitleAreaDialog {

	private ServicoPrestado servico;
	private ServicoPrestadoService servicoService = new ServicoPrestadoService();
	
	private MecasoftText txtNumeroServico;
	
	public ParametroRelatorioServicoAnaliticoClienteDialog(Shell parentShell) {
		super(parentShell);
		servico = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Relat\u00F3rio de servi\u00E7o anal\u00EDtico para o cliente");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblServico = new Label(container, SWT.NONE);
		lblServico.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServico.setText("Servi\u00E7o:");
		
		txtNumeroServico = new MecasoftText(container, SWT.NONE);
		txtNumeroServico.text.setEditable(false);
		txtNumeroServico.setOptions(MecasoftText.NUMEROS, -1);
		txtNumeroServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionar = new Button(container, SWT.NONE);
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				servico = selecionarServico();
				
				if(servico != null)
					txtNumeroServico.setText(servico.getId() + " - " + servico.getCliente().getNome());
				else
					txtNumeroServico.setText("");
			}
		});
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");

		return area;
	}
	
	private ServicoPrestado selecionarServico(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				ServicoPrestado servico = (ServicoPrestado)element;
				return servico.getId() + " - " + (servico.getOrcamento() == null ? "" : servico.getOrcamento().getNumero()) + " - " + servico.getCliente().getNome();
			}
		});
		
		sid.setElements(servicoService.findAllNaoCancelado().toArray());
		
		return (ServicoPrestado) sid.getElementoSelecionado();
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
		if(servico == null){
			setErrorMessage("Selecione o serviço.");
			return;
		}
		
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 185);
	}

	public ServicoPrestado getServico() {
		return servico;
	}

	public void setServico(ServicoPrestado servico) {
		this.servico = servico;
	}

}
