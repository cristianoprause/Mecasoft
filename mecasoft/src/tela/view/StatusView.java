package tela.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.action.Action;
import org.eclipse.wb.swt.ResourceManager;

import tela.editor.StatusEditor;
import tela.editor.editorInput.StatusEditorInput;
import tela.filter.StatusFilter;

import aplicacao.service.StatusService;
import banco.modelo.Status;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class StatusView extends ViewPart {

	public static final String ID = "tela.view.StatusView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private TableViewer tvStatus;
	private Action actionAtualizar;
	private Action actionNovo;
	
	private StatusService service = new StatusService();
	private StatusFilter filtro = new StatusFilter();

	public StatusView() {
		createActions();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmListaDeStatus = formToolkit.createForm(container);
		frmListaDeStatus.setImage(ResourceManager.getPluginImage("mecasoft", "assents/status/status32.png"));
		formToolkit.paintBordersFor(frmListaDeStatus);
		frmListaDeStatus.setText("Lista de status");
		frmListaDeStatus.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaDeStatus.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDeStatus.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvStatus.refresh();
			}
		});
		txtFiltro.setMessage("Filtro...");
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtFiltro, true, true);
		
		tvStatus = new TableViewer(frmListaDeStatus.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvStatus.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection selecao = (IStructuredSelection)tvStatus.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					Status s = (Status)selecao.getFirstElement();
					getSite().getPage().openEditor(new StatusEditorInput(s), StatusEditor.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		table = tvStatus.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tvStatus.setContentProvider(ArrayContentProvider.getInstance());
		tvStatus.addFilter(filtro);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Status)element).getDescricao();
			}
			
			@Override
			public Color getForeground(Object element) {
				Status s = (Status) element;
				
				if(s.isPausar())
					return SWTResourceManager.getColor(SWT.COLOR_RED);
				else
					return SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
			}
		});
		TableColumn tblclmnDescricao = tvcDescricao.getColumn();
		tblclmnDescricao.setWidth(381);
		tblclmnDescricao.setText("Descri\u00E7\u00E3o");
		
		final TableViewerColumn tvcAtuacao = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcAtuacao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Status s = (Status)element;

				if(s.isPausar())
					return "Pausa o serviço";
				else
					return "Da continuidade ao serviço";
			}
			
			@Override
			public Color getForeground(Object element) {
				Status s = (Status) element;
				
				if(s.isPausar())
					return SWTResourceManager.getColor(SWT.COLOR_RED);
				else
					return SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
			}
			
		});
		TableColumn tblclmnAtuacao = tvcAtuacao.getColumn();
		tblclmnAtuacao.setWidth(161);
		tblclmnAtuacao.setText("Atuação");
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Status)element).getStatus();
			}
			
			@Override
			public Color getForeground(Object element) {
				Status s = (Status) element;
				
				if(s.isPausar())
					return SWTResourceManager.getColor(SWT.COLOR_RED);
				else
					return SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
			}
		});
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");
		frmListaDeStatus.getToolBarManager().add(actionAtualizar);
		frmListaDeStatus.getToolBarManager().add(actionNovo);
		frmListaDeStatus.updateToolBar();
	}

	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvStatus.setInput(service.findAll());
					tvStatus.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new StatusEditorInput(), StatusEditor.ID);
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
