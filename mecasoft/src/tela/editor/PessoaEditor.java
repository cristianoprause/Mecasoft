package tela.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
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
	private Table tableVeiculos;
	private Table tableProdutos;

	public PessoaEditor() {
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(7, false));
		
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
		
		Button btnFuncionrio = new Button(compositeConteudo, SWT.CHECK);
		btnFuncionrio.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnFuncionrio.setText("Funcion\u00E1rio");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblRazoSocial = new Label(compositeConteudo, SWT.NONE);
		lblRazoSocial.setText("Raz\u00E3o social:");
		
		txtRazaoSocial = new Text(compositeConteudo, SWT.BORDER);
		txtRazaoSocial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
		Label lblCpfcnpj = new Label(compositeConteudo, SWT.NONE);
		lblCpfcnpj.setText("CPF/CNPJ:");
		
		txtCpfCnpj = new Text(compositeConteudo, SWT.BORDER);
		txtCpfCnpj.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
		Label lblRginscricaoEst = new Label(compositeConteudo, SWT.NONE);
		lblRginscricaoEst.setText("RG/Inscri\u00E7\u00E3o Est:");
		
		txtRgInscrEst = new Text(compositeConteudo, SWT.BORDER);
		txtRgInscrEst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
		Label lblCartTrabalhoN = new Label(compositeConteudo, SWT.NONE);
		lblCartTrabalhoN.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCartTrabalhoN.setText("Cart. trabalho N\u00BA:");
		
		txtCartTrabNum = new Text(compositeConteudo, SWT.BORDER);
		txtCartTrabNum.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblSerie = new Label(compositeConteudo, SWT.NONE);
		lblSerie.setText("S\u00E9rie:");
		
		txtSerie = new Text(compositeConteudo, SWT.BORDER);
		txtSerie.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblSalario = new Label(compositeConteudo, SWT.NONE);
		lblSalario.setText("Sal\u00E1rio:");
		
		txtSalario = new Text(compositeConteudo, SWT.BORDER);
		txtSalario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblCargo = new Label(compositeConteudo, SWT.NONE);
		lblCargo.setText("Cargo:");
		
		ComboViewer cvCargo = new ComboViewer(compositeConteudo, SWT.READ_ONLY);
		Combo cbCargo = cvCargo.getCombo();
		cbCargo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblFoneFax = new Label(compositeConteudo, SWT.NONE);
		lblFoneFax.setText("Fone/Fax:");
		
		txtFoneFax = new Text(compositeConteudo, SWT.BORDER);
		txtFoneFax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
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
		txtCep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
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
		txtRua.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblNumero = new Label(compositeConteudo, SWT.NONE);
		lblNumero.setText("N\u00FAmero:");
		
		txtNumero = new Text(compositeConteudo, SWT.BORDER);
		txtNumero.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblComplemento = new Label(compositeConteudo, SWT.NONE);
		lblComplemento.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblComplemento.setText("Complemento:");
		
		txtComplemento = new Text(compositeConteudo, SWT.BORDER | SWT.MULTI);
		GridData gd_txtComplemento = new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1);
		gd_txtComplemento.heightHint = 54;
		txtComplemento.setLayoutData(gd_txtComplemento);
		
		Label lblVeculos = new Label(compositeConteudo, SWT.NONE);
		lblVeculos.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblVeculos.setText("Ve\u00EDculos:");
		
		TableViewer tvVeiculo = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableVeiculos = tvVeiculo.getTable();
		tableVeiculos.setLinesVisible(true);
		tableVeiculos.setHeaderVisible(true);
		GridData gd_tableVeiculos = new GridData(SWT.FILL, SWT.FILL, true, false, 5, 2);
		gd_tableVeiculos.heightHint = 92;
		tableVeiculos.setLayoutData(gd_tableVeiculos);
		
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
		btnRemoverVeiculo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoverVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/lessCar16.png"));
		btnRemoverVeiculo.setText("Remover");
		
		Label lblFornece = new Label(compositeConteudo, SWT.NONE);
		lblFornece.setText("Fornece:");
		
		TableViewer tvProduto = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableProdutos = tvProduto.getTable();
		tableProdutos.setLinesVisible(true);
		tableProdutos.setHeaderVisible(true);
		GridData gd_tableProdutos = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 2);
		gd_tableProdutos.heightHint = 92;
		tableProdutos.setLayoutData(gd_tableProdutos);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvProduto, SWT.NONE);
		TableColumn tblclmnDescricao = tvcDescricao.getColumn();
		tblclmnDescricao.setWidth(193);
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
