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
import org.eclipse.swt.graphics.Color;
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
import org.eclipse.wb.swt.SWTResourceManager;

import tela.editor.StatusFuncionarioEditor;
import tela.editor.editorInput.StatusFuncionarioEditorInput;
import tela.filter.StatusFuncionarioFilter;
import aplicacao.service.PessoaService;
import aplicacao.service.StatusServicoService;
import banco.modelo.Pessoa;
import banco.modelo.StatusServico;

public class StatusFuncionarioView extends ViewPart {

	public static final String ID = "tela.view.StatusFuncionarioView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private Action actionAtualizar;
	
	private StatusFuncionarioFilter filtro = new StatusFuncionarioFilter();
	private PessoaService service = new PessoaService();
	private StatusServicoService statusService = new StatusServicoService();
	private TableViewer tvStatus;

	public StatusFuncionarioView() {
		createActions();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmStatusDoFuncionrio = formToolkit.createForm(container);
		frmStatusDoFuncionrio.setImage(ResourceManager.getPluginImage("mecasoft", "assents/statusFuncionario/statusFuncionario32.png"));
		formToolkit.paintBordersFor(frmStatusDoFuncionrio);
		frmStatusDoFuncionrio.setText("Status do funcion\u00E1rio");
		frmStatusDoFuncionrio.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmStatusDoFuncionrio.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmStatusDoFuncionrio.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvStatus.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		tvStatus = new TableViewer(frmStatusDoFuncionrio.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvStatus.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				try {
					IStructuredSelection selecao = (IStructuredSelection) tvStatus.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					Pessoa p = (Pessoa) selecao.getFirstElement();
					getSite().getPage().openEditor(new StatusFuncionarioEditorInput(p), StatusFuncionarioEditor.ID);
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
		
		TableViewerColumn tvcFuncionario = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcFuncionario.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNome();
			}
		});
		TableColumn tblclmnFuncionrio = tvcFuncionario.getColumn();
		tblclmnFuncionrio.setWidth(313);
		tblclmnFuncionrio.setText("Funcion\u00E1rio");
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Pessoa p = (Pessoa)element;
				StatusServico status = statusService.findStatusFuncionario(p);
				
				return status == null ? "" : status.getStatus().getDescricao();
			}
			
			@Override
			public Color getForeground(Object element) {
				Pessoa p = (Pessoa)element;
				StatusServico status = statusService.findStatusFuncionario(p);
				
				if(status == null)
					return SWTResourceManager.getColor(SWT.COLOR_BLACK);
				
				if(status.getStatus().isPausar())
					return SWTResourceManager.getColor(SWT.COLOR_RED);
				
				return SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
			}
		});
		TableColumn tblclmnStatusAtual = tvcStatus.getColumn();
		tblclmnStatusAtual.setWidth(182);
		tblclmnStatusAtual.setText("Status atual");
		frmStatusDoFuncionrio.getToolBarManager().add(actionAtualizar);
		frmStatusDoFuncionrio.updateToolBar();

	}

	private void createActions() {
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvStatus.setInput(service.findAllFuncionariosAtivos());
					tvStatus.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
	}

	@Override
	public void setFocus() {
		actionAtualizar.run();
	}

}
