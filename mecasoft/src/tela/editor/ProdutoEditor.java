package tela.editor;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.ValidatorHelper.validar;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.ResourceManager;

import tela.componentes.MecasoftText;
import tela.dialog.SelecionarItemDialog;
import tela.editingSupport.ForneceProdutoEditingSupport;
import tela.editor.editorInput.ProdutoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.LayoutHelper;
import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;
import banco.connection.HibernateConnection;
import banco.modelo.ForneceProduto;
import banco.modelo.Pessoa;
import banco.modelo.ProdutoServico;

public class ProdutoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.ProdutoEditor"; //$NON-NLS-1$
	private Text txtDescricao;
	private Text txtQuantidade;
	private Text txtCusto;
	private Text txtValorUnitario;
	private Table tableFornecedores;
	private MecasoftText txtLucro;
	private TableViewer tvFornecedores;
	private Button btnAtivo;
	private Button btnEstocavel;
	
	private ProdutoServicoService service = new ProdutoServicoService();
	private PessoaService pessoaService = new PessoaService();

	public ProdutoEditor() {
	}

	@Override
	public void salvarRegistro() throws ValidationException{
		validar(service.getProdutoServico());
			
		if(service.getProdutoServico().getListaFornecedores().size() == 0 && service.getProdutoServico().getAtivo())
			throw new ValidationException("Para o cadastro do Produto selecione um fornecedor, caso n�o possua fornecedor, o Produto devera se inativado.");
			
		for(ForneceProduto fp : service.getProdutoServico().getListaFornecedores()){
			validar(fp);
		}
			
		service.saveOrUpdate();
		openInformation("Produto cadastrado com sucesso!");
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(3, false));
		
		Label lblDescrio = new Label(compositeConteudo, SWT.NONE);
		lblDescrio.setText("Descri��o:");
		
		txtDescricao = new Text(compositeConteudo, SWT.BORDER);
		txtDescricao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setText("Ativo");
		
		Label lblQuantidade = new Label(compositeConteudo, SWT.NONE);
		lblQuantidade.setText("Quantidade:");
		
		txtQuantidade = new Text(compositeConteudo, SWT.BORDER);
		txtQuantidade.setEnabled(false);
		txtQuantidade.setEditable(false);
		txtQuantidade.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblCusto = new Label(compositeConteudo, SWT.NONE);
		lblCusto.setText("Custo:");
		
		txtCusto = new Text(compositeConteudo, SWT.BORDER);
		txtCusto.setEnabled(false);
		txtCusto.setEditable(false);
		txtCusto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblLucro = new Label(compositeConteudo, SWT.NONE);
		lblLucro.setText("Lucro:");
		
		txtLucro = new MecasoftText(compositeConteudo, SWT.NONE);
		txtLucro.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularValores();
				initDataBindings();
			}
		});
		txtLucro.setOptions(MecasoftText.NUMEROS, -1);
		txtLucro.addChars(",", new Integer[]{-2}, null, null);
		txtLucro.setEditable(true);
		txtLucro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblValorUnitrio = new Label(compositeConteudo, SWT.NONE);
		lblValorUnitrio.setText("Valor unit\u00E1rio:");
		
		txtValorUnitario = new Text(compositeConteudo, SWT.BORDER);
		txtValorUnitario.setEnabled(false);
		txtValorUnitario.setEditable(false);
		txtValorUnitario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		btnEstocavel = new Button(compositeConteudo, SWT.CHECK);
		btnEstocavel.setEnabled(false);
		btnEstocavel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnEstocavel.setText("Necess\u00E1rio ter em estoque");
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblFornecedores = new Label(compositeConteudo, SWT.NONE);
		lblFornecedores.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblFornecedores.setText("Fornecedores:");
		
		tvFornecedores = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableFornecedores = tvFornecedores.getTable();
		tableFornecedores.setLinesVisible(true);
		tableFornecedores.setHeaderVisible(true);
		GridData gd_tableFornecedores = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 2);
		gd_tableFornecedores.heightHint = 95;
		tableFornecedores.setLayoutData(gd_tableFornecedores);
		
		TableViewerColumn tvcNome = new TableViewerColumn(tvFornecedores, SWT.NONE);
		tvcNome.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ForneceProduto)element).getId().getPessoa().getNomeFantasia();
			}
		});
		TableColumn tblclmnNome = tvcNome.getColumn();
		tblclmnNome.setWidth(274);
		tblclmnNome.setText("Nome");
		
		TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvFornecedores, SWT.NONE);
		
		ForneceProdutoEditingSupport fpes = new ForneceProdutoEditingSupport(tvFornecedores){
			@Override
			protected void setValue(Object element, Object value) {
				super.setValue(element, value);
				initDataBindings();
			}
		};
		
		tvcValorUnitario.setEditingSupport(fpes);
		tvcValorUnitario.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				try{
					return FormatterHelper.getDecimalFormat().format(((ForneceProduto)element).getValorUnitario());
				}catch(Exception e){
					return "";
				}
			}
		});
		TableColumn tblclmnValorUnitario = tvcValorUnitario.getColumn();
		tblclmnValorUnitario.setWidth(100);
		tblclmnValorUnitario.setText("Valor Unit�rio");
		
		Button btnAdicionar = new Button(compositeConteudo, SWT.NONE);
		btnAdicionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/fornecedor/removeFornecedor16.png"));
		btnAdicionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pessoa p = selecionarFornecedor();
				if(p != null){
					ForneceProduto fp = new ForneceProduto();
					fp.getId().setPessoa(p);
					fp.getId().setProduto(service.getProdutoServico());
					service.getProdutoServico().getListaFornecedores().add(fp);
					tvFornecedores.refresh();
				}
			}
		});
		btnAdicionar.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemover = new Button(compositeConteudo, SWT.NONE);
		btnRemover.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/fornecedor/addFornecedor16.png"));
		btnRemover.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection) tvFornecedores.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(openQuestion("Deseja realmente remover este fornecedor da lista?")){
					service.getProdutoServico().getListaFornecedores().remove((ForneceProduto)selecao.getFirstElement());					
					tvFornecedores.refresh();
//					calcularValores();
					initDataBindings();
				}
			}
		});
		btnRemover.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemover.setText("Remover");
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		ProdutoEditorInput pei = (ProdutoEditorInput) input;
		
		setShowExcluir(false);
		
		if(pei.getProdutoServico().getId() != null){
			service.setProdutoServico(service.find(pei.getProdutoServico().getId()));
			this.setPartName("Produto: " + service.getProdutoServico().getDescricao());
		}else{
			service.setProdutoServico(pei.getProdutoServico());
			service.getProdutoServico().setTipo(ProdutoServico.TIPOPRODUTO);
		}
		
		setInput(input);
		setSite(site);
		
	}
	
	private Pessoa selecionarFornecedor(){
		SelecionarItemDialog sid = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		
		//remover fornecedores ja adicionados
		List<Pessoa> fornecedores = pessoaService.findAllFornecedoresAtivos();
		
		for(ForneceProduto fp : service.getProdutoServico().getListaFornecedores()){
			if(fornecedores.contains(fp.getId().getPessoa()))
				fornecedores.remove(fp.getId().getPessoa());
		}
		
		sid.setElements(fornecedores.toArray());
		
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	private void calcularValores(){
		BigDecimal media = BigDecimal.ZERO;
		for(ForneceProduto fp : service.getProdutoServico().getListaFornecedores()){
			if(fp.getValorUnitario() != null){
				media = media.add(fp.getValorUnitario());
			}
		}
		
		if(service.getProdutoServico().getListaFornecedores().size() != 0)
			media = media.divide(new BigDecimal(service.getProdutoServico().getListaFornecedores().size()));
		else
			media = BigDecimal.ZERO;
		
		service.getProdutoServico().setCusto(media);
		service.getProdutoServico().setValorUnitario(media.add(
				service.getProdutoServico().getLucro() == null ? BigDecimal.ZERO : service.getProdutoServico().getLucro()));
		
	}

	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtDescricaoObserveTextObserveWidget = SWTObservables.observeText(txtDescricao, SWT.Modify);
		IObservableValue servicegetProdutoServicoDescricaoObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "descricao");
		bindingContext.bindValue(txtDescricaoObserveTextObserveWidget, servicegetProdutoServicoDescricaoObserveValue, null, null);
		//
		IObservableValue txtQuantidadeObserveTextObserveWidget = SWTObservables.observeText(txtQuantidade, SWT.Modify);
		IObservableValue servicegetProdutoServicoQuantidadeObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "quantidade");
		bindingContext.bindValue(txtQuantidadeObserveTextObserveWidget, servicegetProdutoServicoQuantidadeObserveValue, null, null);
		//
		IObservableValue txtCustoObserveTextObserveWidget = SWTObservables.observeText(txtCusto, SWT.Modify);
		IObservableValue servicegetProdutoServicoCustoObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "custo");
		bindingContext.bindValue(txtCustoObserveTextObserveWidget, servicegetProdutoServicoCustoObserveValue, null, null);
		//
		IObservableValue txtLucrotextObserveTextObserveWidget = SWTObservables.observeText(txtLucro.text, SWT.Modify);
		IObservableValue servicegetProdutoServicoLucroObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "lucro");
		bindingContext.bindValue(txtLucrotextObserveTextObserveWidget, servicegetProdutoServicoLucroObserveValue, null, null);
		//
		IObservableValue txtValorUnitarioObserveTextObserveWidget = SWTObservables.observeText(txtValorUnitario, SWT.Modify);
		IObservableValue servicegetProdutoServicoValorUnitarioObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "valorUnitario");
		bindingContext.bindValue(txtValorUnitarioObserveTextObserveWidget, servicegetProdutoServicoValorUnitarioObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
//		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), ForneceProduto.class, new String[]{"id.pessoa.nomeFantasia", "valorUnitario"});
//		tvFornecedores.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvFornecedores.setContentProvider(listContentProvider);
		//
		IObservableList servicegetProdutoServicoListaFornecedoresObserveList = PojoObservables.observeList(Realm.getDefault(), service.getProdutoServico(), "listaFornecedores");
		tvFornecedores.setInput(servicegetProdutoServicoListaFornecedoresObserveList);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue servicegetProdutoServicoAtivoObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, servicegetProdutoServicoAtivoObserveValue, null, null);
		//
		IObservableValue btnEstocavelObserveSelectionObserveWidget = SWTObservables.observeSelection(btnEstocavel);
		IObservableValue servicegetProdutoServicoEstocavelObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "estocavel");
		bindingContext.bindValue(btnEstocavelObserveSelectionObserveWidget, servicegetProdutoServicoEstocavelObserveValue, null, null);
		//
		return bindingContext;
	}

	@Override
	public void setFocus() {
		if(!HibernateConnection.isSessionRefresh(service.getProdutoServico()) && service.getProdutoServico().getId() != null)
			service.setProdutoServico(service.find(service.getProdutoServico().getId()));
		
		initDataBindings();
	}
}
