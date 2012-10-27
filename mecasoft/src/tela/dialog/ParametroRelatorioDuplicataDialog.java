package tela.dialog;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import java.util.Date;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.PessoaService;
import banco.modelo.Pessoa;

public class ParametroRelatorioDuplicataDialog extends TitleAreaDialog {
	private Text txtCliente;
	private Text txtFuncionario;
	private MecasoftText txtDtInicial;
	private MecasoftText txtDtFinal;
	private MecasoftText txtServico;
	private Button btnPago;
	
	private PessoaService pessoaService;
	private Pessoa cliente;
	private Pessoa funcionario;
	private Date dtInicial;
	private Date dtFinal;
	private Long numeroServico;
	private boolean pago;

	public ParametroRelatorioDuplicataDialog(Shell parentShell) {
		super(parentShell);
		this.pessoaService = new PessoaService();
		this.cliente = null;
		this.funcionario = null;
		this.dtInicial = null;
		this.dtFinal = null;
		this.numeroServico = null;
		this.pago = false;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Informe os campos para filtrar os resultados ou deixe-os vazios para trazer todos os resultados");
		setTitle("Relat\u00F3rio de duplicatas");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(7, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblPeriodoDe = new Label(container, SWT.NONE);
		lblPeriodoDe.setText("Per\u00EDodo de");
		
		txtDtInicial = new MecasoftText(container, SWT.NONE);
		txtDtInicial.setOptions(MecasoftText.NUMEROS, 10);
		txtDtInicial.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDtInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblAte = new Label(container, SWT.NONE);
		lblAte.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAte.setText("at\u00E9");
		
		txtDtFinal = new MecasoftText(container, SWT.NONE);
		txtDtFinal.setOptions(MecasoftText.NUMEROS, 10);
		txtDtFinal.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDtFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(container, SWT.NONE);
		
		Label lblNServico = new Label(container, SWT.NONE);
		lblNServico.setText("N\u00BA servi\u00E7o:");
		
		txtServico = new MecasoftText(container, SWT.NONE);
		txtServico.setOptions(MecasoftText.NUMEROS, -1);
		txtServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1));
		new Label(container, SWT.NONE);
		
		Label lblCliente = new Label(container, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(container, SWT.BORDER);
		txtCliente.setEditable(false);
		txtCliente.setEnabled(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Button btnSelecionarCliente = new Button(container, SWT.NONE);
		btnSelecionarCliente.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cliente = selecionarPessoa();
				if(cliente != null)
					txtCliente.setText(cliente.getNome());
				else
					txtCliente.setText("");
			}
		});
		btnSelecionarCliente.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarCliente.setText("Selecionar");
		
		Label lblFuncionario = new Label(container, SWT.NONE);
		lblFuncionario.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFuncionario.setText("Funcion\u00E1rio:");
		
		txtFuncionario = new Text(container, SWT.BORDER);
		txtFuncionario.setEditable(false);
		txtFuncionario.setEnabled(false);
		txtFuncionario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Button btnSelecionarFuncionario = new Button(container, SWT.NONE);
		btnSelecionarFuncionario.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				funcionario = selecionarPessoa();
				if(funcionario != null)
					txtFuncionario.setText(funcionario.getNome());
				else
					txtFuncionario.setText("");
			}
		});
		btnSelecionarFuncionario.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarFuncionario.setText("Selecionar");
		
		btnPago = new Button(container, SWT.CHECK);
		btnPago.setText("Pago");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		return area;
	}
	
	private Pessoa selecionarPessoa(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNome();
			}
		});
		sid.setElements(pessoaService.findAll().toArray());
		
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	@Override
	protected void okPressed() {
		try{
			if(!txtDtInicial.getText().isEmpty())
				dtInicial = FormatterHelper.getDateFormatData().parse(txtDtInicial.getText());
			
			if(!txtDtFinal.getText().isEmpty())
				dtFinal = FormatterHelper.getDateFormatData().parse(txtDtFinal.getText());
			
		}catch(Exception e){
			setErrorMessage("Informe as datas corretamente.");
			return;
		}
		
		if(!txtServico.getText().isEmpty())
			numeroServico = Long.parseLong(txtServico.getText());
		
		pago = btnPago.getSelection();
		
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
		return new Point(422, 300);
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public Long getNumeroServico() {
		return numeroServico;
	}

	public void setNumeroServico(Long numeroServico) {
		this.numeroServico = numeroServico;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

}
