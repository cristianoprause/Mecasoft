package tela.dialog;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import java.math.BigDecimal;
import java.util.Calendar;
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
import aplicacao.service.ProdutoServicoService;
import aplicacao.service.VeiculoService;
import banco.modelo.Pessoa;
import banco.modelo.ProdutoServico;
import banco.modelo.Veiculo;

public class ParametroRelatorioServicoAnaliticoDialog extends TitleAreaDialog {
	private Text txtCliente;
	private Text txtVeiculo;
	private Text txtProduto;
	
	private Pessoa cliente;
	private Veiculo veiculo;
	private ProdutoServico produtoServico;
	private Date dtInicial;
	private Date dtFinal;
	private BigDecimal valorInicial;
	private BigDecimal valorFinal;
	private Long numeroServico;
	
	private PessoaService pessoaService = new PessoaService();
	private VeiculoService veiculoService = new VeiculoService();
	private ProdutoServicoService produtoServicoService = new ProdutoServicoService();
	private MecasoftText txtDataInicial;
	private MecasoftText txtDataFinal;
	private MecasoftText txtNumeroServico;
	private MecasoftText txtValorInicial;
	private MecasoftText txtValorFinal;

	public ParametroRelatorioServicoAnaliticoDialog(Shell parentShell) {
		super(parentShell);
		cliente = null;
		veiculo = null;
		produtoServico = null;
		dtInicial = null;
		dtFinal = null;
		valorInicial = null;
		valorFinal = null;
		numeroServico = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Informe os campos para filtrar os resultados ou deixe-os vazios para trazer todos os resultados");
		setTitle("Relat\u00F3rio de servi\u00E7os (Anal\u00EDtico)");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(7, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblPerodoDe = new Label(container, SWT.NONE);
		lblPerodoDe.setText("Per\u00EDodo de");
		
		txtDataInicial = new MecasoftText(container, SWT.NONE);
		txtDataInicial.setOptions(MecasoftText.NUMEROS, 10);
		txtDataInicial.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblAt = new Label(container, SWT.NONE);
		lblAt.setText("at\u00E9");
		
		txtDataFinal = new MecasoftText(container, SWT.NONE);
		txtDataFinal.setOptions(MecasoftText.NUMEROS, 10);
		txtDataFinal.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(container, SWT.NONE);
		
		Label lblNServico = new Label(container, SWT.NONE);
		lblNServico.setText("N\u00BA servi\u00E7o:");
		
		txtNumeroServico = new MecasoftText(container, SWT.NONE);
		txtNumeroServico.setOptions(MecasoftText.NUMEROS, -1);
		txtNumeroServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1));
		new Label(container, SWT.NONE);
		
		Label lblCliente = new Label(container, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(container, SWT.BORDER);
		txtCliente.setEnabled(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Button btnSelecionarServico = new Button(container, SWT.NONE);
		btnSelecionarServico.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarServico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cliente = selecionarCliente();
				if(cliente != null)
					txtCliente.setText(cliente.getNome());
				else
					txtCliente.setText("");
			}
		});
		btnSelecionarServico.setText("Selecionar");
		
		Label lblVeculo = new Label(container, SWT.NONE);
		lblVeculo.setText("Ve\u00EDculo:");
		
		txtVeiculo = new Text(container, SWT.BORDER);
		txtVeiculo.setEnabled(false);
		txtVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Button btnSelecionarVeiculo = new Button(container, SWT.NONE);
		btnSelecionarVeiculo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				veiculo = selecionarVeiculo();
				if(veiculo != null)
					txtVeiculo.setText(veiculo.getModelo());
				else
					txtVeiculo.setText("");
			}
		});
		btnSelecionarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarVeiculo.setText("Selecionar");
		
		Label lblProdutoservio = new Label(container, SWT.NONE);
		lblProdutoservio.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProdutoservio.setText("Produto/Servi\u00E7o:");
		
		txtProduto = new Text(container, SWT.BORDER);
		txtProduto.setEnabled(false);
		txtProduto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Button btnSelecionarProduto = new Button(container, SWT.NONE);
		btnSelecionarProduto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				produtoServico = selecionarProdutoServico();
				if(produtoServico != null)
					txtProduto.setText(produtoServico.getDescricao());
				else
					txtProduto.setText("");
			}
		});
		btnSelecionarProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarProduto.setText("Selecionar");
		
		Label lblValorTotalEntre = new Label(container, SWT.NONE);
		lblValorTotalEntre.setText("Valor total entre");
		
		txtValorInicial = new MecasoftText(container, SWT.NONE);
		txtValorInicial.setOptions(MecasoftText.NUMEROS, -1);
		txtValorInicial.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblE = new Label(container, SWT.NONE);
		lblE.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblE.setText("e");
		
		txtValorFinal = new MecasoftText(container, SWT.NONE);
		txtValorFinal.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		txtValorFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		new Label(container, SWT.NONE);

		return area;
	}
	
	private Pessoa selecionarCliente(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNome();
			}
		});
		sid.setElements(pessoaService.findAll().toArray());
		
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	private Veiculo selecionarVeiculo(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getModelo().concat(" - ");
			}
		});
		sid.setElements(veiculoService.findAll().toArray());
		
		return (Veiculo)sid.getElementoSelecionado();
	}
	
	private ProdutoServico selecionarProdutoServico(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ProdutoServico)element).getDescricao().concat(" - ").concat(((ProdutoServico)element).getTipo());
			}
		});
		sid.setElements(produtoServicoService.findAll().toArray());
		
		return (ProdutoServico)sid.getElementoSelecionado();
	}
	
	@Override
	protected void okPressed() {
		try{
			if(!txtDataInicial.getText().isEmpty())
				dtInicial = FormatterHelper.getDateFormatData().parse(txtDataInicial.getText());
			
			if(!txtDataFinal.getText().isEmpty()){
				dtFinal = FormatterHelper.getDateFormatData().parse(txtDataFinal.getText());
				
				//adiciona mais um dia para que apareçam todos os serviços do dia informado
				Calendar c = Calendar.getInstance();
				c.setTime(dtFinal);
				c.add(Calendar.DAY_OF_MONTH, 1);
				
				//joga a data com para o dtFinal
				dtFinal = c.getTime();
			}
			
		}catch(Exception e){
			setErrorMessage("Informe as datas corretamente.");
			return;
		}
		
		if(!txtNumeroServico.getText().isEmpty())
			numeroServico = new Long(txtNumeroServico.getText());
		
		if(!txtValorInicial.getText().isEmpty())
			valorInicial = new BigDecimal(txtValorInicial.getText().replace(",", "."));
		
		if(!txtValorFinal.getText().isEmpty())
			valorFinal = new BigDecimal(txtValorFinal.getText().replace(",", "."));
		
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
		return new Point(442, 325);
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
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

	public Long getNumeroServico() {
		return numeroServico;
	}

	public void setNumeroServico(Long numeroServico) {
		this.numeroServico = numeroServico;
	}

}
