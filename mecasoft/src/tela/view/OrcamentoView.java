package tela.view;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import tela.editor.OrcamentoEditor;
import tela.editor.editorInput.OrcamentoEditorInput;
import tela.filter.OrcamentoFilter;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.OrcamentoService;
import banco.modelo.Orcamento;
import org.eclipse.jface.action.Action;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;

public class OrcamentoView extends ViewPart {

	public static final String ID = "tela.view.OrcamentoView"; //$NON-NLS-1$
	private Logger log = Logger.getLogger(getClass());
	private OrcamentoService service = new OrcamentoService();
	private OrcamentoFilter filtro = new OrcamentoFilter();
	
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private TableViewer tvOrcamento;
	private Action actionAtualizar;
	private Action actionNovo;

	public OrcamentoView() {
		createActions();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmOrcamento = formToolkit.createForm(container);
		frmOrcamento.setImage(ResourceManager.getPluginImage("mecasoft", "assents/servicoPrestado/servicoPrestado32.png"));
		formToolkit.paintBordersFor(frmOrcamento);
		frmOrcamento.setText("Or\u00E7amento");
		frmOrcamento.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = formToolkit.createLabel(frmOrcamento.getBody(), "Buscar:", SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtFiltro = new Text(frmOrcamento.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvOrcamento.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		tvOrcamento = new TableViewer(frmOrcamento.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvOrcamento.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection selecao = (IStructuredSelection)tvOrcamento.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					Orcamento orcamento = (Orcamento)selecao.getFirstElement();
					getSite().getPage().openEditor(new OrcamentoEditorInput(orcamento), OrcamentoEditor.ID);
				} catch (PartInitException e) {
					log.error(e);
				}
			}
		});
		table = tvOrcamento.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tvOrcamento.setContentProvider(ArrayContentProvider.getInstance());
		tvOrcamento.addFilter(filtro);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcNumero = new TableViewerColumn(tvOrcamento, SWT.NONE);
		tvcNumero.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Orcamento)element).getNumero();
			}
		});
		TableColumn tblclmnNumero = tvcNumero.getColumn();
		tblclmnNumero.setWidth(100);
		tblclmnNumero.setText("Numero");
		
		TableViewerColumn tvcCliente = new TableViewerColumn(tvOrcamento, SWT.NONE);
		tvcCliente.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Orcamento)element).getCliente().getNome();
			}
		});
		TableColumn tblclmnCliente = tvcCliente.getColumn();
		tblclmnCliente.setWidth(171);
		tblclmnCliente.setText("Cliente");
		
		TableViewerColumn tvcVeiculo = new TableViewerColumn(tvOrcamento, SWT.NONE);
		tvcVeiculo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Orcamento)element).getVeiculo().getNome();
			}
		});
		TableColumn tblclmnVeiculo = tvcVeiculo.getColumn();
		tblclmnVeiculo.setWidth(187);
		tblclmnVeiculo.setText("Ve\u00EDculo");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tvOrcamento, SWT.NONE);
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.formatMoedaDuasCasas(((Orcamento)element).getValorTotal());
			}
		});
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(116);
		tblclmnValor.setText("Valor");
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvOrcamento, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Orcamento)element).getStatusOrcamento();
			}
		});
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");
		frmOrcamento.getToolBarManager().add(actionAtualizar);
		frmOrcamento.getToolBarManager().add(actionNovo);
		frmOrcamento.updateToolBar();
	}

	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvOrcamento.setInput(service.findAll());
					tvOrcamento.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new OrcamentoEditorInput(), OrcamentoEditor.ID);
					} catch (PartInitException e) {
						log.error(e);
					}
				}
			};
			actionNovo.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/add16.png"));
		}
	}

	@Override
	public void setFocus() {
		actionAtualizar.run();
	}

}
