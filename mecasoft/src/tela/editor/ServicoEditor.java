package tela.editor;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.ValidatorHelper.validar;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;

import tela.dialog.SelecionarItemDialog;
import tela.editingSupport.ForneceProdutoEditingSupport;
import tela.editor.editorInput.ServicoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.LayoutHelper;
import aplicacao.service.MecasoftService;
import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;
import banco.modelo.ForneceProduto;
import banco.modelo.Pessoa;
import banco.modelo.ProdutoServico;

public class ServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.ServicoEditor"; //$NON-NLS-1$

	private ProdutoServicoService service = new ProdutoServicoService();
	private PessoaService pessoaService = new PessoaService();
	
	private Text txtDescricao;
	private Table tableProduto;
	private TableViewer tvProdutos;
	private Button btnAtivo;
	private Text txtMediaServico;
	private Table tablePrestador;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private TableViewer tvPrestador;

	public ServicoEditor() {
	}

	@Override
	public void salvarRegistro() throws ValidationException{
		validar(service.getProdutoServico());
			
		service.saveOrUpdate();
		openInformation("Serviço cadastrado com sucesso!");
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(4, false));
		
		Label lblDescricao = new Label(compositeConteudo, SWT.NONE);
		lblDescricao.setText("Descri\u00E7\u00E3o:");
		
		txtDescricao = new Text(compositeConteudo, SWT.BORDER);
		txtDescricao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblMediaValor = new Label(compositeConteudo, SWT.NONE);
		lblMediaValor.setText("M\u00E9dia Val. Servi\u00E7o");
		
		txtMediaServico = new Text(compositeConteudo, SWT.BORDER);
		txtMediaServico.setEnabled(false);
		txtMediaServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblPrestadoresDoServico = new Label(compositeConteudo, SWT.NONE);
		lblPrestadoresDoServico.setText("Prestadores do servico");
		
		tvPrestador = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tablePrestador = tvPrestador.getTable();
		tablePrestador.setLinesVisible(true);
		tablePrestador.setHeaderVisible(true);
		GridData gd_tablePrestador = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_tablePrestador.heightHint = 95;
		tablePrestador.setLayoutData(gd_tablePrestador);
		
		TableViewerColumn tvcNome = new TableViewerColumn(tvPrestador, SWT.NONE);
		tvcNome.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ForneceProduto)element).getPessoa().getNome();
			}
		});
		TableColumn tblclmnNome = tvcNome.getColumn();
		tblclmnNome.setWidth(222);
		tblclmnNome.setText("Nome");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tvPrestador, SWT.NONE);
		tvcValor.setEditingSupport(new ForneceProdutoEditingSupport(tvPrestador){
			@Override
			protected void setValue(Object element, Object value) {
				super.setValue(element, value);
				initDataBindings();
			}
		});
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				try{
					return FormatterHelper.getDecimalFormat().format(((ForneceProduto)element).getValorUnitario());
				}catch (Exception e) {
					return "";
				}
			}
		});
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(100);
		tblclmnValor.setText("Valor");
		
		Button btnAdicionarPrestador = formToolkit.createButton(compositeConteudo, "Adicionar", SWT.NONE);
		btnAdicionarPrestador.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pessoa prestador = selecionarPrestador();
				if(prestador != null){
					ForneceProduto fp = new ForneceProduto();
					fp.setPessoa(prestador);
					fp.setProduto(service.getProdutoServico());
					
					service.getProdutoServico().getListaFornecedores().add(fp);
					calcularMedia();
					
					tvPrestador.refresh();
					initDataBindings();
				}
			}
		});
		btnAdicionarPrestador.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/fornecedor/removeFornecedor16.png"));
		btnAdicionarPrestador.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemoverPrestador = formToolkit.createButton(compositeConteudo, "Remover", SWT.NONE);
		btnRemoverPrestador.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvPrestador.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(openQuestion("Deseja realmente remover este prestador da lista?")){
					ForneceProduto fp = (ForneceProduto) selecao.getFirstElement();
					
					service.getProdutoServico().getListaFornecedores().remove(fp);
					fp.getPessoa().getListaProduto().remove(fp);
					calcularMedia();
					
					tvPrestador.refresh();
				}
			}
		});
		btnRemoverPrestador.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/fornecedor/addFornecedor16.png"));
		btnRemoverPrestador.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		
		Label lblProdutosParametrizados = new Label(compositeConteudo, SWT.NONE);
		lblProdutosParametrizados.setText("Produtos parametrizados:");
		
		tvProdutos = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableProduto = tvProdutos.getTable();
		tableProduto.setLinesVisible(true);
		tableProduto.setHeaderVisible(true);
		GridData gd_tableProduto = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 2);
		gd_tableProduto.heightHint = 95;
		tableProduto.setLayoutData(gd_tableProduto);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvProdutos, SWT.NONE);
		tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ProdutoServico)element).getDescricao();
			}
		});
		TableColumn tblclmnDescricao = tvcDescricao.getColumn();
		tblclmnDescricao.setWidth(221);
		tblclmnDescricao.setText("Descri\u00E7\u00E3o");
		
		TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvProdutos, SWT.NONE);
		tvcValorUnitario.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ProdutoServico)element).getValorUnitario());
			}
		});
		TableColumn tblclmnValorUnitrio = tvcValorUnitario.getColumn();
		tblclmnValorUnitrio.setWidth(100);
		tblclmnValorUnitrio.setText("Valor unit\u00E1rio");
		
		Button btnAdicionarProduto = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productAdd16.png"));
		btnAdicionarProduto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProdutoServico ps = selecionarProduto();
				if(ps != null){
					if(service.getProdutoServico().getListaProduto().contains(ps)){
						setErroMessage("O produto selecionado já esta parametrizado neste serviço.");
						return;
					}
					
					service.getProdutoServico().getListaProduto().add(ps);
					tvProdutos.refresh();
				}
			}
		});
		btnAdicionarProduto.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnAdicionarProduto.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemoverProduto = new Button(compositeConteudo, SWT.NONE);
		btnRemoverProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemoverProduto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvProdutos.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(openQuestion("Deseja realmente remover este produto do serviço?")){
					ProdutoServico ps = (ProdutoServico)selecao.getFirstElement();
					service.getProdutoServico().getListaProduto().remove(ps);
					tvProdutos.refresh();
				}
			}
		});
		btnRemoverProduto.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		btnRemoverProduto.setText("Remover");
		
		btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setText("Ativo");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		ServicoEditorInput sei = (ServicoEditorInput)input;
		
		if(sei.getProdutoServico().getId() != null){
			service.setProdutoServico(service.find(sei.getProdutoServico().getId()));
			this.setPartName("Serviço: " + service.getProdutoServico().getDescricao());
		}else{
			service.setProdutoServico(sei.getProdutoServico());
			service.getProdutoServico().setTipo(ProdutoServico.TIPOSERVICO);
		}
		
		setShowExcluir(false);
		
		setSite(site);
		setInput(input);
	}
	
	public ProdutoServico selecionarProduto(){
		SelecionarItemDialog sid = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ProdutoServico)element).getDescricao();
			}
		});
		sid.setElements(service.findAllProdutos().toArray());
		
		return (ProdutoServico) sid.getElementoSelecionado();
	}
	
	public Pessoa selecionarPrestador(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNome();
			}
		});
		
		List<Pessoa> listaPrestador = pessoaService.findAllFornecedoresAtivos();
		
		//removendo os fornecedores ja adicionados
		for(ForneceProduto fp : service.getProdutoServico().getListaFornecedores()){
			listaPrestador.remove(fp.getPessoa());
		}
		
		sid.setElements(listaPrestador.toArray());
		
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	private void calcularMedia(){
		BigDecimal media = BigDecimal.ZERO;
		
		for(ForneceProduto fp : service.getProdutoServico().getListaFornecedores()){
			media = media.add(fp.getValorUnitario() == null ? BigDecimal.ZERO : fp.getValorUnitario());
		}
		
		if(service.getProdutoServico().getListaFornecedores().size() > 0)
			media = media.divide(new BigDecimal(service.getProdutoServico().getListaFornecedores().size()));
		
		service.getProdutoServico().setValorUnitario(media);
	}

	@Override
	public boolean isDirty() {
		return service.isDirty();
	}

	@Override
	public void setFocus() {
		initDataBindings();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtDescricaoObserveTextObserveWidget = SWTObservables.observeText(txtDescricao, SWT.Modify);
		IObservableValue servicegetProdutoServicoDescricaoObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "descricao");
		bindingContext.bindValue(txtDescricaoObserveTextObserveWidget, servicegetProdutoServicoDescricaoObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
//		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), ProdutoServico.class, new String[]{"descricao", "valorUnitario"});
//		tvProdutos.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvProdutos.setContentProvider(listContentProvider);
		//
		IObservableList servicegetProdutoServicoListaProdutoObserveList = PojoObservables.observeList(Realm.getDefault(), service.getProdutoServico(), "listaProduto");
		tvProdutos.setInput(servicegetProdutoServicoListaProdutoObserveList);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue servicegetProdutoServicoAtivoObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, servicegetProdutoServicoAtivoObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
//		IObservableMap[] observeMaps_1 = PojoObservables.observeMaps(listContentProvider_1.getKnownElements(), ForneceProduto.class, new String[]{"id.pessoa.nome", "valorUnitario"});
//		tvPrestador.setLabelProvider(new ObservableMapLabelProvider(observeMaps_1));
		tvPrestador.setContentProvider(listContentProvider_1);
		//
		IObservableList listaFornecedoresServicegetProdutoServicoObserveList = PojoProperties.list("listaFornecedores").observe(service.getProdutoServico());
		tvPrestador.setInput(listaFornecedoresServicegetProdutoServicoObserveList);
		//
		IObservableValue observeTextTxtMediaServicoObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtMediaServico);
		IObservableValue valorUnitarioServicegetProdutoServicoObserveValue = PojoProperties.value("valorUnitario").observe(service.getProdutoServico());
		bindingContext.bindValue(observeTextTxtMediaServicoObserveWidget, valorUnitarioServicegetProdutoServicoObserveValue, null, null);
		//
		return bindingContext;
	}
	
	@Override
	public MecasoftService<?> getService() {
		return service;
	}
}
