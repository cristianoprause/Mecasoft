package tela.dialog;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.wb.swt.SWTResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.MovimentacaoCaixaService;
import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;
import banco.modelo.ForneceProduto;
import banco.modelo.Pessoa;
import banco.modelo.ProdutoServico;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class MovimentacaoEstoqueDialog extends TitleAreaDialog {
	private Text txtProduto;
	private Text txtFornecedor;
	private Text txtValorUnitario;
	private Label lblAPagar;
	private MecasoftText txtQuantidade;
	
	private ProdutoServico produto;
	private Pessoa fornecedor;
	private Integer quantidade;
	private BigDecimal valorTotal;
	private BigDecimal valorUnitario;
	private ProdutoServicoService produtoService = new ProdutoServicoService();
	private PessoaService pessoaService = new PessoaService();
	private MovimentacaoCaixaService movimentacaoService = new MovimentacaoCaixaService();

	public MovimentacaoEstoqueDialog(Shell parentShell) {
		super(parentShell);
		produto = null;
		fornecedor = null;
		quantidade = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Adicionar produtos ao estoque");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblProdutos = new Label(container, SWT.NONE);
		lblProdutos.setText("Produtos:");
		
		txtProduto = new Text(container, SWT.BORDER);
		txtProduto.setEnabled(false);
		txtProduto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionarProduto = new Button(container, SWT.NONE);
		btnSelecionarProduto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				produto = selecionarProduto();
				if(produto != null)
					txtProduto.setText(produto.getDescricao());
				else
					txtProduto.setText("");
				
				calcularValores();
			}
		});
		btnSelecionarProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarProduto.setText("Selecionar");
		
		Label lblFornecedor = new Label(container, SWT.NONE);
		lblFornecedor.setText("Fornecedor:");
		
		txtFornecedor = new Text(container, SWT.BORDER);
		txtFornecedor.setEnabled(false);
		txtFornecedor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionarFornecedor = new Button(container, SWT.NONE);
		btnSelecionarFornecedor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fornecedor = selecionarFornecedor();
				
				if(fornecedor != null)
					txtFornecedor.setText(fornecedor.getNome());
				else
					txtFornecedor.setText("");
				
				calcularValores();
			}
		});
		btnSelecionarFornecedor.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarFornecedor.setText("Selecionar");
		
		Label lblQuantidade = new Label(container, SWT.NONE);
		lblQuantidade.setText("Quantidade:");
		
		txtQuantidade = new MecasoftText(container, SWT.NONE);
		txtQuantidade.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularValores();
			}
		});
		txtQuantidade.setOptions(MecasoftText.NUMEROS, -1);
		txtQuantidade.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblValorUnitario = new Label(container, SWT.NONE);
		lblValorUnitario.setText("Valor unit\u00E1rio:");
		
		txtValorUnitario = new Text(container, SWT.BORDER);
		txtValorUnitario.setEnabled(false);
		txtValorUnitario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblTotalPagar = new Label(container, SWT.NONE);
		lblTotalPagar.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblTotalPagar.setText("Total a pagar:");
		
		lblAPagar = new Label(container, SWT.NONE);
		lblAPagar.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblAPagar.setText("R$ 0,00");
		new Label(container, SWT.NONE);

		return area;
	}
	
	private ProdutoServico selecionarProduto(){
		List<ProdutoServico> listaProduto = fornecedor == null ? produtoService.findAllProdutosAtivos() :
			                 findAllProdutoByFornecedor();
		
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ProdutoServico)element).getDescricao() + " Quant: " + ((ProdutoServico)element).getQuantidade().toString();
			}
		});
		sid.setElements(listaProduto.toArray());
		
		return (ProdutoServico) sid.getElementoSelecionado();
	}
	
	private Pessoa selecionarFornecedor(){
		List<Pessoa> listaFornecedor = produto == null ? pessoaService.findAllFornecedoresAtivos() :
			         findAllPessoaByProduto();
		
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNome();
			}
		});
		sid.setElements(listaFornecedor.toArray());
		
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	@Override
	protected void okPressed() {
		
		if(produto == null){
			setErrorMessage("Selecione um produto.");
			return;
		}
		
		if(fornecedor == null){
			setErrorMessage("Selecione um fornecedor.");
			return;
		}
		
		if(quantidade == null || quantidade.compareTo(0) == 0){
			setErrorMessage("Informe a quantidade.");
			return;
		}
		
		BigDecimal totalCaixa = movimentacaoService.getTotalCaixa(UsuarioHelper.getCaixa());
		if(valorTotal.compareTo(totalCaixa) > 0){
			setErrorMessage("O caixa não possui dinheiro suficiente para realizar a movimentação.");
			return;
		}
		
		super.okPressed();
	}
	
	private List<ProdutoServico> findAllProdutoByFornecedor(){
		List<ProdutoServico> listaProduto = new ArrayList<ProdutoServico>();
			
		for(ForneceProduto fp : fornecedor.getListaProduto()){
			listaProduto.add(fp.getId().getProduto());
		}
			
		return listaProduto;
	}
	
	private List<Pessoa> findAllPessoaByProduto(){
		List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		
		for(ForneceProduto fp : produto.getListaFornecedores()){
			listaPessoa.add(fp.getId().getPessoa());
		}
		
		return listaPessoa;
	}
	
	private BigDecimal getValor(){
		BigDecimal valorUnitario = BigDecimal.ZERO;
		
		if(fornecedor != null && produto != null){
			
			for(ForneceProduto fp : produto.getListaFornecedores()){
				if(fp.getId().getPessoa().equals(fornecedor))
					valorUnitario = fp.getValorUnitario();
			}
			
		}
		
		return valorUnitario;
	}
	
	private void calcularValores(){
		valorUnitario = getValor();
		txtValorUnitario.setText(FormatterHelper.getDecimalFormat().format(valorUnitario));
		
		quantidade = txtQuantidade.getText().isEmpty() ? 0 : Integer.parseInt(txtQuantidade.getText());
		
		valorTotal = valorUnitario.multiply(new BigDecimal(quantidade));
		lblAPagar.setText("R$ " + FormatterHelper.getDecimalFormat().format(valorTotal));
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

	public ProdutoServico getProduto() {
		return produto;
	}

	public void setProduto(ProdutoServico produto) {
		this.produto = produto;
	}

	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}
