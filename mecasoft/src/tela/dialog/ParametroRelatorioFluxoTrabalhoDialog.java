package tela.dialog;

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

public class ParametroRelatorioFluxoTrabalhoDialog extends TitleAreaDialog {
	private Text txtFuncionario;
	private MecasoftText txtDataInicial;
	private MecasoftText txtDataFinal;
	private MecasoftText txtServico;
	
	private Date dtInicial;
	private Date dtFinal;
	private Long servicoNumero;
	private Pessoa funcionario;
	private PessoaService pessoaService = new PessoaService();

	public ParametroRelatorioFluxoTrabalhoDialog(Shell parentShell) {
		super(parentShell);
		dtInicial = null;
		dtFinal = null;
		servicoNumero = null;
		funcionario = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Informe os campos para filtrar os resultados ou deixe-os vazios para trazer todos os resultados");
		setTitle("Fluxo de Trabalho");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(5, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblPerodoDe = new Label(container, SWT.NONE);
		lblPerodoDe.setText("Per\u00EDodo de");
		
		txtDataInicial = new MecasoftText(container, SWT.NONE);
		txtDataInicial.setOptions(MecasoftText.NUMEROS, 10);
		txtDataInicial.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAt = new Label(container, SWT.NONE);
		lblAt.setText("at\u00E9");
		
		txtDataFinal = new MecasoftText(container, SWT.NONE);
		txtDataFinal.setOptions(MecasoftText.NUMEROS, 10);
		txtDataFinal.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblServio = new Label(container, SWT.NONE);
		lblServio.setText("Servi\u00E7o:");
		
		txtServico = new MecasoftText(container, SWT.NONE);
		txtServico.setOptions(MecasoftText.NUMEROS, -1);
		txtServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		new Label(container, SWT.NONE);
		
		Label lblFuncionrio = new Label(container, SWT.NONE);
		lblFuncionrio.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFuncionrio.setText("Funcion\u00E1rio:");
		
		txtFuncionario = new Text(container, SWT.BORDER);
		txtFuncionario.setEnabled(false);
		txtFuncionario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnSelecionarFuncionario = new Button(container, SWT.NONE);
		btnSelecionarFuncionario.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				funcionario = selecionarFuncionario();
				if(funcionario == null)
					txtFuncionario.setText("");
				else
					txtFuncionario.setText(funcionario.getNome());
			}
		});
		btnSelecionarFuncionario.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarFuncionario.setText("Selecionar");

		return area;
	}
	
	private Pessoa selecionarFuncionario(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getParentShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNome();
			}
		});
		
		sid.setElements(pessoaService.findAll().toArray());
		
		return (Pessoa)sid.getElementoSelecionado();
	}
	
	@Override
	protected void okPressed() {
		try{
			if(!txtDataInicial.getText().isEmpty())
				dtInicial = FormatterHelper.DATEFORMATDATA.parse(txtDataInicial.getText());
			
			if(!txtDataFinal.getText().isEmpty())
				dtFinal = FormatterHelper.DATEFORMATDATA.parse(txtDataFinal.getText());
		}catch(Exception e){
			setErrorMessage("Informe as datas corretamente.");
			return;
		}
		
		if(!txtServico.getText().isEmpty())
			servicoNumero = Long.parseLong(txtServico.getText());
		
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
		return new Point(450, 241);
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

	public Long getServicoNumero() {
		return servicoNumero;
	}

	public void setServicoNumero(Long servicoNumero) {
		this.servicoNumero = servicoNumero;
	}

	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}

}
