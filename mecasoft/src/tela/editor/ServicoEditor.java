package tela.editor;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.ValidatorHelper.validar;

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
import tela.editor.editorInput.ServicoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.helper.LayoutHelper;
import aplicacao.service.ProdutoServicoService;
import banco.modelo.ProdutoServico;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.ResourceManager;

public class ServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.ServicoEditor"; //$NON-NLS-1$
	private Text txtDescricao;
	private Table tableProdutos;
	
	private ProdutoServicoService service = new ProdutoServicoService();
	private MecasoftText txtValorBase;
	private TableViewer tvProdutos;
	private Button btnAtivo;

	public ServicoEditor() {
	}

	@Override
	public void salvarRegistro() {
		try {
			validar(service.getProdutoServico());
			
			service.saveOrUpdate();
			openInformation("Serviço cadastrado com sucesso!");
			closeThisEditor();
		} catch (ValidationException e) {
			setErroMessage(e.getMessage());
		}
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
		
		Label lblValorBase = new Label(compositeConteudo, SWT.NONE);
		lblValorBase.setText("Valor base:");
		
		txtValorBase = new MecasoftText(compositeConteudo, SWT.NONE);
		txtValorBase.setOptions(MecasoftText.NUMEROS, -1);
		txtValorBase.addChars(",", new Integer[]{-2});
		((GridData) txtValorBase.text.getLayoutData()).widthHint = 203;
		txtValorBase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnAtivo.setText("Ativo");
		
		Label lblProdutosParametrizados = new Label(compositeConteudo, SWT.NONE);
		lblProdutosParametrizados.setText("Produtos parametrizados:");
		
		tvProdutos = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableProdutos = tvProdutos.getTable();
		tableProdutos.setLinesVisible(true);
		tableProdutos.setHeaderVisible(true);
		GridData gd_tableProdutos = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 2);
		gd_tableProdutos.heightHint = 95;
		tableProdutos.setLayoutData(gd_tableProdutos);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvProdutos, SWT.NONE);
		TableColumn tblclmnNome = tvcDescricao.getColumn();
		tblclmnNome.setWidth(163);
		tblclmnNome.setText("Descri\u00E7\u00E3o");
		
		TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvProdutos, SWT.NONE);
		TableColumn tblclmnValorUnitrio = tvcValorUnitario.getColumn();
		tblclmnValorUnitrio.setWidth(100);
		tblclmnValorUnitrio.setText("Valor unit\u00E1rio");
		
		Button btnAdicionar = new Button(compositeConteudo, SWT.NONE);
		btnAdicionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productAdd16.png"));
		btnAdicionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProdutoServico ps = selecionarProduto();
				if(ps != null){
					service.getProdutoServico().getListaProduto().add(ps);
					tvProdutos.refresh();
				}
			}
		});
		btnAdicionar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnAdicionar.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemover = new Button(compositeConteudo, SWT.NONE);
		btnRemover.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemover.addSelectionListener(new SelectionAdapter() {
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
		btnRemover.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		btnRemover.setText("Remover");
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		ServicoEditorInput sei = (ServicoEditorInput)input;
		
		if(sei.getProdutoServico().getId() != null)
			service.setProdutoServico(service.find(sei.getProdutoServico().getId()));
		else{
			service.setProdutoServico(sei.getProdutoServico());
			service.getProdutoServico().setTipo(ProdutoServico.TIPOPRODUTO);
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
		sid.setElements(service.findAllProdutosAndStatus().toArray());
		
		return (ProdutoServico) sid.getElementoSelecionado();
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
		IObservableValue txtValorBasetextObserveTextObserveWidget = SWTObservables.observeText(txtValorBase.text, SWT.Modify);
		IObservableValue servicegetProdutoServicoValorUnitarioObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "valorUnitario");
		bindingContext.bindValue(txtValorBasetextObserveTextObserveWidget, servicegetProdutoServicoValorUnitarioObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), ProdutoServico.class, new String[]{"descricao", "valorUnitario"});
		tvProdutos.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvProdutos.setContentProvider(listContentProvider);
		//
		IObservableList servicegetProdutoServicoListaProdutoObserveList = PojoObservables.observeList(Realm.getDefault(), service.getProdutoServico(), "listaProduto");
		tvProdutos.setInput(servicegetProdutoServicoListaProdutoObserveList);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue servicegetProdutoServicoAtivoObserveValue = PojoObservables.observeValue(service.getProdutoServico(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, servicegetProdutoServicoAtivoObserveValue, null, null);
		//
		return bindingContext;
	}
}
