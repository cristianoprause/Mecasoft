package tela.editor;

import static aplicacao.helper.LayoutHelper.getActiveShell;

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
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
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
import tela.editor.editorInput.AbrirOrdemServicoEditorInput;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.PessoaService;
import aplicacao.service.ServicoPrestadoService;
import aplicacao.service.StatusService;
import banco.modelo.ItemServico;
import banco.modelo.Pessoa;
import banco.modelo.Status;
import banco.modelo.StatusServico;

public class AbrirOrdemServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.AbrirOrdemServicoEditor"; //$NON-NLS-1$
	private Text txtCliente;
	private Text txtVeiculo;
	private Table tableServicos;
	private Table tableItens;
	private Text txtMecanico;
	private Text txtStatusAtual;
	private Table tableStatus;
	private TableViewer tvServico;
	private TableViewer tvItens;
	
	private ServicoPrestadoService service = new ServicoPrestadoService();
	private List<Status> listaStatus;
	private ComboViewer cvNovoStatus;
	private TableViewer tvStatus;

	public AbrirOrdemServicoEditor() {
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
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(4, false));
		
		Label lblCliente = new Label(compositeConteudo, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(compositeConteudo, SWT.BORDER);
		txtCliente.setEnabled(false);
		txtCliente.setEditable(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Button btnSelecionarCliente = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarCliente.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pessoa p = selecionarCliente();
				if(p != null){
					service.getServicoPrestado().setCliente(p);
					txtCliente.setText(p.getNomeFantasia());
				}
			}
		});
		btnSelecionarCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarCliente.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarCliente.setText("Selecionar");
		
		Label lblVeculo = new Label(compositeConteudo, SWT.NONE);
		lblVeculo.setText("Ve\u00EDculo:");
		
		txtVeiculo = new Text(compositeConteudo, SWT.BORDER);
		txtVeiculo.setEnabled(false);
		txtVeiculo.setEditable(false);
		txtVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Button btnSelecionarVeiculo = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarVeiculo.setText("Selecionar");
		
		Label lblServicosPrestados = new Label(compositeConteudo, SWT.NONE);
		lblServicosPrestados.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblServicosPrestados.setText("Servi\u00E7os prestados:");
		
		tvServico = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableServicos = tvServico.getTable();
		tableServicos.setLinesVisible(true);
		tableServicos.setHeaderVisible(true);
		GridData gd_tableServicos = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2);
		gd_tableServicos.widthHint = 608;
		gd_tableServicos.heightHint = 95;
		tableServicos.setLayoutData(gd_tableServicos);
		
		TableViewerColumn tvcServico = new TableViewerColumn(tvServico, SWT.NONE);
		tvcServico.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getDescricao();
			}
		});
		TableColumn tblclmnServico = tvcServico.getColumn();
		tblclmnServico.setWidth(165);
		tblclmnServico.setText("Servi\u00E7o");
		
		TableViewerColumn tvcValorBase = new TableViewerColumn(tvServico, SWT.NONE);
		tvcValorBase.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getValorUnitario());
			}
		});
		TableColumn tblclmnValorBase = tvcValorBase.getColumn();
		tblclmnValorBase.setWidth(127);
		tblclmnValorBase.setText("Valor Base");
		
		TableViewerColumn tvcDesconto = new TableViewerColumn(tvServico, SWT.NONE);
		tvcDesconto.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getDesconto());
			}
		});
		TableColumn tblclmnDesconto = tvcDesconto.getColumn();
		tblclmnDesconto.setWidth(136);
		tblclmnDesconto.setText("Desconto");
		
		TableViewerColumn tvcAcrescimo = new TableViewerColumn(tvServico, SWT.NONE);
		tvcAcrescimo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getAcrescimo());
			}
		});
		TableColumn tblclmnAcrescimo = tvcAcrescimo.getColumn();
		tblclmnAcrescimo.setWidth(136);
		tblclmnAcrescimo.setText("Acrescimo");
		
		Button btnAdicionarServio = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarServio.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/plusServico16.png"));
		btnAdicionarServio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdicionarServio.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemoverServio = new Button(compositeConteudo, SWT.NONE);
		btnRemoverServio.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/lessServico16.png"));
		btnRemoverServio.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoverServio.setText("Remover");
		
		Label lblItensUtilizados = new Label(compositeConteudo, SWT.NONE);
		lblItensUtilizados.setText("Itens utilizados:");
		
		tvItens = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableItens = tvItens.getTable();
		tableItens.setLinesVisible(true);
		tableItens.setHeaderVisible(true);
		GridData gd_tableItens = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2);
		gd_tableItens.widthHint = 628;
		gd_tableItens.heightHint = 95;
		tableItens.setLayoutData(gd_tableItens);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvItens, SWT.NONE);
		tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getDescricao());
			}
		});
		TableColumn tblclmnDescricao = tvcDescricao.getColumn();
		tblclmnDescricao.setWidth(143);
		tblclmnDescricao.setText("Descri\u00E7\u00E3o");
		
		TableViewerColumn tvcQuantidade = new TableViewerColumn(tvItens, SWT.NONE);
		tvcQuantidade.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getQuantidade().toString();
			}
		});
		TableColumn tblclmnQuantidade = tvcQuantidade.getColumn();
		tblclmnQuantidade.setWidth(100);
		tblclmnQuantidade.setText("Quantidade");
		
		TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvItens, SWT.NONE);
		tvcValorUnitario.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getValorUnitario());
			}
		});
		TableColumn tblclmnValorUnitario = tvcValorUnitario.getColumn();
		tblclmnValorUnitario.setWidth(100);
		tblclmnValorUnitario.setText("Valor Unit\u00E1rio");
		
		TableViewerColumn tvcDescontoItens = new TableViewerColumn(tvItens, SWT.NONE);
		tvcDescontoItens.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getDesconto());
			}
		});
		TableColumn tblclmnDesconto_1 = tvcDescontoItens.getColumn();
		tblclmnDesconto_1.setWidth(100);
		tblclmnDesconto_1.setText("Desconto");
		
		TableViewerColumn tvcAcrescimoItens = new TableViewerColumn(tvItens, SWT.NONE);
		tvcAcrescimoItens.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getAcrescimo());
			}
		});
		TableColumn tblclmnAcrescimoProduto = tvcAcrescimoItens.getColumn();
		tblclmnAcrescimoProduto.setWidth(100);
		tblclmnAcrescimoProduto.setText("Acrescimo");
		
		TableViewerColumn tvcTotal = new TableViewerColumn(tvItens, SWT.NONE);
		tvcTotal.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getTotal());
			}
		});
		TableColumn tblclmnTotal = tvcTotal.getColumn();
		tblclmnTotal.setResizable(false);
		tblclmnTotal.setWidth(100);
		tblclmnTotal.setText("Total");
		
		Button btnAdicionarItem = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarItem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdicionarItem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productAdd16.png"));
		btnAdicionarItem.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemoverItem = new Button(compositeConteudo, SWT.NONE);
		btnRemoverItem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemoverItem.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoverItem.setText("Remover");
		
		Label lblMecanico = new Label(compositeConteudo, SWT.NONE);
		lblMecanico.setText("Mec\u00E2nico:");
		
		txtMecanico = new Text(compositeConteudo, SWT.BORDER);
		txtMecanico.setEnabled(false);
		txtMecanico.setEditable(false);
		txtMecanico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		if(service.getServicoPrestado().getListaStatus().size() > 0)
			txtMecanico.setText(service.getServicoPrestado().getListaStatus().get(service.getServicoPrestado().getListaStatus().size() - 1)
					.getFuncionario().getNomeFantasia());
		
		Button btnSelecionar = new Button(compositeConteudo, SWT.NONE);
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");
		
		Label lblNovoStatus = new Label(compositeConteudo, SWT.NONE);
		lblNovoStatus.setText("Novo status:");
		
		cvNovoStatus = new ComboViewer(compositeConteudo, SWT.READ_ONLY);
		Combo cbNovoStatus = cvNovoStatus.getCombo();
		GridData gd_cbNovoStatus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cbNovoStatus.widthHint = 142;
		cbNovoStatus.setLayoutData(gd_cbNovoStatus);
		
		Button btnAlterarStatus = new Button(compositeConteudo, SWT.NONE);
		btnAlterarStatus.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnAlterarStatus.setText("Alterar Status");
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblStatusAtual = new Label(compositeConteudo, SWT.NONE);
		lblStatusAtual.setText("Status atual:");
		
		txtStatusAtual = new Text(compositeConteudo, SWT.BORDER);
		txtStatusAtual.setEditable(false);
		txtStatusAtual.setEnabled(false);
		txtStatusAtual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		if(service.getServicoPrestado().getListaStatus().size() > 0)
			txtStatusAtual.setText(service.getServicoPrestado().getListaStatus().get(service.getServicoPrestado().getListaStatus().size()-1)
					.getStatus().getDescricao());
		
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblStatus = new Label(compositeConteudo, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblStatus.setText("Status:");
		
		tvStatus = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableStatus = tvStatus.getTable();
		tableStatus.setLinesVisible(true);
		tableStatus.setHeaderVisible(true);
		GridData gd_tableStatus = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd_tableStatus.widthHint = 593;
		gd_tableStatus.heightHint = 95;
		tableStatus.setLayoutData(gd_tableStatus);
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((StatusServico)element).getStatus().getDescricao();
			}
		});
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(184);
		tblclmnStatus.setText("Status");
		
		TableViewerColumn tvcData = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcData.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.DATEFORMAT.format(((StatusServico)element).getData());
			}
		});
		TableColumn tblclmnData = tvcData.getColumn();
		tblclmnData.setWidth(159);
		tblclmnData.setText("Data");
		
		TableViewerColumn tvcMecanico = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcMecanico.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((StatusServico)element).getFuncionario().getNomeFantasia();
			}
		});
		TableColumn tblclmnMecanico = tvcMecanico.getColumn();
		tblclmnMecanico.setWidth(299);
		tblclmnMecanico.setText("Mec\u00E2nico");
		new Label(compositeConteudo, SWT.NONE);
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		AbrirOrdemServicoEditorInput aosei = (AbrirOrdemServicoEditorInput)input;
		
		if(aosei.getServicoPrestado().getId() != null)
			service.setServicoPrestado(service.find(aosei.getServicoPrestado().getId()));
		else
			service.setServicoPrestado(aosei.getServicoPrestado());
		
		listaStatus = new StatusService().findAllAtivos();
		
		setSite(site);
		setInput(input);
	}
	
	private Pessoa selecionarCliente(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		
		sid.setElements(new PessoaService().findAllClientesAtivos().toArray());
		return (Pessoa) sid.getElementoSelecionado();
	}

	@Override
	public boolean isDirty() {
		return false;
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtClienteObserveTextObserveWidget = SWTObservables.observeText(txtCliente, SWT.Modify);
		IObservableValue servicegetServicoPrestadoClientenomeFantasiaObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "cliente.nomeFantasia");
		bindingContext.bindValue(txtClienteObserveTextObserveWidget, servicegetServicoPrestadoClientenomeFantasiaObserveValue, null, null);
		//
		IObservableValue txtVeiculoObserveTextObserveWidget = SWTObservables.observeText(txtVeiculo, SWT.Modify);
		IObservableValue servicegetServicoPrestadoVeiculomodeloObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "veiculo.modelo");
		bindingContext.bindValue(txtVeiculoObserveTextObserveWidget, servicegetServicoPrestadoVeiculomodeloObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), ItemServico.class, new String[]{"descricao", "valorUnitario", "desconto", "acrescimo"});
		tvServico.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvServico.setContentProvider(listContentProvider);
		//
		IObservableList servicegetServicoPrestadoListaServicosObserveList = PojoObservables.observeList(Realm.getDefault(), service.getServicoPrestado(), "listaServicos");
		tvServico.setInput(servicegetServicoPrestadoListaServicosObserveList);
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
		IObservableMap[] observeMaps_1 = PojoObservables.observeMaps(listContentProvider_1.getKnownElements(), ItemServico.class, new String[]{"descricao", "quantidade", "valorUnitario", "desconto", "acrescimo", "total"});
		tvItens.setLabelProvider(new ObservableMapLabelProvider(observeMaps_1));
		tvItens.setContentProvider(listContentProvider_1);
		//
		IObservableList servicegetServicoPrestadoListaProdutosObserveList = PojoObservables.observeList(Realm.getDefault(), service.getServicoPrestado(), "listaProdutos");
		tvItens.setInput(servicegetServicoPrestadoListaProdutosObserveList);
		//
		ObservableListContentProvider listContentProvider_2 = new ObservableListContentProvider();
		IObservableMap observeMap = PojoObservables.observeMap(listContentProvider_2.getKnownElements(), Status.class, "descricao");
		cvNovoStatus.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvNovoStatus.setContentProvider(listContentProvider_2);
		//
		WritableList writableList = new WritableList(listaStatus, Status.class);
		cvNovoStatus.setInput(writableList);
		//
		ObservableListContentProvider listContentProvider_3 = new ObservableListContentProvider();
		IObservableMap[] observeMaps_2 = PojoObservables.observeMaps(listContentProvider_3.getKnownElements(), StatusServico.class, new String[]{"status.descricao", "data", "funcionario.nomeFantasia"});
		tvStatus.setLabelProvider(new ObservableMapLabelProvider(observeMaps_2));
		tvStatus.setContentProvider(listContentProvider_3);
		//
		IObservableList servicegetServicoPrestadoListaStatusObserveList = PojoObservables.observeList(Realm.getDefault(), service.getServicoPrestado(), "listaStatus");
		tvStatus.setInput(servicegetServicoPrestadoListaStatusObserveList);
		//
		return bindingContext;
	}
}
