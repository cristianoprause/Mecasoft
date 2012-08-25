package tela.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
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

import tela.editor.TipoFuncionarioEditor;
import tela.editor.editorInput.TipoFuncionarioEditorInput;
import tela.filter.TipoFuncionarioFilter;
import aplicacao.service.TipoFuncionarioService;
import banco.modelo.TipoFuncionario;

public class TipoFuncionarioView extends ViewPart {

	public static final String ID = "tela.view.TipoFuncionarioView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private Action actionAtualizar;
	private Action actionNovo;
	private TableViewer tvTipoFuncionario;
	
	private TipoFuncionarioService service = new TipoFuncionarioService();
	private TipoFuncionarioFilter filtro = new TipoFuncionarioFilter();

	public TipoFuncionarioView() {
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
		
		Form frmListaDeTipos = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frmListaDeTipos);
		frmListaDeTipos.setText("Lista de tipos de funcion\u00E1rio");
		frmListaDeTipos.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaDeTipos.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDeTipos.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvTipoFuncionario.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtFiltro, true, true);
		
		tvTipoFuncionario = new TableViewer(frmListaDeTipos.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvTipoFuncionario.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection selecao = (IStructuredSelection)tvTipoFuncionario.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					getSite().getPage().openEditor(new TipoFuncionarioEditorInput((TipoFuncionario)selecao.getFirstElement()), TipoFuncionarioEditor.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		table = tvTipoFuncionario.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tvTipoFuncionario.setContentProvider(ArrayContentProvider.getInstance());
		tvTipoFuncionario.addFilter(filtro);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcNome = new TableViewerColumn(tvTipoFuncionario, SWT.NONE);
		tvcNome.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((TipoFuncionario)element).getNome();
			}
		});
		TableColumn tblclmnNome = tvcNome.getColumn();
		tblclmnNome.setWidth(1000);
		tblclmnNome.setText("Nome");
		frmListaDeTipos.getToolBarManager().add(actionAtualizar);
		frmListaDeTipos.getToolBarManager().add(actionNovo);
		frmListaDeTipos.updateToolBar();

	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvTipoFuncionario.setInput(service.findAll());
					tvTipoFuncionario.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new TipoFuncionarioEditorInput(), TipoFuncionarioEditor.ID);
					} catch (PartInitException e) {
						e.printStackTrace();
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
