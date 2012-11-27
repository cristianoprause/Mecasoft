package tela.view;

import static aplicacao.helper.MessageHelper.openError;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import banco.modelo.DuplicataPaga;

import tela.componentes.MecasoftText;
import tela.filter.DuplicataPagaFilter;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.DuplicataPagaService;
import org.eclipse.jface.action.Action;
import org.eclipse.wb.swt.ResourceManager;

public class DuplicataPagaView extends ViewPart {

	public static final String ID = "tela.view.DuplicataPagaView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private TableViewer tvDuplicataPaga;
	
	private DuplicataPagaService service = new DuplicataPagaService();
	private DuplicataPagaFilter filtro = new DuplicataPagaFilter();
	private MecasoftText txtDataInicial;
	private MecasoftText txtDataFinal;
	private Action actionBuscarTodos;
	private Action actionBuscarPeriodo;

	public DuplicataPagaView() {
		createActions();
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmListaDeDuplicatas = formToolkit.createForm(container);
		frmListaDeDuplicatas.setImage(ResourceManager.getPluginImage("mecasoft", "assents/duplicata/duplicataPaga32.png"));
		formToolkit.paintBordersFor(frmListaDeDuplicatas);
		frmListaDeDuplicatas.setText("Lista de duplicatas pagas");
		frmListaDeDuplicatas.getBody().setLayout(new GridLayout(4, false));
		
		Label lblBuscar = new Label(frmListaDeDuplicatas.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDeDuplicatas.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvDuplicataPaga.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		Label lblPeriodoDe = new Label(frmListaDeDuplicatas.getBody(), SWT.NONE);
		formToolkit.adapt(lblPeriodoDe, true, true);
		lblPeriodoDe.setText("Per\u00EDodo de");
		
		txtDataInicial = new MecasoftText(frmListaDeDuplicatas.getBody(), SWT.NONE);
		txtDataInicial.setOptions(MecasoftText.NUMEROS, 10);
		txtDataInicial.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(txtDataInicial);
		formToolkit.paintBordersFor(txtDataInicial);
		
		Calendar dtInicial = Calendar.getInstance();
		dtInicial.add(Calendar.MONTH, -1);
		txtDataInicial.setText(FormatterHelper.getDateFormatData().format(dtInicial.getTime()));
		
		Label lblAt = new Label(frmListaDeDuplicatas.getBody(), SWT.NONE);
		formToolkit.adapt(lblAt, true, true);
		lblAt.setText("at\u00E9");
		
		txtDataFinal = new MecasoftText(frmListaDeDuplicatas.getBody(), SWT.NONE);
		txtDataFinal.setOptions(MecasoftText.NUMEROS, 10);
		txtDataFinal.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		formToolkit.adapt(txtDataFinal);
		formToolkit.paintBordersFor(txtDataFinal);
		
		Calendar dtFinal = Calendar.getInstance();
		txtDataFinal.setText(FormatterHelper.getDateFormatData().format(dtFinal.getTime()));
		
		tvDuplicataPaga = new TableViewer(frmListaDeDuplicatas.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		table = tvDuplicataPaga.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		formToolkit.paintBordersFor(table);
		tvDuplicataPaga.setContentProvider(ArrayContentProvider.getInstance());
		tvDuplicataPaga.addFilter(filtro);
		
		TableViewerColumn tvcNumero = new TableViewerColumn(tvDuplicataPaga, SWT.NONE);
		tvcNumero.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((DuplicataPaga)element).getDuplicata().getNumero();
			}
		});
		TableColumn tblclmnNmero = tvcNumero.getColumn();
		tblclmnNmero.setWidth(100);
		tblclmnNmero.setText("N\u00FAmero");
		
		TableViewerColumn tvcCliente = new TableViewerColumn(tvDuplicataPaga, SWT.NONE);
		tvcCliente.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((DuplicataPaga)element).getDuplicata().getServicoPrestado().getCliente().getNome();
			}
		});
		TableColumn tblclmnCliente = tvcCliente.getColumn();
		tblclmnCliente.setWidth(227);
		tblclmnCliente.setText("Cliente");
		
		TableViewerColumn tvcDataPagamento = new TableViewerColumn(tvDuplicataPaga, SWT.NONE);
		tvcDataPagamento.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDateFormatData().format(((DuplicataPaga)element).getDataPagamento());
			}
		});
		TableColumn tblclmnDataPagamento = tvcDataPagamento.getColumn();
		tblclmnDataPagamento.setWidth(100);
		tblclmnDataPagamento.setText("Data pagamento");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tvDuplicataPaga, SWT.NONE);
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((DuplicataPaga)element).getDuplicata().getValor());
			}
		});
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(100);
		tblclmnValor.setText("Valor");
		
		TableViewerColumn tvcDesconto = new TableViewerColumn(tvDuplicataPaga, SWT.NONE);
		tvcDesconto.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				DuplicataPaga d = (DuplicataPaga)element;
				
				return FormatterHelper.getDecimalFormat().format(d.getValorDesconto() == null ? BigDecimal.ZERO : d.getValorDesconto());
			}
		});
		TableColumn tblclmnDesconto = tvcDesconto.getColumn();
		tblclmnDesconto.setWidth(100);
		tblclmnDesconto.setText("Desconto");
		
		TableViewerColumn tvcTotal = new TableViewerColumn(tvDuplicataPaga, SWT.NONE);
		tvcTotal.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((DuplicataPaga)element).getValorTotal());
			}
		});
		TableColumn tblclmnTotal = tvcTotal.getColumn();
		tblclmnTotal.setWidth(100);
		tblclmnTotal.setText("Total");
		frmListaDeDuplicatas.getToolBarManager().add(actionBuscarTodos);
		frmListaDeDuplicatas.getToolBarManager().add(actionBuscarPeriodo);
		frmListaDeDuplicatas.updateToolBar();

	}

	private void createActions() {
		{
			actionBuscarTodos = new Action("Buscar todas as duplicatas") {				@Override
				public void run() {
					tvDuplicataPaga.setInput(service.findAll());
					tvDuplicataPaga.refresh();
				}
			};
			actionBuscarTodos.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionBuscarPeriodo = new Action("Buscar por per\u00EDodo") {				@Override
				public void run() {
					try{
						Date dtInicial = FormatterHelper.getDateFormatData().parse(txtDataInicial.getText());
						Date dtFinal = FormatterHelper.getDateFormatData().parse(txtDataFinal.getText());
						
						tvDuplicataPaga.setInput(service.findAllByPeriodo(dtInicial, dtFinal));
					}catch(Exception e){
						openError("Informe as datas corretamente.");
					}
				}
			};
			actionBuscarPeriodo.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
	}

	@Override
	public void setFocus() {
		actionBuscarPeriodo.run();
	}

}
