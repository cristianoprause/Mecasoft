package tela.editor;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.ValidatorHelper.validar;

import java.math.BigDecimal;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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

import tela.componentes.MecasoftText;
import tela.dialog.SelecionarItemDialog;
import tela.editingSupport.ForneceProdutoEditingSupport;
import tela.editor.editorInput.ProdutoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.helper.LayoutHelper;
import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;
import banco.modelo.ForneceProduto;
import banco.modelo.Pessoa;
import banco.modelo.ProdutoServico;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.wb.swt.ResourceManager;

public class ProdutoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.ProdutoEditor"; //$NON-NLS-1$
	private Text txtDescricao;
	private Text txtQuantidade;
	private Text txtCusto;
	private Text txtValorUnitario;
	private Table tableFornecedores;
	
	private ProdutoServicoService service = new ProdutoServicoService();
	private MecasoftText txtLucro;
	private TableViewer tvFornecedores;
	private Button btnAtivo;

	public ProdutoEditor() {
	}

	@Override
	public void salvarRegistro() {
		try {
			validar(service.getProdutoServico());
			
			if(service.getProdutoServico().getListaFornecedores().size() == 0){
				setErroMessage("Selecione ao menos um fornecedor.");
				return;
			}
			
			for(ForneceProduto fp : service.getProdutoServico().getListaFornecedores()){
				validar(fp);
			}
			
			service.saveOrUpdate();
			openInformation("Produto cadastrado com sucesso!");
			closeThisEditor();
		} catch (ValidationException e) {
			setErroMessage(e.getMessage());
		}
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(3, false));
		
		Label lblDescrio = new Label(compositeConteudo, SWT.NONE);
		lblDescrio.setText("Descrição:");
		
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
				initDataBindings();
			}
		});
		txtLucro.text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				calcularValores();
			}
		});
		txtLucro.setOptions(MecasoftText.NUMEROS, -1);
		txtLucro.addChars(",", new Integer[]{-2});
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
		
		Label lblFornecedores = new Label(compositeConteudo, SWT.NONE);
		lblFornecedores.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblFornecedores.setText("Fornecedores:");
		
		tvFornecedores = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableFornecedores = tvFornecedores.getTable();
		tableFornecedores.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.CR){
					calcularValores();
					initDataBindings();
				}
			}
		});
		tableFornecedores.setLinesVisible(true);
		tableFornecedores.setHeaderVisible(true);
		GridData gd_tableFornecedores = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 2);
		gd_tableFornecedores.heightHint = 95;
		tableFornecedores.setLayoutData(gd_tableFornecedores);
		
		TableViewerColumn tvcNome = new TableViewerColumn(tvFornecedores, SWT.NONE);
		TableColumn tblclmnNome = tvcNome.getColumn();
		tblclmnNome.setWidth(274);
		tblclmnNome.setText("Nome");
		
		TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvFornecedores, SWT.NONE);
		tvcValorUnitario.setEditingSupport(new ForneceProdutoEditingSupport(tvFornecedores));
		TableColumn tblclmnValorUnitario = tvcValorUnitario.getColumn();
		tblclmnValorUnitario.setWidth(100);
		tblclmnValorUnitario.setText("Valor Unitário");
		
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
					calcularValores();
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
		sid.setElements(new PessoaService().findAllFornecedoresAtivos().toArray());
		
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
		
//		initDataBindings();
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
		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), ForneceProduto.class, new String[]{"id.pessoa.nomeFantasia", "valorUnitario"});
		tvFornecedores.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvFornecedores.setContentProvider(listContentProvider);
		//
		IObservableList servicegetProdutoServicoListaFornecedoresObserveList = PojoObservables.observeList(Realm.getDefault(), service.getProdutoServico(), "listaFornecedores");
		tvFornecedores.setInput(servicegetProdutoServicoListaFornecedoresObserveList);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue servicegetProdutoServicoAtivoObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, servicegetProdutoServicoAtivoObserveValue, null, null);
		//
		return bindingContext;
	}
}
