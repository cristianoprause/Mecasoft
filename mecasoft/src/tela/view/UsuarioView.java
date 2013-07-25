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

import banco.modelo.Usuario;

import tela.editor.UsuarioEditor;
import tela.editor.editorInput.UsuarioEditorInput;
import tela.filter.UsuarioFilter;
import aplicacao.service.UsuarioService;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;

public class UsuarioView extends ViewPart {

	public static final String ID = "tela.view.UsuarioView"; //$NON-NLS-1$
	private Logger log = Logger.getLogger(getClass());
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private Action actionAtualizar;
	private Action actionNovo;
	private TableViewer tvUsuario;
	
	private UsuarioService service;
	private UsuarioFilter filtro;

	public UsuarioView() {
		service = new UsuarioService();
		filtro = new UsuarioFilter();
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
		
		Form frmListaUsuarios = formToolkit.createForm(container);
		frmListaUsuarios.setImage(ResourceManager.getPluginImage("mecasoft", "assents/usuario/usuario32.png"));
		formToolkit.paintBordersFor(frmListaUsuarios);
		frmListaUsuarios.setText("Lista de usuários");
		frmListaUsuarios.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaUsuarios.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaUsuarios.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvUsuario.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		tvUsuario = new TableViewer(frmListaUsuarios.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvUsuario.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selecao = (IStructuredSelection)tvUsuario.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				try {
					getSite().getPage().openEditor(new UsuarioEditorInput((Usuario)selecao.getFirstElement()), UsuarioEditor.ID);
				} catch (PartInitException e) {
					log.error(e);
				}
			}
		});
		table = tvUsuario.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		formToolkit.paintBordersFor(table);
		tvUsuario.addFilter(filtro);
		tvUsuario.setContentProvider(ArrayContentProvider.getInstance());
		
		TableViewerColumn tvcNome = new TableViewerColumn(tvUsuario, SWT.NONE);
		tvcNome.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Usuario)element).getFuncionario().getNomeFantasia();
			}
		});
		TableColumn tblclmnNome = tvcNome.getColumn();
		tblclmnNome.setWidth(185);
		tblclmnNome.setText("Nome");
		
		TableViewerColumn tvcLogin = new TableViewerColumn(tvUsuario, SWT.NONE);
		tvcLogin.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Usuario)element).getLogin();
			}
		});
		TableColumn tblclmnLogin = tvcLogin.getColumn();
		tblclmnLogin.setWidth(154);
		tblclmnLogin.setText("Login");
		
		TableViewerColumn tvcPapel = new TableViewerColumn(tvUsuario, SWT.NONE);
		tvcPapel.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Usuario)element).getPapel().getNome();
			}
		});
		TableColumn tblclmnPapel = tvcPapel.getColumn();
		tblclmnPapel.setWidth(128);
		tblclmnPapel.setText("Papel");
		
		TableViewerColumn tvcSituacao = new TableViewerColumn(tvUsuario, SWT.NONE);
		tvcSituacao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Usuario)element).getStatus();
			}
		});
		TableColumn tblclmnSitucao = tvcSituacao.getColumn();
		tblclmnSitucao.setWidth(100);
		tblclmnSitucao.setText("Situa\u00E7\u00E3o");
		frmListaUsuarios.getToolBarManager().add(actionAtualizar);
		frmListaUsuarios.getToolBarManager().add(actionNovo);
		frmListaUsuarios.updateToolBar();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvUsuario.setInput(service.findAll());
					tvUsuario.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new UsuarioEditorInput(), UsuarioEditor.ID);
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
