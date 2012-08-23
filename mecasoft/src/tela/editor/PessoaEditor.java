package tela.editor;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.ValidatorHelper.validar;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import tela.dialog.SelecionarItemDialog;
import tela.editingSupport.ForneceProdutoEditingSupport;
import tela.editor.editorInput.PessoaEditorInput;
import tela.editor.editorInput.VeiculoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.LayoutHelper;
import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;
import aplicacao.service.TipoFuncionarioService;
import banco.modelo.ForneceProduto;
import banco.modelo.ProdutoServico;
import banco.modelo.TipoFuncionario;
import banco.modelo.Veiculo;


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
	private Button btnAdicionarVeiculo;
	private Button btnDesativarVeiculo;
	private Button btnAdicionarProduto;
	private Button btnRemoverProduto;
	private ComboViewer cvCargo; 
	private Button btnFuncionrio;
	private Button btnAtivo;
	private Button btnCliente;
	private Button btnFornecedor;
	
	private PessoaService service = new PessoaService();
	private List<TipoFuncionario> tiposFuncionarios;
	private TableViewer tvVeiculo;
	private TableViewer tvProduto;
	private Combo cbCargo;

	public PessoaEditor() {
		tiposFuncionarios = new TipoFuncionarioService().findAll();
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(7, false));
		
		Label lblNomeFantasia = new Label(compositeConteudo, SWT.NONE);
		lblNomeFantasia.setText("Nome fantasia:");
		
		txtNomeFantasia = new Text(compositeConteudo, SWT.BORDER);
		txtNomeFantasia.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setText("Ativo");
		
		Label lblTipo = new Label(compositeConteudo, SWT.NONE);
		lblTipo.setText("Tipo:");
		
		btnCliente = new Button(compositeConteudo, SWT.CHECK);
		btnCliente.setText("Cliente");
		
		btnFornecedor = new Button(compositeConteudo, SWT.CHECK);
		btnFornecedor.setText("Fornecedor");
		
		btnFuncionrio = new Button(compositeConteudo, SWT.CHECK);
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
		
		cvCargo = new ComboViewer(compositeConteudo, SWT.READ_ONLY);
		cbCargo = cvCargo.getCombo();
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
		
		tvVeiculo = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableVeiculos = tvVeiculo.getTable();
		tableVeiculos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvVeiculo.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				Veiculo v = (Veiculo)selecao.getFirstElement();
				
				if(v.getAtivo()){
					btnDesativarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/lessCar16.png"));
					btnDesativarVeiculo.setText("Desativar");
				}else{
					btnDesativarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/plusCar16.png"));
					btnDesativarVeiculo.setText("Ativar");
				}
			}
		});
		tableVeiculos.setLinesVisible(true);
		tableVeiculos.setHeaderVisible(true);
		GridData gd_tableVeiculos = new GridData(SWT.FILL, SWT.FILL, true, false, 5, 2);
		gd_tableVeiculos.heightHint = 92;
		tvVeiculo.setContentProvider(ArrayContentProvider.getInstance());
		tableVeiculos.setLayoutData(gd_tableVeiculos);
		
		TableViewerColumn tvcModelo = new TableViewerColumn(tvVeiculo, SWT.NONE);
		tvcModelo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getModelo();
			}
		});
		TableColumn tblclmnModelo = tvcModelo.getColumn();
		tblclmnModelo.setWidth(100);
		tblclmnModelo.setText("Modelo");
		
		TableViewerColumn tvcPlaca = new TableViewerColumn(tvVeiculo, SWT.NONE);
		tvcPlaca.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getPlaca();
			}
		});
		TableColumn tblclmnPlaca = tvcPlaca.getColumn();
		tblclmnPlaca.setWidth(100);
		tblclmnPlaca.setText("Placa");
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvVeiculo, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Veiculo v = (Veiculo)element;
				
				if(v.getAtivo())
					return "Ativo";
				else
					return "Desativado";
			}
		});
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");
		
		btnAdicionarVeiculo = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarVeiculo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					getSite().getPage().openEditor(new VeiculoEditorInput(service), VeiculoEditor.ID);
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnAdicionarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/plusCar16.png"));
		btnAdicionarVeiculo.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		btnDesativarVeiculo = new Button(compositeConteudo, SWT.NONE);
		btnDesativarVeiculo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvVeiculo.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				Veiculo v = (Veiculo)selecao.getFirstElement();
				v.setAtivo(!v.getAtivo());
				
				if(v.getAtivo()){
					btnDesativarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/lessCar16.png"));
					btnDesativarVeiculo.setText("Desativar");
				}else{
					btnDesativarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/plusCar16.png"));
					btnDesativarVeiculo.setText("Ativar");
				}
				
				tvVeiculo.refresh();
			}
		});
		btnDesativarVeiculo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnDesativarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/car/lessCar16.png"));
		btnDesativarVeiculo.setText("Desativar");
		
		Label lblFornece = new Label(compositeConteudo, SWT.NONE);
		lblFornece.setText("Fornece:");
		
		tvProduto = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableProdutos = tvProduto.getTable();
		tableProdutos.setLinesVisible(true);
		tableProdutos.setHeaderVisible(true);
		GridData gd_tableProdutos = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 2);
		gd_tableProdutos.heightHint = 92;
		tvProduto.setContentProvider(ArrayContentProvider.getInstance());
		tableProdutos.setLayoutData(gd_tableProdutos);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvProduto, SWT.NONE);
		tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ForneceProduto)element).getId().getProduto().getDescricao();
			}
		});
		TableColumn tblclmnDescricao = tvcDescricao.getColumn();
		tblclmnDescricao.setWidth(193);
		tblclmnDescricao.setText("Descri\u00E7\u00E3o");
		
		TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvProduto, SWT.NONE);
		tvcValorUnitario.setEditingSupport(new ForneceProdutoEditingSupport(tvProduto));
		tvcValorUnitario.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				try{
					return FormatterHelper.DECIMALFORMAT.format(((ForneceProduto)element).getValorUnitario());
				}catch(Exception e){
					return "";
				}
			}
		});
		TableColumn tblclmnValorUnitario = tvcValorUnitario.getColumn();
		tblclmnValorUnitario.setWidth(100);
		tblclmnValorUnitario.setText("Valor unit\u00E1rio");
		
		btnAdicionarProduto = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarProduto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProdutoServico ps = selecionarProduto();
				if(ps != null){
					ForneceProduto fp = new ForneceProduto();
					fp.getId().setPessoa(service.getPessoa());
					fp.getId().setProduto(ps);
					service.getPessoa().getListaProduto().add(fp);
					tvProduto.refresh();
				}
			}
		});
		btnAdicionarProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productAdd16.png"));
		btnAdicionarProduto.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		btnRemoverProduto = new Button(compositeConteudo, SWT.NONE);
		btnRemoverProduto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvProduto.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(openQuestion("Deseja realmente remover este produto da lista?")){
					ForneceProduto fp = (ForneceProduto)selecao.getFirstElement();
					service.getPessoa().getListaProduto().remove(fp);
					tvProduto.refresh();
				}
			}
		});
		btnRemoverProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemoverProduto.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoverProduto.setText("Remover");
		
		initDataBindings();
	}

	@Override
	public void salvarRegistro() {
		try {
			validar(service.getPessoa());
			
			if(service.getPessoa().getTipoFuncionario()){
				
				if(service.getPessoa().getCarteiraNum().isEmpty()){
					setErroMessage("Informe o número da carteira de trabalho.");
					return;
				}
				
				if(service.getPessoa().getSerie().isEmpty()){
					setErroMessage("informe a série.");
					return;
				}
				
				if(service.getPessoa().getSalario() == null){
					setErroMessage("Informe o salário.");
					return;
				}
				
				if(service.getPessoa().getTipo() == null){
					setErroMessage("Selecione o cargo.");
					return;
				}
				
				service.saveOrUpdate();
				
			}else
				service.saveOrUpdate();
			
			openInformation("Pessoa cadastrada com sucesso!");
			closeThisEditor();
			
		} catch (ValidationException e) {
			setErroMessage(e.getMessage());
		}
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setShowExcluir(false);
		
		PessoaEditorInput pei = (PessoaEditorInput)input;
		
		if(pei.getPessoa().getId() != null)
			service.setPessoa(service.find(pei.getPessoa().getId()));
		else
			service.setPessoa(pei.getPessoa());
		
		setSite(site);
		setInput(input);
	}
	
	private ProdutoServico selecionarProduto(){
		SelecionarItemDialog sid = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ProdutoServico)element).getDescricao();
			}
		});
		sid.setElements(new ProdutoServicoService().findAllProdutos().toArray());
		
		return (ProdutoServico) sid.getElementoSelecionado();
	}

	@Override
	public void setFocus() {
		tvProduto.refresh();
		tvVeiculo.refresh();
	}
	
	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtNomeFantasiaObserveTextObserveWidget = SWTObservables.observeText(txtNomeFantasia, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioNomeFantasiaObserveValue = PojoObservables.observeValue(service.getPessoa(), "nomeFantasia");
		bindingContext.bindValue(txtNomeFantasiaObserveTextObserveWidget, funcionarioServicegetFuncionarioNomeFantasiaObserveValue, null, null);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue funcionarioServicegetFuncionarioAtivoObserveValue = PojoObservables.observeValue(service.getPessoa(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, funcionarioServicegetFuncionarioAtivoObserveValue, null, null);
		//
		IObservableValue btnClienteObserveSelectionObserveWidget = SWTObservables.observeSelection(btnCliente);
		IObservableValue funcionarioServicegetFuncionarioTipoClienteObserveValue = PojoObservables.observeValue(service.getPessoa(), "tipoCliente");
		bindingContext.bindValue(btnClienteObserveSelectionObserveWidget, funcionarioServicegetFuncionarioTipoClienteObserveValue, null, null);
		//
		IObservableValue btnFornecedorObserveSelectionObserveWidget = SWTObservables.observeSelection(btnFornecedor);
		IObservableValue funcionarioServicegetFuncionarioTipoFornecedorObserveValue = PojoObservables.observeValue(service.getPessoa(), "tipoFornecedor");
		bindingContext.bindValue(btnFornecedorObserveSelectionObserveWidget, funcionarioServicegetFuncionarioTipoFornecedorObserveValue, null, null);
		//
		IObservableValue btnFuncionrioObserveSelectionObserveWidget = SWTObservables.observeSelection(btnFuncionrio);
		IObservableValue funcionarioServicegetFuncionarioTipoFuncionarioObserveValue = PojoObservables.observeValue(service.getPessoa(), "tipoFuncionario");
		bindingContext.bindValue(btnFuncionrioObserveSelectionObserveWidget, funcionarioServicegetFuncionarioTipoFuncionarioObserveValue, null, null);
		//
		IObservableValue txtRazaoSocialObserveTextObserveWidget = SWTObservables.observeText(txtRazaoSocial, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioRazaoSocialObserveValue = PojoObservables.observeValue(service.getPessoa(), "razaoSocial");
		bindingContext.bindValue(txtRazaoSocialObserveTextObserveWidget, funcionarioServicegetFuncionarioRazaoSocialObserveValue, null, null);
		//
		IObservableValue txtCpfCnpjObserveTextObserveWidget = SWTObservables.observeText(txtCpfCnpj, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioCpfCnpjObserveValue = PojoObservables.observeValue(service.getPessoa(), "cpfCnpj");
		bindingContext.bindValue(txtCpfCnpjObserveTextObserveWidget, funcionarioServicegetFuncionarioCpfCnpjObserveValue, null, null);
		//
		IObservableValue txtRgInscrEstObserveTextObserveWidget = SWTObservables.observeText(txtRgInscrEst, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioRgInscricaoEstObserveValue = PojoObservables.observeValue(service.getPessoa(), "rgInscricaoEst");
		bindingContext.bindValue(txtRgInscrEstObserveTextObserveWidget, funcionarioServicegetFuncionarioRgInscricaoEstObserveValue, null, null);
		//
		IObservableValue txtCartTrabNumObserveTextObserveWidget = SWTObservables.observeText(txtCartTrabNum, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioCarteiraNumObserveValue = PojoObservables.observeValue(service.getPessoa(), "carteiraNum");
		bindingContext.bindValue(txtCartTrabNumObserveTextObserveWidget, funcionarioServicegetFuncionarioCarteiraNumObserveValue, null, null);
		//
		IObservableValue txtSerieObserveTextObserveWidget = SWTObservables.observeText(txtSerie, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioSerieObserveValue = PojoObservables.observeValue(service.getPessoa(), "serie");
		bindingContext.bindValue(txtSerieObserveTextObserveWidget, funcionarioServicegetFuncionarioSerieObserveValue, null, null);
		//
		IObservableValue txtSalarioObserveTextObserveWidget = SWTObservables.observeText(txtSalario, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioSalarioObserveValue = PojoObservables.observeValue(service.getPessoa(), "salario");
		bindingContext.bindValue(txtSalarioObserveTextObserveWidget, funcionarioServicegetFuncionarioSalarioObserveValue, null, null);
		//
		IObservableValue txtFoneFaxObserveTextObserveWidget = SWTObservables.observeText(txtFoneFax, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioFoneFaxObserveValue = PojoObservables.observeValue(service.getPessoa(), "foneFax");
		bindingContext.bindValue(txtFoneFaxObserveTextObserveWidget, funcionarioServicegetFuncionarioFoneFaxObserveValue, null, null);
		//
		IObservableValue txtCelularObserveTextObserveWidget = SWTObservables.observeText(txtCelular, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioCelularObserveValue = PojoObservables.observeValue(service.getPessoa(), "celular");
		bindingContext.bindValue(txtCelularObserveTextObserveWidget, funcionarioServicegetFuncionarioCelularObserveValue, null, null);
		//
		IObservableValue txtEmailObserveTextObserveWidget = SWTObservables.observeText(txtEmail, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioEmailObserveValue = PojoObservables.observeValue(service.getPessoa(), "email");
		bindingContext.bindValue(txtEmailObserveTextObserveWidget, funcionarioServicegetFuncionarioEmailObserveValue, null, null);
		//
		IObservableValue txtCepObserveTextObserveWidget = SWTObservables.observeText(txtCep, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioCepObserveValue = PojoObservables.observeValue(service.getPessoa(), "cep");
		bindingContext.bindValue(txtCepObserveTextObserveWidget, funcionarioServicegetFuncionarioCepObserveValue, null, null);
		//
		IObservableValue txtCidadeObserveTextObserveWidget = SWTObservables.observeText(txtCidade, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioCidadeObserveValue = PojoObservables.observeValue(service.getPessoa(), "cidade");
		bindingContext.bindValue(txtCidadeObserveTextObserveWidget, funcionarioServicegetFuncionarioCidadeObserveValue, null, null);
		//
		IObservableValue txtBairroObserveTextObserveWidget = SWTObservables.observeText(txtBairro, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioBairroObserveValue = PojoObservables.observeValue(service.getPessoa(), "bairro");
		bindingContext.bindValue(txtBairroObserveTextObserveWidget, funcionarioServicegetFuncionarioBairroObserveValue, null, null);
		//
		IObservableValue txtRuaObserveTextObserveWidget = SWTObservables.observeText(txtRua, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioRuaObserveValue = PojoObservables.observeValue(service.getPessoa(), "rua");
		bindingContext.bindValue(txtRuaObserveTextObserveWidget, funcionarioServicegetFuncionarioRuaObserveValue, null, null);
		//
		IObservableValue txtNumeroObserveTextObserveWidget = SWTObservables.observeText(txtNumero, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioNumeroObserveValue = PojoObservables.observeValue(service.getPessoa(), "numero");
		bindingContext.bindValue(txtNumeroObserveTextObserveWidget, funcionarioServicegetFuncionarioNumeroObserveValue, null, null);
		//
		IObservableValue txtComplementoObserveTextObserveWidget = SWTObservables.observeText(txtComplemento, SWT.Modify);
		IObservableValue funcionarioServicegetFuncionarioComplementoObserveValue = PojoObservables.observeValue(service.getPessoa(), "complemento");
		bindingContext.bindValue(txtComplementoObserveTextObserveWidget, funcionarioServicegetFuncionarioComplementoObserveValue, null, null);
		//
		IObservableValue btnAdicionarProdutoObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnAdicionarProduto);
		bindingContext.bindValue(btnAdicionarProdutoObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoFornecedorObserveValue, null, null);
		//
		IObservableValue btnRemoverProdutoObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnRemoverProduto);
		bindingContext.bindValue(btnRemoverProdutoObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoFornecedorObserveValue, null, null);
		//
		IObservableValue btnAdicionarVeiculoObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnAdicionarVeiculo);
		bindingContext.bindValue(btnAdicionarVeiculoObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoClienteObserveValue, null, null);
		//
		IObservableValue btnRemoverVeiculoObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnDesativarVeiculo);
		bindingContext.bindValue(btnRemoverVeiculoObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoClienteObserveValue, null, null);
		//
		IObservableValue txtSerieObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtSerie);
		bindingContext.bindValue(txtSerieObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoFuncionarioObserveValue, null, null);
		//
		IObservableValue cbCargoObserveEnabledObserveWidget = SWTObservables.observeEnabled(cbCargo);
		bindingContext.bindValue(cbCargoObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoFuncionarioObserveValue, null, null);
		//
		IObservableValue txtCartTrabNumObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtCartTrabNum);
		bindingContext.bindValue(txtCartTrabNumObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoFuncionarioObserveValue, null, null);
		//
		IObservableValue txtSalarioObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtSalario);
		bindingContext.bindValue(txtSalarioObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoFuncionarioObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider_1.getKnownElements(), ForneceProduto.class, new String[]{"id.produto.descricao", "valorUnitario"});
		tvProduto.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvProduto.setContentProvider(listContentProvider_1);
		//
		IObservableList funcionarioServicegetFuncionarioListaProdutoObserveList = PojoObservables.observeList(Realm.getDefault(), service.getPessoa(), "listaProduto");
		tvProduto.setInput(funcionarioServicegetFuncionarioListaProdutoObserveList);
		//
		ObservableListContentProvider listContentProvider_2 = new ObservableListContentProvider();
		IObservableMap[] observeMaps_1 = PojoObservables.observeMaps(listContentProvider_2.getKnownElements(), Veiculo.class, new String[]{"modelo", "placa"});
		tvVeiculo.setLabelProvider(new ObservableMapLabelProvider(observeMaps_1));
		tvVeiculo.setContentProvider(listContentProvider_2);
		//
		IObservableList funcionarioServicegetFuncionarioListaVeiculoObserveList = PojoObservables.observeList(Realm.getDefault(), service.getPessoa(), "listaVeiculo");
		tvVeiculo.setInput(funcionarioServicegetFuncionarioListaVeiculoObserveList);
		//
		IObservableValue tableVeiculosObserveEnabledObserveWidget = SWTObservables.observeEnabled(tableVeiculos);
		bindingContext.bindValue(tableVeiculosObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoClienteObserveValue, null, null);
		//
		IObservableValue tableProdutosObserveEnabledObserveWidget = SWTObservables.observeEnabled(tableProdutos);
		bindingContext.bindValue(tableProdutosObserveEnabledObserveWidget, funcionarioServicegetFuncionarioTipoFornecedorObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = PojoObservables.observeMap(listContentProvider.getKnownElements(), TipoFuncionario.class, "nome");
		cvCargo.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvCargo.setContentProvider(listContentProvider);
		//
		WritableList writableList = new WritableList(tiposFuncionarios, TipoFuncionario.class);
		cvCargo.setInput(writableList);
		//
		IObservableValue cvCargoObserveSingleSelection = ViewersObservables.observeSingleSelection(cvCargo);
		IObservableValue funcionarioServicegetFuncionarioTipoObserveValue = PojoObservables.observeValue(service.getPessoa(), "tipo");
		bindingContext.bindValue(cvCargoObserveSingleSelection, funcionarioServicegetFuncionarioTipoObserveValue, null, null);
		//
		return bindingContext;
	}
}
