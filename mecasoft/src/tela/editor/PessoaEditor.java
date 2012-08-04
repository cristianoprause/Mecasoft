package tela.editor;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.ResourceManager;

public class PessoaEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.PessoaEditor"; //$NON-NLS-1$
	private Text txtNomeFantasia;
	private Text txtRazaoSocial;
	private Text txtCpfCnpj;
	private Text txtRgInscrEst;
	private Text txtCartTrabNum;
	private Text txtSerie;
	private Text txtSalario;
	private Text txtFoneFax;
	private Text txtCelular;
	private Text txtEmail;
	private Text txtCep;
	private Text txtCidade;
	private Text txtBairro;
	private Text txtRua;
	private Text txtNumero;
	private Text txtComplemento;
	private Table tableVeiculo;
	private Table tableProduto;

	public PessoaEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		compositeConteudo.setLayout(new GridLayout(1, false));
		
		Label lblNomeFantasia = new Label(compositeConteudo, SWT.NONE);
		lblNomeFantasia.setText("Nome fantasia:");
		
		txtNomeFantasia = new Text(compositeConteudo, SWT.BORDER);
		txtNomeFantasia.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Button btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setText("Ativo");
		
		Label lblTipo = new Label(compositeConteudo, SWT.NONE);
		lblTipo.setText("Tipo:");
		
		Button btnCliente = new Button(compositeConteudo, SWT.CHECK);
		btnCliente.setText("Cliente");
		
		Button btnFornecedor = new Button(compositeConteudo, SWT.CHECK);
		btnFornecedor.setText("Fornecedor");
		
		Button btnFuncionario = new Button(compositeConteudo, SWT.CHECK);
		GridData gd_btnFuncionario = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFuncionario.widthHint = 105;
		btnFuncionario.setLayoutData(gd_btnFuncionario);
		btnFuncionario.setText("Funcion\u00E1rio");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblRazoSocial = new Label(compositeConteudo, SWT.NONE);
		lblRazoSocial.setText("Raz\u00E3o social:");
		
		txtRazaoSocial = new Text(compositeConteudo, SWT.BORDER);
		txtRazaoSocial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));
		
		Label lblCpfCnpj = new Label(compositeConteudo, SWT.NONE);
		lblCpfCnpj.setText("CPF/CNPJ:");
		
		txtCpfCnpj = new Text(compositeConteudo, SWT.BORDER);
		txtCpfCnpj.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));
		
		Label lblRgInscrEst = new Label(compositeConteudo, SWT.NONE);
		lblRgInscrEst.setText("RG/Inscri\u00E7\u00E3o Est:");
		
		txtRgInscrEst = new Text(compositeConteudo, SWT.BORDER);
		txtRgInscrEst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
		Label lblCartTrabalhoN = new Label(compositeConteudo, SWT.NONE);
		lblCartTrabalhoN.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCartTrabalhoN.setText("Cart. trabalho N\u00BA:");
		
		txtCartTrabNum = new Text(compositeConteudo, SWT.BORDER);
		GridData gd_txtCartTrabNum = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_txtCartTrabNum.widthHint = 136;
		txtCartTrabNum.setLayoutData(gd_txtCartTrabNum);
		
		Label lblSerie = new Label(compositeConteudo, SWT.NONE);
		lblSerie.setText("S\u00E9rie:");
		
		txtSerie = new Text(compositeConteudo, SWT.BORDER);
		txtSerie.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblSalario = new Label(compositeConteudo, SWT.NONE);
		lblSalario.setText("Sal\u00E1rio:");
		
		txtSalario = new Text(compositeConteudo, SWT.BORDER);
		txtSalario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		Label lblCargo = new Label(compositeConteudo, SWT.NONE);
		lblCargo.setText("Cargo:");
		
		Combo cbCargo = new Combo(compositeConteudo, SWT.READ_ONLY);
		cbCargo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblFonefax = new Label(compositeConteudo, SWT.NONE);
		lblFonefax.setText("Fone/Fax:");
		
		txtFoneFax = new Text(compositeConteudo, SWT.BORDER);
		txtFoneFax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		Label lblCelular = new Label(compositeConteudo, SWT.NONE);
		lblCelular.setText("Celular:");
		
		txtCelular = new Text(compositeConteudo, SWT.BORDER);
		txtCelular.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblEmail = new Label(compositeConteudo, SWT.NONE);
		lblEmail.setText("E-mail:");
		
		txtEmail = new Text(compositeConteudo, SWT.BORDER);
		txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
		Label lblCep = new Label(compositeConteudo, SWT.NONE);
		lblCep.setText("CEP:");
		
		txtCep = new Text(compositeConteudo, SWT.BORDER);
		txtCep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		Label lblCidade = new Label(compositeConteudo, SWT.NONE);
		lblCidade.setText("Cidade:");
		
		txtCidade = new Text(compositeConteudo, SWT.BORDER);
		txtCidade.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblBairro = new Label(compositeConteudo, SWT.NONE);
		lblBairro.setText("Bairro:");
		
		txtBairro = new Text(compositeConteudo, SWT.BORDER);
		txtBairro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
		Label lblRua = new Label(compositeConteudo, SWT.NONE);
		lblRua.setText("Rua:");
		
		txtRua = new Text(compositeConteudo, SWT.BORDER);
		txtRua.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		Label lblNmero = new Label(compositeConteudo, SWT.NONE);
		lblNmero.setText("N\u00FAmero:");
		
		txtNumero = new Text(compositeConteudo, SWT.BORDER);
		txtNumero.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblComplemento = new Label(compositeConteudo, SWT.NONE);
		lblComplemento.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblComplemento.setText("Complemento:");
		
		txtComplemento = new Text(compositeConteudo, SWT.BORDER | SWT.MULTI);
		GridData gd_txtComplemento = new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1);
		gd_txtComplemento.heightHint = 58;
		txtComplemento.setLayoutData(gd_txtComplemento);
		
		Label lblVeiculos = new Label(compositeConteudo, SWT.NONE);
		lblVeiculos.setText("Ve\u00EDculos:");
		
		TableViewer tvVeiculo = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableVeiculo = tvVeiculo.getTable();
		tableVeiculo.setLinesVisible(true);
		tableVeiculo.setHeaderVisible(true);
		GridData gd_tableVeiculo = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 2);
		gd_tableVeiculo.heightHint = 95;
		tableVeiculo.setLayoutData(gd_tableVeiculo);
		
		TableViewerColumn tvcModelo = new TableViewerColumn(tvVeiculo, SWT.NONE);
		TableColumn tblclmnModelo = tvcModelo.getColumn();
		tblclmnModelo.setWidth(100);
		tblclmnModelo.setText("Modelo");
		
		TableViewerColumn tvcPlaca = new TableViewerColumn(tvVeiculo, SWT.NONE);
		TableColumn tblclmnPlaca = tvcPlaca.getColumn();
		tblclmnPlaca.setWidth(100);
		tblclmnPlaca.setText("Placa");
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvVeiculo, SWT.NONE);
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");
		
		Button btnAdicionarVeiculo = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/plusCar16.png"));
		btnAdicionarVeiculo.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemoverVeiculo = new Button(compositeConteudo, SWT.NONE);
		btnRemoverVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/lessCar16.png"));
		btnRemoverVeiculo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoverVeiculo.setText("Remover");
		
		Label lblFornece = new Label(compositeConteudo, SWT.NONE);
		lblFornece.setText("Fornece:");
		
		TableViewer tvProduto = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableProduto = tvProduto.getTable();
		tableProduto.setLinesVisible(true);
		tableProduto.setHeaderVisible(true);
		GridData gd_tableProduto = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 2);
		gd_tableProduto.heightHint = 95;
		tableProduto.setLayoutData(gd_tableProduto);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvProduto, SWT.NONE);
		TableColumn tblclmnDescricao = tvcDescricao.getColumn();
		tblclmnDescricao.setWidth(248);
		tblclmnDescricao.setText("Descri\u00E7\u00E3o");
		
		TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvProduto, SWT.NONE);
		TableColumn tblclmnValorUnitario = tvcValorUnitario.getColumn();
		tblclmnValorUnitario.setWidth(100);
		tblclmnValorUnitario.setText("Valor unit\u00E1rio");
		
		Button btnAdicionarProduto = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productAdd16.png"));
		btnAdicionarProduto.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemoverProduto = new Button(compositeConteudo, SWT.NONE);
		btnRemoverProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemoverProduto.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoverProduto.setText("Remover");
		
	}

	@Override
	public void salvarRegistro() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluirRegistro() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

}
