package tela.view;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
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

import tela.editor.PapelEditor;
import tela.editor.editorInput.PapelEditorInput;
import tela.filter.PapelFilter;
import aplicacao.service.PapelService;
import banco.modelo.Papel;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class PapelView extends ViewPart {

	public static final String ID = "tela.view.PapelView"; //$NON-NLS-1$
	private Logger log = Logger.getLogger(getClass());
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private Action actionAtualizar;
	private Action actionNovo;
	private TableViewer tvPapel;
	
	private PapelService service;
	private PapelFilter filtro;

	public PapelView() {
		service = new PapelService();
		filtro = new PapelFilter();
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
		
		Form frmListaDePapis = formToolkit.createForm(container);
		frmListaDePapis.setImage(ResourceManager.getPluginImage("mecasoft", "assents/usuario/papel32.png"));
		formToolkit.paintBordersFor(frmListaDePapis);
		frmListaDePapis.setText("Lista de pap\u00E9is");
		frmListaDePapis.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaDePapis.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDePapis.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvPapel.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		tvPapel = new TableViewer(frmListaDePapis.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvPapel.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selecao = (IStructuredSelection)tvPapel.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				try {
					getSite().getPage().openEditor(new PapelEditorInput((Papel)selecao.getFirstElement()), PapelEditor.ID);
				} catch (PartInitException e) {
					log.error(e);
				}
			}
		});
		table = tvPapel.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tvPapel.addFilter(filtro);
		tvPapel.setContentProvider(ArrayContentProvider.getInstance());
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcNome = new TableViewerColumn(tvPapel, SWT.NONE);
		tvcNome.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Papel)element).getNome();				
			}
		});
		TableColumn tblclmnNome = tvcNome.getColumn();
		tblclmnNome.setWidth(582);
		tblclmnNome.setText("Nome");
		frmListaDePapis.getToolBarManager().add(actionAtualizar);
		frmListaDePapis.getToolBarManager().add(actionNovo);
		frmListaDePapis.updateToolBar();

	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvPapel.setInput(service.findAll());
					tvPapel.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new PapelEditorInput(), PapelEditor.ID);
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
