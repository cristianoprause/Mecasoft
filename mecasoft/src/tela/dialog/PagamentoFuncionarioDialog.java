package tela.dialog;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openInformation;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.MovimentacaoCaixaService;
import aplicacao.service.PessoaService;
import banco.modelo.MovimentacaoCaixa;
import banco.modelo.Pessoa;

public class PagamentoFuncionarioDialog extends TitleAreaDialog {
	private Text txtNome;
	private Text txtTotal;
	private MecasoftText txtSalario;
	private MecasoftText txtDesconto;
	
	private PessoaService service = new PessoaService();
	private MovimentacaoCaixaService movimentacaoService = new MovimentacaoCaixaService();
	private BigDecimal total;
	private BigDecimal desconto;

	public PagamentoFuncionarioDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Pagar funcion\u00E1rio");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblNome = new Label(container, SWT.NONE);
		lblNome.setText("Nome:");
		
		txtNome = new Text(container, SWT.BORDER);
		txtNome.setEnabled(false);
		txtNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionar = new Button(container, SWT.NONE);
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.setPessoa(selecionarFuncionario());
				if(service.getPessoa() == null)
					txtNome.setText("");
				else{
					txtNome.setText(service.getPessoa().getNome());
					
					BigDecimal salario = service.getPessoa().getSalario();
					
					if(salario != null)
						txtSalario.setText(FormatterHelper.getDecimalFormat().format(service.getPessoa().getSalario()));
				}
				
				calcularTotais();
			}
		});
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");
		
		Label lblSalario = new Label(container, SWT.NONE);
		lblSalario.setText("Sal\u00E1rio:");
		
		txtSalario = new MecasoftText(container, SWT.NONE);
		txtSalario.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularTotais();
			}
		});
		txtSalario.setOptions(MecasoftText.NUMEROS, -1);
		txtSalario.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtSalario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblDesconto = new Label(container, SWT.NONE);
		lblDesconto.setText("Desconto:");
		
		txtDesconto = new MecasoftText(container, SWT.NONE);
		txtDesconto.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularTotais();
			}
		});
		txtDesconto.setOptions(MecasoftText.NUMEROS, -1);
		txtDesconto.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtDesconto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblTotal = new Label(container, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal.setText("Total a pagar:");
		
		txtTotal = new Text(container, SWT.BORDER);
		txtTotal.setEnabled(false);
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		return area;
	}
	
	private Pessoa selecionarFuncionario(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNome();
			}
		});
		sid.setElements(service.findAllFuncionarioAPagar().toArray());
		
		return (Pessoa)sid.getElementoSelecionado();
	}
	
	private void calcularTotais(){
		
		if(txtSalario.getText().isEmpty())
			total = BigDecimal.ZERO;
		else
			total = new BigDecimal(txtSalario.getText().replace(",", "."));
		
		if(txtDesconto.getText().isEmpty())
			desconto = BigDecimal.ZERO;
		else
			desconto = new BigDecimal(txtDesconto.getText().replace(",", "."));
		
		total = total.subtract(desconto);
		txtTotal.setText(FormatterHelper.getDecimalFormat().format(total));
		
	}
	
	@Override
	protected void okPressed() {
		
		if(service.getPessoa() == null){
			setErrorMessage("Selecione o funcionário.");
			return;
		}
		
		if(txtSalario.getText().isEmpty()){
			setErrorMessage("Informe o salário do funcionário.");
			return;
		}
		
		BigDecimal totalCaixa = movimentacaoService.getTotalCaixa(UsuarioHelper.getCaixa());
		if(totalCaixa.compareTo(total) < 0){
			setErrorMessage("O caixa não possui dinheiro suficiente para pagar o funcionário.");
			return;
		}
		
		BigDecimal salario = new BigDecimal(txtSalario.getText().replaceAll(",", "."));
		if(salario.compareTo(desconto) < 0){
			setErrorMessage("O desconto não pode ser superior ao salário do funcionário.");
			return;
		}
		
		//gera nova movimentacao
		MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
		movimentacao.setFuncionario(service.getPessoa());
		movimentacao.setMotivo("Pagamento do funcionário " + service.getPessoa().getNome());
		movimentacao.setStatus(MovimentacaoCaixa.STATUSPAGAMENTOFUNCIONARIO);
		movimentacao.setTipo(MovimentacaoCaixa.TIPOSAIDA);
		movimentacao.setValor(total);
		
		movimentacaoService.setMovimentacao(movimentacao);
		movimentacaoService.saveOrUpdate();
		
		//seta a data atual como a data de pagamento do funcionario
		service.getPessoa().setDataUltimoPagto(new Date());
		service.saveOrUpdate();
		
		//commita
//		HibernateConnection.commit(service.getPessoa());
		
		openInformation("Funcionário pago com sucesso!");
		
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
		return new Point(450, 264);
	}

}
