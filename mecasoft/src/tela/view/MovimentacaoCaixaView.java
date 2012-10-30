package tela.view;

import org.eclipse.jface.action.Action;
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
import org.eclipse.wb.swt.ResourceManager;

import tela.filter.MovimentacaoCaixaFilter;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.MovimentacaoCaixaService;
import banco.modelo.Caixa;
import banco.modelo.MovimentacaoCaixa;

public class MovimentacaoCaixaView extends ViewPart {

	public static final String ID = "tela.view.MovimentacaoCaixaView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Text txtValorAbertura;
	private Text txtTotalEntradas;
	private Text txtTotalSaida;
	private Text txtTotal;
	private Table table;
	
	private MovimentacaoCaixaService service = new MovimentacaoCaixaService();
	private MovimentacaoCaixaFilter filtro = new MovimentacaoCaixaFilter();
	private TableViewer tvMovimentacao;
	private Action actionAtualizar;

	public MovimentacaoCaixaView() {
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
		
		Form frmMovimentaoNoCaixa = formToolkit.createForm(container);
		frmMovimentaoNoCaixa.setImage(ResourceManager.getPluginImage("mecasoft", "assents/caixa/movimentacaoCaixa32.png"));
		formToolkit.paintBordersFor(frmMovimentaoNoCaixa);
		frmMovimentaoNoCaixa.setText("Movimentações no caixa");
		frmMovimentaoNoCaixa.getBody().setLayout(new GridLayout(9, false));
		
		Label lblBuscar = new Label(frmMovimentaoNoCaixa.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmMovimentaoNoCaixa.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvMovimentacao.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 8, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		Label lblValorAbertura = new Label(frmMovimentaoNoCaixa.getBody(), SWT.NONE);
		lblValorAbertura.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		formToolkit.adapt(lblValorAbertura, true, true);
		lblValorAbertura.setText("Valor abertura:");
		
		txtValorAbertura = new Text(frmMovimentaoNoCaixa.getBody(), SWT.BORDER);
		txtValorAbertura.setEnabled(false);
		txtValorAbertura.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtValorAbertura, true, true);
		
		Label lblTotalEntradas = new Label(frmMovimentaoNoCaixa.getBody(), SWT.NONE);
		lblTotalEntradas.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblTotalEntradas, true, true);
		lblTotalEntradas.setText("Total entradas:");
		
		txtTotalEntradas = new Text(frmMovimentaoNoCaixa.getBody(), SWT.BORDER);
		txtTotalEntradas.setEnabled(false);
		txtTotalEntradas.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtTotalEntradas, true, true);
		
		Label lblTotalSadas = new Label(frmMovimentaoNoCaixa.getBody(), SWT.NONE);
		lblTotalSadas.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblTotalSadas, true, true);
		lblTotalSadas.setText("Total sa\u00EDdas:");
		
		txtTotalSaida = new Text(frmMovimentaoNoCaixa.getBody(), SWT.BORDER);
		txtTotalSaida.setEnabled(false);
		txtTotalSaida.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtTotalSaida, true, true);
		
		Label lblTotal = new Label(frmMovimentaoNoCaixa.getBody(), SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblTotal, true, true);
		lblTotal.setText("Total:");
		
		txtTotal = new Text(frmMovimentaoNoCaixa.getBody(), SWT.BORDER);
		txtTotal.setEnabled(false);
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtTotal, true, true);
		
		tvMovimentacao = new TableViewer(frmMovimentaoNoCaixa.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		table = tvMovimentacao.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 9, 1));
		tvMovimentacao.setContentProvider(ArrayContentProvider.getInstance());
		tvMovimentacao.addFilter(filtro);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcNumero = new TableViewerColumn(tvMovimentacao, SWT.NONE);
		tvcNumero.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((MovimentacaoCaixa)element).getId().toString();
			}
		});
		TableColumn tblclmnMovNmero = tvcNumero.getColumn();
		tblclmnMovNmero.setWidth(100);
		tblclmnMovNmero.setText("Mov. N\u00FAmero");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tvMovimentacao, SWT.NONE);
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((MovimentacaoCaixa)element).getValor());
			}
		});
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(100);
		tblclmnValor.setText("Valor");
		
		TableViewerColumn tvcData = new TableViewerColumn(tvMovimentacao, SWT.NONE);
		tvcData.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDateFormatData().format(((MovimentacaoCaixa)element).getDataMovimentacao());
			}
		});
		TableColumn tblclmnData = tvcData.getColumn();
		tblclmnData.setWidth(100);
		tblclmnData.setText("Data");
		
		TableViewerColumn tvcTipo = new TableViewerColumn(tvMovimentacao, SWT.NONE);
		tvcTipo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				MovimentacaoCaixa mc = (MovimentacaoCaixa)element;
				
				if(mc.getTipo().equals(MovimentacaoCaixa.TIPOENTRADA))
					return "Entrada";
				else
					return "Saida";
			}
		});
		TableColumn tblclmnTipo = tvcTipo.getColumn();
		tblclmnTipo.setWidth(54);
		tblclmnTipo.setText("Tipo");
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvMovimentacao, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((MovimentacaoCaixa)element).getStatus();
			}
		});
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(196);
		tblclmnStatus.setText("Status");
		
		TableViewerColumn tvcMotivo = new TableViewerColumn(tvMovimentacao, SWT.NONE);
		tvcMotivo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((MovimentacaoCaixa)element).getMotivo();
			}
		});
		TableColumn tblclmnMotivo = tvcMotivo.getColumn();
		tblclmnMotivo.setWidth(244);
		tblclmnMotivo.setText("Motivo");
		frmMovimentaoNoCaixa.getToolBarManager().add(actionAtualizar);
		frmMovimentaoNoCaixa.updateToolBar();

	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		{
			actionAtualizar = new Action("Atualizar") {
				public void run() {
					Caixa caixa = UsuarioHelper.getCaixa();
					
					//informações dos texts acima da lista de movimentações
					txtValorAbertura.setText(caixa == null ? "" : "R$ " + FormatterHelper.getDecimalFormat().format(caixa.getValorAbertura()));
					txtTotalEntradas.setText(caixa == null ? "" : "R$ " + FormatterHelper.getDecimalFormat().format(service.getTotalCaixaByTipo(caixa, MovimentacaoCaixa.TIPOENTRADA)));
					txtTotalSaida.setText(caixa == null ? "" : "R$ " + FormatterHelper.getDecimalFormat().format(service.getTotalCaixaByTipo(caixa, MovimentacaoCaixa.TIPOSAIDA)));
					txtTotal.setText(caixa == null ? "" : "R$ " + FormatterHelper.getDecimalFormat().format(service.getTotalCaixa(caixa)));
					
					tvMovimentacao.setInput(service.findAllByCaixa(caixa));
					tvMovimentacao.refresh();
									};
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
	}

	@Override
	public void setFocus() {
		actionAtualizar.run();
	}

}
