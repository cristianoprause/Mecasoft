package tela.dialog;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import java.math.BigDecimal;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
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

import banco.modelo.Pessoa;

import tela.componentes.MecasoftText;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.PessoaService;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ParametroRelatorioServicoSintetico extends TitleAreaDialog {

	private MecasoftText txtServicoNumero;
	private Text txtCliente;
	private MecasoftText txtValorInicial;
	private MecasoftText txtValorFinal;
	
	private Pessoa cliente;
	private Long numeroServico;
	private BigDecimal valorInicial;
	private BigDecimal valorFinal;

	public ParametroRelatorioServicoSintetico(Shell parentShell) {
		super(parentShell);
		cliente = null;
		numeroServico = null;
		valorInicial = null;
		valorFinal = null;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Informe os campos para filtrar os resultados ou deixe-os vazios para trazer todos os resultados");
		setTitle("Relat\u00F3rio de servi\u00E7os (sint\u00E9tico)");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(6, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblServioNmero = new Label(container, SWT.NONE);
		lblServioNmero.setText("Servi\u00E7o n\u00FAmero:");
		
		txtServicoNumero = new MecasoftText(container, SWT.NONE);
		txtServicoNumero.setOptions(MecasoftText.NUMEROS, -1);
		txtServicoNumero.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Label lblValorTotalEntre = new Label(container, SWT.NONE);
		lblValorTotalEntre.setText("Valor total entre");
		
		txtValorInicial = new MecasoftText(container, SWT.NONE);
		txtValorInicial.setOptions(MecasoftText.NUMEROS, -1);
		txtValorInicial.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblE = new Label(container, SWT.NONE);
		lblE.setText("e");
		
		txtValorFinal = new MecasoftText(container, SWT.NONE);
		txtValorFinal.setOptions(MecasoftText.NUMEROS, -1);
		txtValorFinal.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblCliente = new Label(container, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(container, SWT.BORDER);
		txtCliente.setEnabled(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Button btnSelecionar = new Button(container, SWT.NONE);
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cliente = selecionarPessoa();
				if(cliente != null)
					txtCliente.setText(cliente.getNomeFantasia());
			}
		});
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");

		return area;
	}
	
	public Pessoa selecionarPessoa(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		
		sid.setElements(new PessoaService().findAllAtivos().toArray());
		
		return (Pessoa)sid.getElementoSelecionado();
	}
	
	@Override
	protected void okPressed() {
		if(!txtServicoNumero.getText().isEmpty())
			numeroServico = new Long(txtServicoNumero.getText());
		
		if(!txtValorInicial.getText().isEmpty())
			valorInicial = new BigDecimal(txtValorInicial.getText().replaceAll(",", "."));
		
		if(!txtValorFinal.getText().isEmpty())
			valorFinal = new BigDecimal(txtValorFinal.getText().replaceAll(",", "."));
		
		super.okPressed();
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
		return new Point(450, 245);
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public Long getNumeroServico() {
		return numeroServico;
	}

	public void setNumeroServico(Long numeroServico) {
		this.numeroServico = numeroServico;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}
}
