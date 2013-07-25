package tela.view;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
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
import org.eclipse.wb.swt.ResourceManager;

import tela.editor.FormaPagamentoEditor;
import tela.editor.editorInput.FormaPagamentoEditorInput;
import tela.filter.FormaPagamentoFilter;
import aplicacao.service.FormaPagamentoService;
import banco.modelo.FormaPagamento;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;

public class FormaPagamentoView extends ViewPart {

	public static final String ID = "tela.view.FormaPagamentoView"; //$NON-NLS-1$
	private Logger log = Logger.getLogger(getClass());
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private Action actionAtualizar;
	private Action actionNovo;
	private TableViewer tvFormaPagamento;
	
	private FormaPagamentoService service = new FormaPagamentoService();
	private FormaPagamentoFilter filter = new FormaPagamentoFilter();

	public FormaPagamentoView() {
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
		
		Form frmListaDeFormas = formToolkit.createForm(container);
		frmListaDeFormas.setImage(ResourceManager.getPluginImage("mecasoft", "assents/formaPagamento/formaPagamento32.png"));
		formToolkit.paintBordersFor(frmListaDeFormas);
		frmListaDeFormas.setText("Lista de formas de pagamento");
		frmListaDeFormas.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaDeFormas.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDeFormas.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filter.setSearch(txtFiltro.getText());
				tvFormaPagamento.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtFiltro, true, true);
		txtFiltro.setMessage("Filtro...");
		
		tvFormaPagamento = new TableViewer(frmListaDeFormas.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvFormaPagamento.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection selecao = (IStructuredSelection)tvFormaPagamento.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					FormaPagamento fp = (FormaPagamento)selecao.getFirstElement();
					getSite().getPage().openEditor(new FormaPagamentoEditorInput(fp), FormaPagamentoEditor.ID);
				} catch (PartInitException e) {
					log.error(e);
				}
			}
		});
		table = tvFormaPagamento.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tvFormaPagamento.setContentProvider(ArrayContentProvider.getInstance());
		tvFormaPagamento.addFilter(filter);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcForma = new TableViewerColumn(tvFormaPagamento, SWT.NONE);
		tvcForma.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((FormaPagamento)element).getNome();
			}
		});
		TableColumn tblclmnForma = tvcForma.getColumn();
		tblclmnForma.setWidth(185);
		tblclmnForma.setText("Forma");
		
		TableViewerColumn tvcTipo = new TableViewerColumn(tvFormaPagamento, SWT.NONE);
		tvcTipo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				FormaPagamento fp = (FormaPagamento)element;
				
				if(fp.isGeraPagVista())
					return "Gera pagamento à vista";
				else if(fp.isGeraDuplicata())
					return "Gera duplicatas";
				return "";
			}
		});
		TableColumn tblclmnTipo = tvcTipo.getColumn();
		tblclmnTipo.setWidth(171);
		tblclmnTipo.setText("Tipo");
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvFormaPagamento, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((FormaPagamento)element).getStatus();
			}
		});
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");
		frmListaDeFormas.getToolBarManager().add(actionAtualizar);
		frmListaDeFormas.getToolBarManager().add(actionNovo);
		frmListaDeFormas.updateToolBar();

	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvFormaPagamento.setInput(service.findAll());
					tvFormaPagamento.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new FormaPagamentoEditorInput(), FormaPagamentoEditor.ID);
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
