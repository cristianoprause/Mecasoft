package tela.view;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import tela.componentes.MecasoftText;
import tela.dialog.BaixarDuplicataDialog;
import tela.filter.BaixaDuplicataFilter;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.DuplicataService;
import banco.modelo.Duplicata;

public class BaixaDuplicataView extends ViewPart {

	public static final String ID = "tela.view.BaixaDuplicataView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Action actionBuscarTodas;
	private Action actionBaixar;
	private Action actionBuscarPeriodo;
	private Text txtFiltro;
	private Table table;
	private TableViewer tvDuplicata;
	
	private DuplicataService service = new DuplicataService();
	private BaixaDuplicataFilter filter = new BaixaDuplicataFilter();
	private MecasoftText txtDataInicial;
	private MecasoftText txtDataFinal;

	public BaixaDuplicataView() {
		createActions();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmBaixaDeDuplicatas = formToolkit.createForm(container);
		frmBaixaDeDuplicatas.setImage(ResourceManager.getPluginImage("mecasoft", "assents/duplicata/duplicata32.png"));
		formToolkit.paintBordersFor(frmBaixaDeDuplicatas);
		frmBaixaDeDuplicatas.setText("Baixa de duplicatas");
		frmBaixaDeDuplicatas.getBody().setLayout(new GridLayout(4, false));
		
		Label lblBuscar = new Label(frmBaixaDeDuplicatas.getBody(), SWT.NONE);
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmBaixaDeDuplicatas.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filter.setSearch(txtFiltro.getText());
				tvDuplicata.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		Label lblPerodoDe = new Label(frmBaixaDeDuplicatas.getBody(), SWT.NONE);
		formToolkit.adapt(lblPerodoDe, true, true);
		lblPerodoDe.setText("Per\u00EDodo de");
		
		txtDataInicial = new MecasoftText(frmBaixaDeDuplicatas.getBody(), SWT.NONE);
		txtDataInicial.setOptions(MecasoftText.NUMEROS, 10);
		txtDataInicial.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		GridData gd_txtDataInicial = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtDataInicial.widthHint = 95;
		txtDataInicial.setLayoutData(gd_txtDataInicial);
		formToolkit.adapt(txtDataInicial);
		formToolkit.paintBordersFor(txtDataInicial);
		
		//seta a data inicial como 1 mes atraz
		Calendar dtInicial = Calendar.getInstance();
		dtInicial.add(Calendar.MONTH, -1);
		txtDataInicial.setText(FormatterHelper.getDateFormatData().format(dtInicial.getTime()));
		
		Label lblAte = new Label(frmBaixaDeDuplicatas.getBody(), SWT.NONE);
		formToolkit.adapt(lblAte, true, true);
		lblAte.setText("at\u00E9");
		
		txtDataFinal = new MecasoftText(frmBaixaDeDuplicatas.getBody(), SWT.NONE);
		txtDataFinal.setOptions(MecasoftText.NUMEROS, 10);
		txtDataFinal.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		GridData gd_txtDataFinal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtDataFinal.widthHint = 95;
		txtDataFinal.setLayoutData(gd_txtDataFinal);
		formToolkit.adapt(txtDataFinal);
		formToolkit.paintBordersFor(txtDataFinal);
		
		//seta a data final com a data atual
		Calendar dtFinal = Calendar.getInstance();
		txtDataFinal.setText(FormatterHelper.getDateFormatData().format(dtFinal.getTime()));	
		
		tvDuplicata = new TableViewer(frmBaixaDeDuplicatas.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		table = tvDuplicata.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		tvDuplicata.setContentProvider(ArrayContentProvider.getInstance());
		tvDuplicata.addFilter(filter);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcNumero = new TableViewerColumn(tvDuplicata, SWT.NONE);
		tvcNumero.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Duplicata)element).getNumero();
			}
		});
		TableColumn tblclmnNmero = tvcNumero.getColumn();
		tblclmnNmero.setWidth(100);
		tblclmnNmero.setText("Número");
		
		TableViewerColumn tvcServico = new TableViewerColumn(tvDuplicata, SWT.NONE);
		tvcServico.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Duplicata)element).getServicoPrestado().getId().toString();
			}
		});
		TableColumn tblclmnServico = tvcServico.getColumn();
		tblclmnServico.setWidth(156);
		tblclmnServico.setText("Servi\u00E7o");
		
		TableViewerColumn tvcDataVencimento = new TableViewerColumn(tvDuplicata, SWT.NONE);
		tvcDataVencimento.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDateFormatData().format(((Duplicata)element).getDataVencimento());
			}
		});
		TableColumn tblclmnDataVencimento = tvcDataVencimento.getColumn();
		tblclmnDataVencimento.setWidth(167);
		tblclmnDataVencimento.setText("Data Vencimento");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tvDuplicata, SWT.NONE);
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((Duplicata)element).getValor());
			}
		});
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(137);
		tblclmnValor.setText("Valor");
		
		Menu menu = new Menu(table);
		table.setMenu(menu);
		
		MenuItem mntmBaixar = new MenuItem(menu, SWT.NONE);
		mntmBaixar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/duplicata/baixar20.png"));
		mntmBaixar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IStructuredSelection selecao = (IStructuredSelection)tvDuplicata.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(UsuarioHelper.getCaixa() == null){
					openError("O caixa esta fechado.\nAbra-o primeiro para depois baixar as duplicatas.");
					return;
				}
				
				Duplicata duplicata = (Duplicata)selecao.getFirstElement();
				new BaixarDuplicataDialog(getActiveShell(), duplicata).open();
				
			}
		});
		mntmBaixar.setText("Baixar");
		frmBaixaDeDuplicatas.getToolBarManager().add(actionBuscarTodas);
		frmBaixaDeDuplicatas.getToolBarManager().add(actionBuscarPeriodo);
		frmBaixaDeDuplicatas.getToolBarManager().add(actionBaixar);
		frmBaixaDeDuplicatas.updateToolBar();

	}

	private void createActions() {
		{
			actionBuscarTodas = new Action("Buscar todas as duplicatas") {				@Override
				public void run() {
					tvDuplicata.setInput(service.findAllNaoPagas());
					tvDuplicata.refresh();
				}
			};
			actionBuscarTodas.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionBaixar = new Action("Baixar duplicata") {				@Override
				public void run() {
					if(UsuarioHelper.getCaixa() == null){
						openError("O caixa esta fechado.\nAbra-o primeiro para depois baixar as duplicatas.");
						return;
					}
					
					new BaixarDuplicataDialog(getActiveShell(), new Duplicata()).open();
				}
			};
			actionBaixar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/duplicata/baixar20.png"));
		}
		{
			actionBuscarPeriodo = new Action("Buscar duplicatas no período") {				@Override
				public void run() {
					Date dtInicial;
					Date dtFinal;
					try{
						dtInicial = FormatterHelper.getDateFormatData().parse(txtDataInicial.getText());
						dtFinal = FormatterHelper.getDateFormatData().parse(txtDataFinal.getText());
					}catch(Exception e){
						openError("Informe o período corretamente.");
						return;
					}
					
					tvDuplicata.setInput(service.findAllNaoPagasByPeriodo(dtInicial, dtFinal));
					tvDuplicata.refresh();
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
