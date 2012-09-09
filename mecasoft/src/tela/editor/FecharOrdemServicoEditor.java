package tela.editor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.PadraoHelper;
import aplicacao.service.ServicoPrestadoService;
import banco.modelo.FormaPagtoUtilizada;

public class FecharOrdemServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.FecharOrdemServicoEditor"; //$NON-NLS-1$
	private Label lblValorTotal;
	private Table table;
	
	private ServicoPrestadoService service = new ServicoPrestadoService();
	private MecasoftText txtTotalServico;
	private MecasoftText txtTotalItem;
	private MecasoftText txtLocomocao;
	private MecasoftText txtMaoObra;
	private MecasoftText txtDesconto;
	private MecasoftText txtTroco;
	private TableViewer tableViewer;

	public FecharOrdemServicoEditor() {
	}

	@Override
	public void salvarRegistro() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(3, false));
		
		Label lblTotalServicos = new Label(compositeConteudo, SWT.NONE);
		lblTotalServicos.setText("Total servi\u00E7os:");
		
		txtTotalServico = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTotalServico.setEnabled(false);
		txtTotalServico.setOptions(MecasoftText.NUMEROS, -1);
		txtTotalServico.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtTotalServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblTotalItens = new Label(compositeConteudo, SWT.NONE);
		lblTotalItens.setText("Total itens:");
		
		txtTotalItem = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTotalItem.setEnabled(false);
		txtTotalItem.setOptions(MecasoftText.NUMEROS, -1);
		txtTotalItem.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtTotalItem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblLocomocao = new Label(compositeConteudo, SWT.NONE);
		lblLocomocao.setText("Locomo\u00E7\u00E3o:");
		
		txtLocomocao = new MecasoftText(compositeConteudo, SWT.NONE);
		txtLocomocao.setOptions(MecasoftText.NUMEROS, -1);
		txtLocomocao.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtLocomocao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblMaoDeObra = new Label(compositeConteudo, SWT.NONE);
		lblMaoDeObra.setText("M\u00E3o de obra:");
		
		txtMaoObra = new MecasoftText(compositeConteudo, SWT.NONE);
		txtMaoObra.setOptions(MecasoftText.NUMEROS, -1);
		txtMaoObra.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtMaoObra.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblDesconto = new Label(compositeConteudo, SWT.NONE);
		lblDesconto.setText("Desconto:");
		
		txtDesconto = new MecasoftText(compositeConteudo, SWT.NONE);
		txtDesconto.setOptions(MecasoftText.NUMEROS, -1);
		txtDesconto.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtDesconto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblTotal = new Label(compositeConteudo, SWT.NONE);
		lblTotal.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblTotal.setText("Total:");
		
		lblValorTotal = new Label(compositeConteudo, SWT.NONE);
		lblValorTotal.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblFormasDePagamento = new Label(compositeConteudo, SWT.NONE);
		lblFormasDePagamento.setText("Formas de pagamento:");
		
		tableViewer = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_table.heightHint = 95;
		table.setLayoutData(gd_table);
		
		TableViewerColumn tvcForma = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcForma.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((FormaPagtoUtilizada)element).getFormaPagamento().getNome();
			}
		});
		TableColumn tblclmnForma = tvcForma.getColumn();
		tblclmnForma.setWidth(310);
		tblclmnForma.setText("Forma");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((FormaPagtoUtilizada)element).getValor());
			}
		});
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(100);
		tblclmnValor.setText("Valor");
		
		Button btnAdicionar = new Button(compositeConteudo, SWT.NONE);
		btnAdicionar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnAdicionar.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemover = new Button(compositeConteudo, SWT.NONE);
		btnRemover.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		btnRemover.setText("Remover");
		
		Label lblTroco = new Label(compositeConteudo, SWT.NONE);
		lblTroco.setText("Troco:");
		
		txtTroco = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTroco.setEnabled(false);
		txtTroco.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		initDataBindings();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setShowExcluir(false);
		setShowSalvar(false);
		
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtTotalServicotextObserveTextObserveWidget = SWTObservables.observeText(txtTotalServico.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTotalServicoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "totalServico");
		bindingContext.bindValue(txtTotalServicotextObserveTextObserveWidget, servicegetServicoPrestadoTotalServicoObserveValue, null, null);
		//
		IObservableValue txtTotalItemtextObserveTextObserveWidget = SWTObservables.observeText(txtTotalItem.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTotalItensObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "totalItens");
		bindingContext.bindValue(txtTotalItemtextObserveTextObserveWidget, servicegetServicoPrestadoTotalItensObserveValue, null, null);
		//
		IObservableValue txtLocomocaotextObserveTextObserveWidget = SWTObservables.observeText(txtLocomocao.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTotalLocomocaoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "totalLocomocao");
		bindingContext.bindValue(txtLocomocaotextObserveTextObserveWidget, servicegetServicoPrestadoTotalLocomocaoObserveValue, null, null);
		//
		IObservableValue txtMaoObratextObserveTextObserveWidget = SWTObservables.observeText(txtMaoObra.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTotalMaoObraObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "totalMaoObra");
		bindingContext.bindValue(txtMaoObratextObserveTextObserveWidget, servicegetServicoPrestadoTotalMaoObraObserveValue, null, null);
		//
		IObservableValue txtDescontotextObserveTextObserveWidget = SWTObservables.observeText(txtDesconto.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoDescontoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "desconto");
		bindingContext.bindValue(txtDescontotextObserveTextObserveWidget, servicegetServicoPrestadoDescontoObserveValue, null, null);
		//
		IObservableValue lblValorTotalObserveTextObserveWidget = SWTObservables.observeText(lblValorTotal);
		IObservableValue servicegetServicoPrestadoValorTotalObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "valorTotal");
		bindingContext.bindValue(lblValorTotalObserveTextObserveWidget, servicegetServicoPrestadoValorTotalObserveValue, null, null);
		//
		IObservableValue txtTrocotextObserveTextObserveWidget = SWTObservables.observeText(txtTroco.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTrocoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "troco");
		bindingContext.bindValue(txtTrocotextObserveTextObserveWidget, servicegetServicoPrestadoTrocoObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
//		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), FormaPagtoUtilizada.class, new String[]{"formaPagamento.nome", "valor"});
//		tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tableViewer.setContentProvider(listContentProvider);
		//
		IObservableList servicegetServicoPrestadoListaFormaPagtoObserveList = PojoObservables.observeList(Realm.getDefault(), service.getServicoPrestado(), "listaFormaPagto");
		tableViewer.setInput(servicegetServicoPrestadoListaFormaPagtoObserveList);
		//
		return bindingContext;
	}
}
